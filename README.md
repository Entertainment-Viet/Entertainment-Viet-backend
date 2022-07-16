# Viet-Entertainment-backend

The repository for backend server of Viet Entertainment using azul-jdk 17.0.3 and connecting with PostgreSQL 14 database

### Run locally
1. docker-compose up -d
2. The server will be launch in localhost:8080
3. The backend will be exposed at localhost:5432 with account (postgres/postgres)

### Deploy to SQA heroku 
1. Login to heroku: `heroku login`
2. Commit all changes: `git commit -am "message"`
3. Build app: `gradle stage`
4. Deploy changes to SQA: `git push heroku main`


#### Some helpful command with heroku
* Access to database: `heroku pg:psql`
* Check status of app: `heroku ps`
* Restart the app: `heroku restart`
* Check app environment variable: `heroku config` 
* Check app logs: `heroku logs --tail`

### Access to backend database through db client
1. Go to Heroku App dashboard
2. Click on the Heroku Postgres Addon
3. Go To Settings
4. Click on Credentials (this information can change dynamically, make sure check the data again before connect)
5. Copy database information for connection
