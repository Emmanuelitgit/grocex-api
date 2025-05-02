#!/bin/bash

# Strip surrounding quotes from Railway env vars
CLEAN_DATABASE_URL=$(echo $DATABASE_URL | sed 's/^"\(.*\)"$/\1/')
CLEAN_DATABASE_USERNAME=$(echo $DATABASE_USERNAME | sed 's/^"\(.*\)"$/\1/')
CLEAN_DATABASE_PASSWORD=$(echo $DATABASE_PASSWORD | sed 's/^"\(.*\)"$/\1/')

# Optional logging for sanity check (do not log password!)
echo "Using DB URL: $CLEAN_DATABASE_URL"
echo "Using DB Username: $CLEAN_DATABASE_USERNAME"

# Run Spring Boot app with cleaned DB config
exec java \
  -Dspring.datasource.url=$CLEAN_DATABASE_URL \
  -Dspring.datasource.username=$CLEAN_DATABASE_USERNAME \
  -Dspring.datasource.password=$CLEAN_DATABASE_PASSWORD \
  -jar app.jar