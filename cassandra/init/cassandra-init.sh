#!/bin/bash

# Start Cassandra in background
/usr/local/bin/docker-entrypoint.sh cassandra -f &

# Wait for Cassandra to be ready
echo "Waiting for Cassandra to start..."
until cqlsh -u cassandra -p cassandra -e "describe cluster" > /dev/null 2>&1; do
  sleep 2
done

echo "Cassandra is up, creating keyspace..."

# Create keyspace
cqlsh -u cassandra -p cassandra -e "CREATE KEYSPACE IF NOT EXISTS ${CASSANDRA_KEYSPACE} WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"

echo "Keyspace created successfully!"

# Keep container running
wait