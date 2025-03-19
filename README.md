# Webapp Static Resource Compressor Maven Plugin

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/license/mit)
&nbsp; [![CodeFactor](https://www.codefactor.io/repository/github/edwgiz/webapp-static-resource-compressor-maven-plugin/badge)](https://www.codefactor.io/repository/github/edwgiz/webapp-static-resource-compressor-maven-plugin)
&nbsp; [![Latest Maven Central release](https://img.shields.io/maven-central/v/io.github.edwgiz.maven/webapp-static-resource-compressor-maven-plugin.svg?logo=java)](http://mvnrepository.com/artifact/io.github.edwgiz.maven/webapp-static-resource-compressor-maven-plugin)

[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)

A Maven plugin to automatically generate compressed versions (gzip and [Brotli](https://en.wikipedia.org/wiki/Brotli)) of static resources in your web application. 
This improves web performance by reducing bandwidth usage and load times.

## Features

- Compresses JavaScript, CSS, HTML, and other text-based static files.
- Supports Gzip and Brotli compression.
- Seamless integration into Maven build lifecycle.
- Configurable resource inclusion/exclusion patterns.

## Configuration

Add the plugin definition to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.edwgiz.maven</groupId>
            <artifactId>webapp-static-resource-compressor-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <phase>prepare-package</phase>
                    <goals>
                        <goal>add-compressed-resources</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <resources>
                    <resource>
                        <!-- default
                        <skip>false</skip>
                        <outputDirectory>${project.build.outputDirectory}</outputDirectory>
                        -->
                        <subDirectory>webapp/static</subDirectory>
                        <includes>
                            <include>**/*.html</include>
                            <include>**/*.css</include>
                            <include>**/*.js</include>
                        </includes>
                    </resource>
                </resources>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Usage

Run the plugin with:

```sh
mvn io.github.edwgiz.maven:webapp-static-resource-compressor-maven-plugin:add-compressed-resources
```

or as part of the build:

```sh
mvn clean package
```

## License

This project is licensed under the MIT License.
