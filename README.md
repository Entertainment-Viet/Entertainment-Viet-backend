# Viet-Entertainment-backend

The repository for backend server of Viet Entertainment, here is the version for its component:
  - JDK: azul-zulu 17.0.3
  - PostgreSQL: 14
  - Postgis: 3.2
  - Keycloak 16.1.1 

### Run locally
1. ./gradlew clean bootJar 
2. docker-compose up -d
3. The server will be launch in localhost:8080
4. The backend will be exposed at localhost:5432 with account (postgres/postgres)

### Deploy to SQA heroku 
1. Login to heroku: `heroku login`
2. Commit all changes: `git commit -am "message"`
3. Deploy changes to SQA: `git push heroku main`


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
