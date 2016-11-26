@echo off
cd bin
"C:\Program Files\Java\jdk1.8.0_102\bin\jar.exe" cfm ../authserver.jar ../meta.txt *
cd ..
<<<<<<< HEAD
java -jar -XX:+UseConcMarkSweepGC authserver.jar
=======
java -jar authserver.jar - server
>>>>>>> b0fef0fd7134c910641efb77848f3c67e91d2387
pause