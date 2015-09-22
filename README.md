# MRv2 UI 

## Overview

"MRv2 UI" is a web interface of job history of Hadoop MapReduce v2(MRv2) and works on Apache Tomcat.
MRv2 is run on Hadoop YARN environment. And MRv2 UI uses the information of Hadoop YARN environment. MRv2 UI aims to run in the same Tomcat as Tez UI.

MRv2 UI provides the following information.

  * MapReduce v2 job list
  * MapReduce v2 job summary
  * Task list of MapReduce v2 job

## Requirement

* Java 1.7+
* Apache Tomcat 8+
* Apache Maven 3+

* Running Hadoop YARN TimelineServer (Apache Hadoop 2.6+)

## How to build and install

  * Run "git clone" command

        $ git clone https://github.com/sinchii/mrv2-ui.git mrv2-ui

  * Run mvn command

        $ cd mrv2-ui
        $ mvn clean package

  * (if needed) Start Tomcat

        $ ${TOMCAT_HOME}/bin/catalina.sh start

  * Move mrv2-ui.war to Tomcat webapps directory

        $ cp target/mrv2-ui.war ${TOMCAT_HOME}/webapps/

  * Configure web.xml after MRv2UI application deployed by Tomcat. 
  Set the address of TimelineServer to "mrv2ui.tls.http.address"

        $ vim ${TOMCAT_HOME}/webapps/mrv2-ui/web.xml

  * Access to MRv2 UI page

        http://${TOMCAT_ADDRESS}:${TOMCAT_PORT}/mrv2-ui/

# Sample images

  * [MapReduce job list](https://gist.github.com/sinchii/9572774d657a28192f97#file-mrv2ui-joblist-png)
  * [MapReduce job information](https://gist.github.com/sinchii/9572774d657a28192f97#file-mrv2ui-job-png)
  * [MapReduce task information](https://gist.github.com/sinchii/9572774d657a28192f97#file-mrv2ui-task-png)

# License

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)


# Author

Shinchi Yamashita / @\_sinchii\_