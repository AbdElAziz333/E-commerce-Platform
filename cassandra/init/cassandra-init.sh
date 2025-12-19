#!/bin/bash

# Start Cassandra in background
docker-entrypoint.sh cassandra -f &

# Wait for Cassandra to be ready
echo "Waiting for Cassandra to start..."
until cqlsh -e "describe cluster" > /dev/null 2>&1; do
  sleep 2
done

echo "Cassandra is up, creating keyspace..."

# Create keyspace
cqlsh -e "CREATE KEYSPACE IF NOT EXISTS notification WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"

echo "Keyspace created successfully!"

# Keep container running
wait