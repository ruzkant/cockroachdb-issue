# Cockroach jdbc hangs with certain order of queries 

This was tested against cockroach 2.0.0 on linux 64bit.

## The issue


The main issue occurs if the following sequence is executed in a transaction:

1. select query (or something like show tables)
1. create table
1. insert into new table

Interestingly enough if you add a delay after the first query you get an 
exception that the table does not exist instead of the hanging.

Also if you delay the create statement as well then it goes back to hanging.

This seems like some pg wire protocol issue, but I don't know enough to be able to say for sure.

## Setup cockroachdb

Run the following queries (from create.sql file in repo):

``` 
create database cockroach_issue;
create user cockroach_issue_user;
grant all on database cockroach_issue to cockroach_issue_user;
```

Note the lack of password for user as I am using --insecure (2.0 does not support password setting in insecure mode).

## Building and running the project

``` 
mvn package
java -jar target/cockroach-issue.jar
```

## Different scenarios:

The command line arguments are:
# boolean: delay after first query
# boolean: delay after create statement


### 1. Hangs

```
java -jar target/cockroach-issue.jar false false
``` 
### 2. Hangs

``` 
java -jar target/cockroach-issue.jar false true
```

### 3. Hangs

``` 
java -jar target/cockroach-issue.jar true true
```

### 4. Generates Exception 

``` 
java -jar target/cockroach-issue.jar true false
```
