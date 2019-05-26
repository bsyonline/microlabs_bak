DROP TABLE IF EXISTS test.`t_customer`;
CREATE TABLE test.`t_customer` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create database if not exists test2;
DROP TABLE IF EXISTS test2.`t_book`;
CREATE TABLE test2.`t_book` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `customer_id` int(4) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

