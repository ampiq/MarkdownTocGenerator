# Markdown TOC Generator

Cli-app that provides simple way to generate a table of contentens for your markdown

### Prerequisites

```
Java 8+
maven 3.5+
enabled annotation processor
```

### Usage

You can run cli-app and generate markdown by command below, where "fromFile" is correct file path

```
java -jar jarname.jar -from=<fromFile>
```

For example, you can just do this under terminal

```
cd target

java -jar MarkDownGenerator-1.0-SNAPSHOT-jar-with-dependencies.jar -from="C:\Users\Desktop\markdown.txt"
```

## Installation and development setup

To make jar use(jar will be ready for usage in /target: MarkDownGenerator-1.0-SNAPSHOT-jar-with-dependencies.jar)

```sh
mvn package
```

## Release

https://github.com/ampiq/MarkdownTocGenerator/releases/tag/1.0.0

## Meta

David Guyo
