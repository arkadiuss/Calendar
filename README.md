# Calendar

## Database
Database is setup using Azure SQL Server. Credentials (they can be public it's just small calendar): \
server: arkadiussqlserver.database.windows.net \
port: 1433 \
username: calendar \
password: strongP4ssword

## Setup
To open project using IntelliJ just choose `Import Project` from context menu and choose gradle as a project type :)

## Run
`./gradlew build run` should be enough. \
If you want to run it through the IntelliJ choose `Add Configuration` and then add new `Gradle` with module `Calendar` and commands `build run`.