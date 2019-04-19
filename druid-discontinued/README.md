### Druid

Druid is a tool that can rapidly ingest lots of data from various sources and make them available for queries with apache calcite, as SQL dialect.

It's an interesting technology to explore, but is not implemented (deployed on kubernetes) in the tweetspace project yet (the distributed setup is quite complicated, so without the use of helm charts this would be quite an effort)

Attached in this folder, you will find an example Druid source specification that ingests the tweetspace avro into druid.

