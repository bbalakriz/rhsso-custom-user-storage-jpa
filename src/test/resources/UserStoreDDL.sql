create database keycloak;

use keycloak;

DELIMITER //

create table UserStore (
        id bigint not null auto_increment,
        userName varchar(255),
        phone varchar(20),
        email varchar(100),
        primary key (id)
    ) ENGINE=InnoDB; //
    
create table UserPass (
        userName varchar(255),
        passPhrase varchar(20)
    ) ENGINE=InnoDB; //

CREATE PROCEDURE GetUserByName(user_name varchar(20))
BEGIN
   select id,userName,phone,email from UserStore where userName = user_name;
   END //

DELIMITER ;
    
insert into UserStore (userName, phone, email) values('Bala', '3458738735', 'bala@test.com');
insert into UserPass (userName, passPhrase) values('Bala', 'Bala');
insert into UserStore (userName, phone, email) values('Krishnan', '538764874', 'kris@test.com');
insert into UserPass (userName, passPhrase)  values('Krishnan', 'Krishnan');