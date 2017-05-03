# Data Integartion Server

## Overview

Generic data integration server (DIS) can be used to built custom data integration server. It is based on [Pentaho DI CE](http://community.pentaho.com/projects/data-integration/). Custom DIS should be embedded in Tomcat 8.x or higher.
DIS supports following:
	using pehtaho scripts repository,
	executes scripts on schedule,
	executes scripts on request from http or web socket client


## Building From Source

[Maven](http://maven.apache.org) is used to build and deploy.

Run build jar:

```sh
$ mvn package
```

The resulting jar files are in `target/`.

Generate the documentation:

```sh
$ mvn javadoc:javadoc
```

The resulting HTML files are in `target/site/apidocs/`.


## Using From Maven

Any Maven based project can use it directly by adding the appropriate entries to the
`dependencies` section of its `pom.xml` file:

```xml
<dependencies>
  <dependency>
    <groupId>org.homedns.mkh</groupId>
    <artifactId>dis</artifactId>
    <version>0.0.5</version>
  </dependency>
</dependencies>
```


## Using From Binaries

Packaged jars can be downloaded directly from the [Releases page](https://github.com/khomisha/dis/releases).


## Contact

* mkhodonov@gmail.com

## Donate

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://load.payoneer.com/LoadToPage.aspx)

## License

Apache License, Version 2.0
Copyright (c) 2015-2017 Mikhail Khodonov.
It is free software and may be redistributed under the terms specified
in the LICENSE file.


