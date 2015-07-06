[![Build Status](https://travis-ci.org/btravers/snmptrans_gui.svg?branch=master)](https://travis-ci.org/btravers/snmptrans_gui)

# SNMPTRANS GUI

## Introduction

This project provides a graphical interface for viewing and editing [snmptrans](https://github.com/btravers/snmptrans) configuration documents which are saved in an elasticsearch cluster.

## Building

Install [snmptrans](https://github.com/btravers/snmptrans) project. Once install, build the war using Maven:

    mvn clean install
    
## Running

Deploy the resulting war in your Tomcat web server.

Some options can be overridden on the command line by doing:

    -Delasticsearch.host=HOST:PORT
    -Delasticseach.path=NEW_PATH
    -Delasticseach.cluster.name=NEW_CLUSTER_NAME