# Viet-Entertainment-backend

The repository for backend server of Viet Entertainment, here is the version for its component:
  - JDK: azul-zulu 17.0.3
  - PostgreSQL: 14
  - Postgis: 3.2
  - Keycloak 16.1.1 

The `keycloak` folder store the automation configuration for keycloak. Refer its README for more information

### Run locally
1. ./gradlew clean bootJar 
2. docker-compose up -d
3. The server will be launch in localhost:8080
4. The backend will be exposed at localhost:5432 with account (postgres/postgres)
5. The keycloak server will be at localhost:18080/auth

### Deploy to SQA heroku 
1. Login to heroku: `heroku login`
2. Connect to app on heroku: ` heroku git:remote boiling-shore-81192`
3. Commit all changes: `git commit -am "message"`
4. Deploy changes to SQA: `git push heroku main`


#### Some helpful command with heroku
* Access to database: `heroku pg:psql`
* Check status of app: `heroku ps`
* Restart the app: `heroku restart`
* Check app environment variable: `heroku config` 
* Check app logs: `heroku logs --tail`
* Clear database: `heroku pg:reset DATABASE --confirm <app_name>`

### Access to backend database through db client
1. Go to Heroku App dashboard
2. Click on the Heroku Postgres Addon
3. Go To Settings
4. Click on Credentials (this information can change dynamically, make sure check the data again before connect)
5. Copy database information for connection


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
All the endpoint is documented in the link: `http://localhost:8080/api/swagger-ui/index.html#/`