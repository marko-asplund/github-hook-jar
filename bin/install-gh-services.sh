#!/bin/sh

SERVICES_REPO=https://github.com/marko-asplund/github-services.git
#SERVICES_REPO=git@github.com:marko-asplund/github-services.git

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
jruby -S script/bootstrap

# jruby-openssl needs to be installed manually
jruby -S gem install -i vendor/gems/jruby/1.8 jruby-openssl

# add jruby-openssl to jruby LOAD_PATH
echo >> .bundle/loadpath
find vendor/gems -regex .*/jruby-openssl.*/lib/shared -type d >> .bundle/loadpath
find vendor/gems -regex .*/bouncy-castle-java.*/lib   -type d >> .bundle/loadpath

rm -f requires.rb
for i in `ls services/*.rb`; do
  echo "require '$i'" >> requires.rb
done