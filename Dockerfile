FROM tomcat:9.0.8-jre8

RUN apt-get update && apt-get install software-properties-common -y

RUN cp target/add-project-internship-2018*.war ./ROOT.war
ADD ROOT.war /usr/local/tomcat/webapps/
#RUN /usr/local/tomcat/bin/startup.sh
RUN rm -rf /usr/local/tomcat/webapps/ROOT
#RUN mv /usr/local/tomcat/webapps/serviceteam1.war /usr/local/tomcat/webapps/ROOT
#ADD server1.xml /usr/local/tomcat/conf/
#RUN rm /usr/local/tomcat/conf/server.xml

#RUN cat /usr/local/tomcat/conf/server1.xml > /usr/local/tomcat/conf/server.xml
EXPOSE 8080
CMD /usr/local/tomcat/bin/catalina.sh run