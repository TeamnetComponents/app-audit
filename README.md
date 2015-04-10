#App-Audit

Core features for auditing using annotations and Spring AOP. 
Provides an API and a template for implementing custom auditing strategies.

## Getting Started
To add App-Audit to your maven dependencies, include the following code in your pom.xml file:

```
        <dependency>
            <groupId>ro.teamnet.audit</groupId>
            <artifactId>app-audit</artifactId>
            <version>${app-audit.version}</version>
        </dependency>
```
App-Audit will not perform any auditing by itself. You need to write your own implementation, or provide a jar with an
existing implementation in your application dependencies.