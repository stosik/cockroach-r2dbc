#!/bin/bash

docker exec -it roach1 ./cockroach init --insecure
docker-compose exec roach1 /cockroach/cockroach sql --insecure --execute="CREATE DATABASE test;"
docker-compose exec roach1 /cockroach/cockroach sql --insecure --execute="CREATE USER test_user;"
