GitHub repotories can optionally be configured to deliver notifications for repository
push events to external systems using the
[post-receive hooks](https://help.github.com/articles/post-receive-hooks) mechanism.
Ready-made post-receive hooks are provided for many systems such
as issue trackers and CI servers, but custom hooks can also be implemented.

github-hook-jar is a container for running existing and custom
[github-services](https://github.com/github/github-services) inside a Java web application
server (i.e. servlet container). github-hook-jar is a polyglot container and allows
running Ruby based github-services simultaneously with services implemented in other
languages, such as Java.

github-hook-jar can be configured with an ordered set of hooks (chain) to handle
post-receive events from one or multiple GitHub repositories. It's also possible to
configure a single github-hook-jar container with separate hook chains, each bound to a
different repository.

Below is an illustration of two GitHub repositories configured to deliver post-receive events
to a single github-hook-jar container. The container has been configured with a separate
hook chain for each repository.

<img src="https://raw.github.com/marko-asplund/github-hook-jar/master/doc/images/ghj-diagram.png">
