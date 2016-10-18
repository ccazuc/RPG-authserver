@echo off
cd bin
"C:\Program Files\Java\jdk1.8.0_102\bin\jar.exe" cfm ../authserver.jar ../meta.txt *
cd ..
java -jar authserver.jar
pause