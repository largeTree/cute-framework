@echo off
set /P newVersion="Entry newVersion:"
call mvn versions:set -DnewVersion=%newVersion%-SNAPSHOT