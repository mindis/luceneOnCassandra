luceneOnCassandra
=================

The goal of this project is to have the lucene index stored in Apache Cassandra.
With this, Apache Cassandra serve as the medium for indexing and querying
without changes to other lucene indexing and querying API.

change the following configuration in apache-cassandra-1.0.8
* apache-cassandra-1.0.8/conf/cassandra.yaml
* apache-cassandra-1.0.8/conf/log4j-server.properties


In order to quickly understand this project, let's start cassandra locally for testing.

1. cd to apache-cassandra-1.0.8/bin.
2. start it by 
   $ cassandra -f

to run the test cases, you must
1. start the cassandra.
2. run pom.xml or in eclipse using junit.
