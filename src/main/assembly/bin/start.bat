@echo off
echo %~f0
set profile=test
set jar_path=%~dp0..\mock-service.jar
set class_path=%~dp0..\config
set plugins_path=%~dp0..\extra
set log_path=%~dp0..\logs\
echo classpath: %class_path%
echo executable_path: %jar_path%
echo log_path: %log_path%
java -Xbootclasspath/a:%class_path% -Xbootclasspath/a:%plugins_path% -Dspring.profiles.active=%profile% -DlogPath=%log_path% -jar %jar_path%