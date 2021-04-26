### Description
This is a simple repository for reproducing idle connections issues in [r2dbc-pool](http://google.com)

### Prerequisites
1. For macOS, install `watch`
```shell
brew install watch
```
2. For macOS, install `psql` - https://stackoverflow.com/a/49689589
   
3. Run local PostgreSQL server
```shell
docker-compose up
```
4. Execute init script
```shell
PGPASSWORD=password psql -U root -h localhost -p 5430 -d books -f ./src/main/resources/init.sql
```

### Steps to reproduce
1. Build project
```shell
./mvnw clean install
```
2. Run Spring Boot application
```shell
java -jar ./target/r2dbc-pool-idle-connection-issue-0.0.1-SNAPSHOT.jar
```
3. Run `jconsole` to observe JMX metrics
4. Watch db connection in PostgreSQL
```shell
watch --differences=permanent 'PGPASSWORD=password psql -U root -h 127.0.0.1 -p 5430 -d books -c "select * from (select count(*) active_conn FROM pg_stat_activity where datname = '\''books'\'' and application_name='\''r2dbc-postgresql'\'' and pid<>pg_backend_pid() and state = '\''active'\'') q1, (select count(*) idle_conn FROM pg_stat_activity where datname = '\''books'\'' and application_name='\''r2dbc-postgresql'\'' and pid<>pg_backend_pid() and state = '\''idle'\'') q2"'
```   
5. Perform `POST` request to generate some usage of database connections.
```shell
curl -X POST 'http://localhost:8080/books'
```