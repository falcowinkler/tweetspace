FROM openjdk:8-slim

RUN apt-get update && apt-get install -y wget && \
    wget https://github.com/apache/incubator-gobblin/archive/master.zip &&  \
    unzip master.zip && \
    cd incubator-gobblin-master && \
    ./gradlew clean :gobblin-distribution:buildDistributionTar -PhadoopVersion=2.8.1 &&  \
    cd .. &&  \
    mv incubator-gobblin-master/build/gobblin-distribution/distributions/apache-gobblin-incubating-bin-0.15.0.tar.gz . && \
    rm -rf incubator-gobblin-master && \
    tar -xzf apache-gobblin-incubating-bin-0.15.0.tar.gz && \
    rm apache-gobblin-incubating-bin-0.15.0.tar.gz &&  \
    mkdir gobblin-workdir gobblin-jobconf
    
RUN ENV GOBBLIN_WORK_DIR=/gobblin-workdir
RUN ENV GOBBLIN_JOB_CONFIG_DIR=/gobblin-jobconf
WORKDIR /gobblin-dist
ENTRYPOINT ["bin/gobblin-standalone.sh" "start"]
