-- Initial database setup for Campus Crawler
-- This script runs automatically when the MySQL container starts for the first time

-- Create the database if it doesn't exist (already done by MYSQL_DATABASE env var)
-- CREATE DATABASE IF NOT EXISTS campus_crawler;

-- Grant privileges to the campus_user
GRANT ALL PRIVILEGES ON campus_crawler.* TO 'campus_user'@'%';
FLUSH PRIVILEGES;

-- The tables will be created automatically by Hibernate with ddl-auto=update