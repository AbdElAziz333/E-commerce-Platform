CREATE KEYSPACE IF NOT EXISTS notification
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};