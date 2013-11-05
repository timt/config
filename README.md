config
======

A really simple configuration library implemented in plain Java with no dependencies

Order of precedence
-------------------
System properties -> application.properties -> fallback.properties

Properties are immutable once defined they can not be overridden, e.g. fallback properties can not override application properties can not override system properties.

For example fallback.properties could be baked into the artifact(.jar), the application.properties providing environment specific properties, and system properties providing properties at application startup.

Usage
-----
    import io.shaka.config.*;
    ...
    Config config = Config.load();
    String aech = config.get("helen.harris");

The above example loads the config files from classpath root package. To load config files from the package some.other:

    Config config = Config.load("some/other")



Templated Properties
--------------------
    first.name=Wade
    second.name=Owen
    greeting=hello ${first.name} ${second.name}
    ...
    config.get("greeting") returns "hello Wade Owen"

Recursively merges templated properties

    alter.ego=Parzival
    name=${alter.ego}(Wade Owen)
    greeting=I know who ${name} really is
    ...
    config.get("greeting") returns "I know who Parzival(Wade Owen) really is"


Maven Repo
----------
http://timt.github.io/repo/releases/

Code license
------------
Apache License 2.0