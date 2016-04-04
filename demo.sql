
CREATE TABLE demo (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  create_time TIMESTAMP NOT NULL,
  name varchar(100) NOT NULL,
  type varchar(60) DEFAULT NULL
) CHARSET=utf8;

CREATE TABLE menu (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  code varchar(60) NOT NULL
) CHARSET=utf8;
CREATE INDEX menu_userid ON menu(user_id);

CREATE TABLE user (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  username varchar(60) NOT NULL,
  password varchar(60) NOT NULL,
  create_time TIMESTAMP NOT NULL
) CHARSET=utf8;
CREATE UNIQUE INDEX user_username ON user(username);

CREATE TABLE product (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  product_type int(4) NOT NULL,
  name varchar(60) NOT NULL,
  sku varchar(60) DEFAULT NULL,
  image varchar(100) DEFAULT NULL,
  rich_text varchar(100) DEFAULT NULL,
  create_time TIMESTAMP NOT NULL,
  update_time TIMESTAMP DEFAULT NULL
) CHARSET=utf8;
CREATE INDEX p_t_index ON product(product_type);

CREATE TABLE notice (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  notice_type int(4) NOT NULL,
  title varchar(60) DEFAULT NULL,
  content varchar(1000) DEFAULT NULL,
  rich_text varchar(100) DEFAULT NULL,
  create_time TIMESTAMP NOT NULL,
  update_time TIMESTAMP DEFAULT NULL
) CHARSET=utf8;
CREATE INDEX n_t_index ON notice(notice_type);

INSERT INTO `user` VALUES ('1', 'admin', 'admin', now());
INSERT INTO `menu` VALUES ('1', '1', 'product-menu');
INSERT INTO `menu` VALUES ('2', '1', 'product-type1');
INSERT INTO `menu` VALUES ('3', '1', 'product-type2');
INSERT INTO `menu` VALUES ('4', '1', 'product-type3');
INSERT INTO `menu` VALUES ('5', '1', 'product-type4');
INSERT INTO `menu` VALUES ('6', '1', 'notice-menu');
INSERT INTO `menu` VALUES ('7', '1', 'notice-type1');
INSERT INTO `menu` VALUES ('8', '1', 'notice-type2');
INSERT INTO `menu` VALUES ('9', '1', 'notice-type3');

