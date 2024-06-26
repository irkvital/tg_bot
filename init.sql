CREATE TABLE 
    users (id bigint PRIMARY KEY, 
            username varchar(32) not null, 
            first_name varchar(64), 
            last_name varchar(64));

CREATE TABLE 
    users_location (id SERIAL PRIMARY KEY, 
                    users_id integer REFERENCES users (id), 
                    longitude double precision, 
                    latitude double precision);