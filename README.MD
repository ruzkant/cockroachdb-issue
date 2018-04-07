
h1. Cockroach jdbc hangs with certain order of queries 

This was tested against cockroach 2.0.0

h2. The issue


The main issue occurs if the following sequence is executed in a transaction:

# select query (or something like show tables)
# create table
# insert into new table

Interestingly enough if you add a delay after the first query you get an 
exception that the table does not exist instead of the hanging.

Also if you delay the create statement as well then it goes back to hanging.

This seems like some pg wire protocol issue, but I don't know enough to be able to say for sure.

h2. Setup cockroachdb

Run the following queries (from create.sql file in repo):

bc. 
create database cockroach_issue;
create user cockroach_issue_user;
grant all on database cockroach_issue to cockroach_issue_user;

Note the lack of password for user as I am using --insecure (2.0 does not support password setting in insecure mode).

h2. Building and running the project

bc. 
mvn package
java -jar target/cockroach-issue.jar

h2. Different scenarios:

The command line arguments are:
# boolean: delay after first query
# boolean: delay after create statement


h3. 1. Hangs

bc. 
java -jar target/cockroach-issue.jar false false
 
h3. 2. Hangs

bc. 
java -jar target/cockroach-issue.jar false true

h3. 3. Hangs

bc. 
java -jar target/cockroach-issue.jar true true

h3. 4. Generates Exception 

bc. 
java -jar target/cockroach-issue.jar true false

