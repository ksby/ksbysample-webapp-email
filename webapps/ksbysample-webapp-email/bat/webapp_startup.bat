@echo on

setlocal
set JAVA_HOME=C:\Java\jdk1.8.0_45
set PATH=%JAVA_HOME%\bin;%PATH%
set WEBAPP_HOME=C:\webapps\ksbysample-webapp-email
set WEBAPP_JAR=ksbysample-webapp-email-0.0.1-SNAPSHOT.jar

cd /d %WEBAPP_HOME%
java -server ^
     -Xms1024m -Xmx1024m ^
     -XX:MaxMetaspaceSize=384m ^
     -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled ^
     -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=75 ^
     -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark ^
     -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps ^
     -Xloggc:%WEBAPP_HOME%/logs/gc.log ^
     -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M ^
     -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%WEBAPP_HOME%/logs/`date`.hprof ^
     -XX:ErrorFile=%WEBAPP_HOME%/logs/hs_err_pid_%p.log ^
     -Dsun.net.inetaddr.ttl=100 ^
     -Dcom.sun.management.jmxremote ^
     -Dcom.sun.management.jmxremote.port=7900 ^
     -Dcom.sun.management.jmxremote.ssl=false ^
     -Dcom.sun.management.jmxremote.authenticate=false ^
     -Dspring.profiles.active=product ^
     -jar lib\%WEBAPP_JAR%
