## tweetspace
An example for the kappa architecture, a big data architecture centralized around the log as unifying data abstraction.


### Local dev setup
```bash
confluent start schema-registry #starts kafka, zookeeper, schema-registry
docker run -it -p 50070:50070 -p 8030:8030 sequenceiq/hadoop-docker:2.7.1 /etc/bootstrap.sh -bash
lein run #execute in tweetbird directory
```
