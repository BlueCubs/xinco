#!/bin/bash
set -e
"/opt/homebrew/Cellar/maven/3.9.12/libexec/bin/mvn" -f "/Users/javierortiz/Documents/NetBeansProjects/xinco/pom.xml" com.cosium.code:git-code-format-maven-plugin:on-pre-commit 
