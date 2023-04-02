# Viet-Entertainment-backend

The repository for backend server of Viet Entertainment, here is the version for its component:
  - JDK: azul-zulu 17.0.3
  - PostgreSQL: 14
  - Postgis: 3.2
  - Keycloak 16.1.1 

The `keycloak` folder store the automation configuration for keycloak. Refer its README for more information

### Run locally
1. ./gradlew clean bootJar 
2. Add `127.0.0.1 keycloak` to /etc/hosts (or equivalent file in Windows)
3. docker-compose up -d
4. The server will be launch in localhost:8888
5. The backend will be exposed at localhost:5432 with account (postgres/postgres)
6. The keycloak server will be at keycloak:8080/auth

### Build docker image
1. docker login
2. ./gradlew clean bootJar
3. docker build -t vietentertainment2023/ve-backend:0.1.3 .
4. docker push vietentertainment2023/ve-backend:0.1.3
5. Update new version to docker-compose file

### Deploy to SQA AWS
1. Using VE-cert.pem to establish connection to AWS EC2
2. Login as `ubuntu`
3. Active root account: `sudo su`
4. Go to backend directory: `cd /home/ubuntu/Entertainment-Viet-backend`
5. Update latest code: `git pull`
6. Enter passpharse as `ve`
7. Run new version of backend: `docker-compose -f docker-compose.sqa.yaml up -d`

## Development ##

### Adding Flyway migration ### 
The Flyway migration script naming convention should be V*YYYYMMDD_HHmmss__xxx_name*.
Where:
  - YYYY: year
  - MM: month
  - DD: day of the month
  - HH: hour 
  - mm: minute 
  - ss: second
  - xxx: script type (DDL/DML)
  - name: script name

### Debug Hibernate generated SQL 
```yaml
spring:
  jpa:
    properties:
      hibernate:
        show_sql: true    
        format_sql: true   
        
logging:
  level:
    org:
      hibernate:
        type:
          descriptor:
            sql:
              BasicBinder: TRACE
```

### REST docs ###
All the endpoint is documented in the link: `http://localhost:8888/api/swagger-ui/index.html#/`