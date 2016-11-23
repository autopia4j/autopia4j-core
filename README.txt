autopia4j is an open source automation framework, designed to work with any Java based automation tool. The most prominent implementation of the framework is on top of Selenium WebDriver. A SoapUI implementation is also available.

The autopia4j-core project contains the core libraries which are common to all implementations of the framework. Note that the core library by itself is not a complete framework, and needs to be combined with a tool specific implementation.

How to build the library from source:
1. Check out the latest version from BitBucket
2. Run "mvn clean install"

How to generate the documentation:
1. Run "mvn javadoc:javadoc"