
GitHub repotories can optionally be configured to deliver notifications for repository
push events to external systems using the
[post-receive hooks](https://help.github.com/articles/post-receive-hooks) mechanism.
Ready-made post-receive hooks are provided for many systems such
as issue trackers and CI server, but custom hooks can also be implemented.

<img style="width:200px; height:auto"
src="https://raw.github.com/marko-asplund/github-hook-jar/master/doc/images/ghj-diagram.png">

github-hook-jar is a container for running existing and custom
[github-services](https://github.com/github/github-services) inside a Java web application
server (i.e. servlet container). github-hook-jar is a polyglot container and allows
running Ruby based github-services simultaneously with services implemented in other
languages, such as Java.

An ordered set of hooks (chain) can be configured to handle post-receive events from one
or multiple GitHub repositories. It's also possible to configure a single github-hook-jar
container with separate hook chains, each bound to a different repository.

