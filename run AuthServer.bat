@echo off
cd bin
jar.exe cfm ../authserver.jar ../meta.txt *
cd ..
java -jar -server -XX:+UseConcMarkSweepGC authserver.jar
pause