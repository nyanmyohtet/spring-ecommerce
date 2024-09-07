# Spring Boot with Oracle Database

Spring Boot with Oracle Database

ref: https://container-registry.oracle.com/ords/f?p=113:4:7017560529758:::4:P4_REPOSITORY,AI_REPOSITORY,AI_REPOSITORY_NAME,P4_REPOSITORY_NAME,P4_EULA_ID,P4_BUSINESS_AREA_ID:803,803,Oracle%20Database%20Express%20Edition,Oracle%20Database%20Express%20Edition,1,0&cs=34F4ZUpbk6kueA-wlN8oJ0ojpiff2eJ0iWh6Hupmx6ifnJ7ZHV64JzzNnz-PIRwHGJrIeeOGhs1KgUD0GO6iFSg

## Tech Stack

Oracle Database Express:21.3.0-xe

## SpringFox - Swagger

- http://localhost:8080/v2/api-docs
- http://localhost:8080/swagger-ui/

ref: https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api

## Setup Oracle DB using Docker

```shell

docker compose up -d

docker exec -it oracle-db sqlplus sys/password@XE as sysdba

SHOW PDBS;
    CON_ID CON_NAME                       OPEN MODE  RESTRICTED
---------- ------------------------------ ---------- ----------
         2 PDB$SEED                       READ ONLY  NO
         3 XEPDB1                         READ WRITE NO

ALTER SESSION SET CONTAINER = XEPDB1;

CREATE USER SPRINGBOOT IDENTIFIED BY password;

GRANT ALL PRIVILEGES TO SPRINGBOOT;
```
