CREATE TABLE author(id INTEGER, name TEXT, PRIMARY KEY (id));

CREATE TABLE post(title TEXT, author_id INTEGER, FOREIGN KEY (author_id) REFERENCES author (id));

