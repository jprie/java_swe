CREATE table PHOTO (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(20),
	url VARCHAR(255),
	photographer BIGINT,
	date DATE,
	location VARCHAR(20),
	PRIMARY KEY (id)
);