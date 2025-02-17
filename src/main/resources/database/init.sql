CREATE TABLE IF NOT EXISTS
    users (id bigint PRIMARY KEY, 
            username varchar not null, 
            first_name varchar, 
            last_name varchar,
            date_of_registration date);

CREATE TABLE IF NOT EXISTS
    locations (id BIGSERIAL PRIMARY KEY, 
                    user_id bigint REFERENCES users (id), 
                    longitude double precision, 
                    latitude double precision,
                    date_of_request date);