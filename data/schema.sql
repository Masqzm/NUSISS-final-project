DROP DATABASE IF EXISTS jiomakan;
CREATE DATABASE jiomakan;
USE jiomakan;

CREATE TABLE users (
	user_id char(8) NOT NULL,
    email varchar(255) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,		
    oauth2_id varchar(255),
    created_at timestamp DEFAULT NOW(),
    
    primary KEY(id)
);


CREATE TABLE user_profiles (
	user_id char(8) NOT NULL,
    username varchar(255) NOT NULL,		
	profile_pic blob,
	karma int DEFAULT 0,
	bio text,
	
    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

# table to track all rsvps (jios) of a user
CREATE TABLE user_rsvps (
	user_id char(8) NOT NULL,
	jio_id char(8) NOT NULL,
    
    PRIMARY KEY(user_id, jio_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);