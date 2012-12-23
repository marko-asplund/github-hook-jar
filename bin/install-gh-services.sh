#!/bin/sh

#
# tested on Mac OS X 10.7 and 10.8
#
# prerequisites:
#
# mac os x
# - Xcode + command line tools (build environment for native extensions e.g. thin).
#		you may need to add the compiler toolchain to path.
#		(tested with Xcode 4.5.2)
#
#


SERVICES_REPO=https://github.com/marko-asplund/github-services.git
#SERVICES_REPO=https://github.com/github/github-services.git
RUBY_VERSION=1.8

# GitHub Services requires Ruby 1.8.7
export JRUBY_OPTS="--$RUBY_VERSION"

git clone $SERVICES_REPO
if [ ! -d "github-services" ]; then
  echo "github-services does not exist, aborting"
  exit 1
fi
cd github-services 

# unset RVM environment settings, if using RVM.
rvm reset

# install Bundler
jruby -S gem install bundler

# install required Ruby gems
JRUBY_OPTS="$JRUBY_OPTS -Xcext.enabled=true" jruby -S script/bootstrap

# jruby-openssl needs to be installed manually
jruby -S gem install -i vendor/gems/jruby/$RUBY_VERSION jruby-openssl

# add jruby-openssl to jruby LOAD_PATH
echo >> .bundle/loadpath
find vendor/gems -regex .*/jruby-openssl.*/lib/shared -type d >> .bundle/loadpath
find vendor/gems -regex .*/bouncy-castle-java.*/lib   -type d >> .bundle/loadpath

rm -f requires.rb
for i in `ls services/*.rb`; do
  echo "require '$i'" >> requires.rb
done
