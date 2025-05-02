#!/bin/bash

# Strip quotes from Railway-provided env variables
CLEAN_DATABASE_URL=$(echo $DATABASE_URL | sed 's/^"\(.*\)"$/\1/')
CLEAN_DATABASE_USERNAME=$(echo $DATABASE_USERNAME | sed 's/^"\(.*\)"$/\1/')
CLEAN_DATABASE_PASSWORD=$(echo $DATABASE_PASSWORD | sed 's/^"\(.*\)"$/\1/')

# Export clean values to Spring Boot
export SPRING_DATASOURCE_URL=$CLEAN_DATABASE_URL
export SPRING_DATASOURCE_USERNAME=$CLEAN_DATABASE_USERNAME
export SPRING_DATASOURCE_PASSWORD=$CLEAN_DATABASE_PASSWORD

# Start the app
exec java -jar app.jar
