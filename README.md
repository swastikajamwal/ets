ets - Technologies
==================

1) UI - SockJS, SockJS client
2) Server - Spring 
3) orm - http://docs.spring.io/spring/docs/4.0.6.RELEASE/spring-framework-reference/htmlsingle/#orm-introduction
4) mvc - http://docs.spring.io/docs/Spring-MVC-step-by-step/part5.html
5) bootstraping web apps - http://www.baeldung.com/2011/10/20/bootstraping-a-web-application-with-spring-3-1-and-java-based-configuration-part-1/

http://spring.io/guides/gs/messaging-stomp-websocket/
http://assets.spring.io/wp/WebSocketBlogPost.html


Tools/Technologies/Methodologies
---------------------------------
1) Backend - Java 7, Spring 4+, Spring boot, Spring Integration, Hibernate, Redis or Coherence or Hazelcast, Oracle DB, Maven
2) Frontend (Client/Server) -  HTML. Javascript - AngularJS, BootstrapUI, Websockets/SockJS, STOMP, Java 7, Spring4+, Spring boot, Spring MVC, ActiveMQ or caching (Redis/Coherence/Hazelcast)
2) IDE - Eclipse/IntelliJ
2) Testing - JUnit/Mockito/HSQLDB/Selenium
3) Code Coverage - Jacoco (Sonar)
4) Code Formatting/Style - 
5) Performance tests - JMeter
6) JVM Monitoring/Profiling - JConsole


TODO
----
1) Add chart
2) Change portfolio to fx trades
3) Add spring db classes/hbm etc
4) 

MYSQL
=====
1) Start: mysqld --console
2) Stop: mysqladmon -u root shutdown
3) Connect via command line client window: mysql -u root

show databases;
use ets;
show tables;

CREATE TABLE `ets`.`user` (
  `id` VARCHAR(45) NOT NULL,
  `user_name` VARCHAR(10) NULL,
  `password` VARCHAR(10) NULL,
  `first_name` VARCHAR(10) NULL,
  `last_name` VARCHAR(10) NULL,
  `email` VARCHAR(45) NULL,
  `role` VARCHAR(10) NULL,
  PRIMARY KEY (`id`));
  
CREATE TABLE `ets`.`portfolio` (
  `id` VARCHAR(45) NOT NULL,
  `user_id` VARCHAR(10) NULL,
  `total_value` VARCHAR(10) NULL,
  `pandl` VARCHAR(10) NULL,
  PRIMARY KEY (`id`));
  
CREATE TABLE `ets`.`trade` (
  `id` VARCHAR(45) NOT NULL,
  `symbol` VARCHAR(10) NULL,
  `size` VARCHAR(10) NULL,
  `trade_price` VARCHAR(10) NULL,
  `trade_date` TIMESTAMP NULL,
  `user_id` VARCHAR(45) NULL,
  `side` VARCHAR(5) NULL,
  PRIMARY KEY (`id`));
  
CREATE TABLE `ets`.`orders` (
  `id` VARCHAR(45) NOT NULL,
  `symbol` VARCHAR(5) NULL,
  `size` VARCHAR(10) NULL,
  `order_price` VARCHAR(10) NULL,
  `status` VARCHAR(10) NULL,
  `user_id` VARCHAR(45) NULL,
  `side` VARCHAR(10) NULL,
  PRIMARY KEY (`id`));
  
ALTER TABLE `ets`.`trade` 
ADD COLUMN `status` VARCHAR(10) NULL AFTER `side`;
  

insert into user values ('1', 'trader1', 'trd123', 'trader', '1', 'trader1@email.com', 'user');
insert into user values ('2', 'admin', 'admin', 'admin', '', 'admin@email.com', 'admin');

insert into trade values ('1001', 'GBPUSD', '1.0', '1.68341', '2014.09.02 09:23:00', 'trader1', 'buy', 'position');
insert into trade values ('1002', 'EURGBP', '1.0', '0.79977', '2014.09.09 11:23:07', 'trader1', 'sell', 'position');

insert into portfolio values ('trader1', 'trader1', '10000.000', '0.0');

commit;


