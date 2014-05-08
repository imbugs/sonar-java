Sonar Java
==========

Supports Java 8 projects (08/05/2014)

Based on Java Ecostem 2.2 (SonarQube 4.3)

### Compile Findbugs 3.0.0-dev-20140323-8036a5d9

~~~
git clone https://code.google.com/p/findbugs
cd findbugs
git checkout 8036a5d9
git rm findbugsTestCases/src/java/com/google/errorprone/bugpatterns/LongLiteralLowerCaseSuffixPositiveCase1.java
git rm findbugsTestCases/src/java/com/google/errorprone/bugpatterns/LongLiteralLowerCaseSuffixPositiveCase2.java
ant
~~~

This will put libs into `findbugs/findbugs/lib`. You need the `findbugs.jar` and
`bcel-6.0-SNAPSHOT.jar` (this one, and no other!).

### Compile Sonar Java Ecosystem 2.3-SNAPSHOT

Clone this repository, then:

~~~
cd sonar-java
mvn package -DskipTests
~~~

Search for `sonar-*SHOT.jar` and pick:

~~~
sonar-findbugs-plugin-2.3-SNAPSHOT.jar
sonar-jacoco-listeners-2.3-SNAPSHOT.jar
sonar-jacoco-plugin-2.3-SNAPSHOT.jar
sonar-java-plugin-2.3-SNAPSHOT.jar
sonar-squid-java-plugin-2.3-SNAPSHOT.jar
sonar-surefire-plugin-2.3-SNAPSHOT.jar
~~~

### Troubleshooting

SonarQube 4.3 still bundles the 2.1 versions of the plugins, so if you install
the new plugins, you should probably remove everything in `lib/bundled-plugins`
on your sonar server.
