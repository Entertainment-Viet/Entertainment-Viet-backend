# Automation keycloak config

### Concept
This leverage the [`keycloak-config-cli`](https://github.com/adorsys/keycloak-config-cli) repo to configure
the keycloak as code and handle these configuration files in git. We use docker-compose command line
to build an image from the library base image, and its container will build the configuration file and run the importing job.

The configuration is divided into separated components inside `import` folder:
  - `roles-realm.yaml`: define realm roles to assigned to users
  - `roles-client.yaml`: define client roles (i.e. permission) corresponding to each client
  - `clients.yaml`: define set of client (i.e. application), which will use the keycloak as identity provider
  - `users.yaml`: define set of user accounts

### How to use
1. `cd keycloak`
2. `docker-compose -f docker-compose-keycloak.yaml --env-file env-files/.env.local up --build`

- For running on SQA: `docker-compose -f docker-compose-keycloak.yaml --env-file env-files/.env.sqa up --build`