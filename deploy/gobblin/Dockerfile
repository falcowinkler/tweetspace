FROM openjdk:8-slim

RUN apt-get update && apt-get install -y wget && \
    wget https://github.com/apache/incubator-gobblin/archive/release-0.14.0.zip &&  \
    unzip release-0.14.0.zip && \
    cd incubator-gobblin-release-0.14.0 && \
    ./gradlew clean :gobblin-distribution:buildDistributionTar -PhadoopVersion=2.8.1 &&  \
    cd .. &&  \
    mv incubator-gobblin-release-0.14.0/build/gobblin-distribution/distributions/apache-gobblin-incubating-bin-0.14.0.tar.gz . && \
    rm -rf incubator-gobblin-release-0.14.0 && \
    tar -xzf apache-gobblin-incubating-bin-0.14.0.tar.gz && \
    rm apache-gobblin-incubating-bin-0.14.0.tar.gz &&  \
    mkdir gobblin-workdir gobblin-jobconf

ENV GOBBLIN_WORK_DIR=/gobblin-workdir
ENV GOBBLIN_JOB_CONFIG_DIR=/gobblin-jobconf
WORKDIR /gobblin-dist
ENTRYPOINT ["bin/gobblin-standalone.sh" "start"]
