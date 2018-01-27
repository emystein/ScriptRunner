-- This is a comment, it should be ignored.
// This also is a comment

DROP TABLE IF EXISTS post;

DROP TABLE IF EXISTS author;

CREATE TABLE author(id INTEGER, name TEXT, PRIMARY KEY (id));

CREATE TABLE post(id INTEGER, title TEXT, author_id INTEGER, PRIMARY KEY (id), FOREIGN KEY (author_id) REFERENCES author (id));

