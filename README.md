config
======

A really simple configuration library implemented in plain Java with no dependencies

Order of precedence
-------------------
System properties -> application.properties -> fallback.properties

Properties are immutable once defined they can not be overridden, e.g. fallback properties can not override application properties can not override system properties.

Usage
-----
    import io.shaka.config.*;
    ...
    Config config = Config.load();
    String foo = config.get("bar");
    

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