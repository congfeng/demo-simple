
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
  qrcode varchar(100) DEFAULT NULL,
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

CREATE TABLE msg (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  title varchar(60) DEFAULT NULL,
  content varchar(1000) DEFAULT NULL,
  user_name varchar(100) DEFAULT NULL,
  user_email varchar(100) DEFAULT NULL,
  create_time TIMESTAMP NOT NULL,
  update_time TIMESTAMP DEFAULT NULL,
  send_status int(4) NOT NULL,
  reply_status int(4) NOT NULL
) CHARSET=utf8;
CREATE INDEX msg_s_index ON msg(send_status);
CREATE INDEX msg_r_index ON msg(reply_status);

CREATE TABLE msg_receiver (
  id int(11) PRIMARY KEY AUTO_INCREMENT,
  address varchar(100) NOT NULL,
  create_time TIMESTAMP NOT NULL,
  update_time TIMESTAMP DEFAULT NULL
) CHARSET=utf8;

INSERT INTO PUBLIC.USER(ID, USERNAME, PASSWORD, CREATE_TIME) VALUES 
(1, 'admin', 'admin', now());

INSERT INTO PUBLIC.MSG_RECEIVER(ID, ADDRESS, CREATE_TIME, UPDATE_TIME) VALUES 
(1, '3024992@qq.com', now(), NULL);

INSERT INTO PUBLIC.MENU(ID, USER_ID, CODE) VALUES 
(1, 1, 'product-menu'), 
(2, 1, 'product-type1'), 
(3, 1, 'product-type2'), 
(4, 1, 'product-type3'), 
(5, 1, 'product-type4'), 
(6, 1, 'notice-menu'), 
(7, 1, 'notice-type1'), 
(8, 1, 'notice-type2'), 
(9, 1, 'notice-type3'), 
(10, 1, 'msg-menu'), 
(11, 1, 'msg-receiver'), 
(12, 1, 'msg-list');

INSERT INTO PUBLIC.PRODUCT(ID, PRODUCT_TYPE, NAME, SKU, IMAGE, RICH_TEXT, CREATE_TIME, UPDATE_TIME) VALUES
(1, 1, STRINGDECODE('\u76ae\u80a4\u5fc3\u7682\u7682'), '', 'product/image/2016/04/04/4011a08c-f1d3-499e-baf9-4f7955ac1a6e.jpg', 'product/richText/2016/04/09/368f1c99-857c-492a-bdab-383a6048d48e.html', now(), now()),
(2, 1, STRINGDECODE('\u4e07\u7530\u9176/\u9897\u7c92\u7c7b\u578b'), '', 'product/image/2016/04/04/cce3a96f-1634-46f6-a48a-e5caeb6a5bf8.jpg', NULL, now(), now()),
(3, 1, STRINGDECODE('\u4e07\u7530\u9176/\u5206\u5305\u7c7b\u578b'), '', 'product/image/2016/04/04/b096e6ed-f71c-42c7-9f5b-9e44914bd216.jpg', NULL, now(), now()),
(4, 1, STRINGDECODE('\u4e07\u7530\u9176/\u74f6\u7c7b\u578b'), '', 'product/image/2016/04/04/7edfb942-834f-496f-9ac5-06278618c6fb.jpg', NULL, now(), now()),
(5, 1, STRINGDECODE('\u836f\u81b3\u5496\u55b1'), '', 'product/image/2016/04/04/a28ad2b2-2950-4d2a-b3ac-a097fcd01fbf.jpg', NULL, now(), now()),
(6, 1, STRINGDECODE('\u836f\u81b3\u6c34\u82b9\u5496\u55b1'), '', 'product/image/2016/04/04/18a91ac1-a4d7-4228-9b9d-d62b7181d7fb.jpg', NULL, now(), now()),
(7, 1, STRINGDECODE('\u836f\u81b3\u67da\u5b50\u5496\u55b1'), '', 'product/image/2016/04/04/66e57d33-be63-4d44-b339-196a072cdb05.jpg', NULL, now(), now()),
(8, 1, STRINGDECODE('\u6c99\u68d8\u6709\u673a'), '', 'product/image/2016/04/04/6486c616-dbd8-4c1e-886c-5813469c4ca7.jpg', NULL, now(), now()),
(9, 1, STRINGDECODE('\u6c99\u68d8\u6e29\u548c'), '', 'product/image/2016/04/04/2d743209-5da6-476b-a2ac-68ad8c84edb0.jpg', NULL, now(), now()),
(10, 1, STRINGDECODE('\u5929\u516d\u8c37'), '', 'product/image/2016/04/04/63ea5f79-cea6-4f24-9731-fdd06d03accf.jpg', NULL, now(), now()),
(11, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u8d85\u7ea7EX'), '', 'product/image/2016/04/04/04f67b43-b620-4afa-a77d-6b93db717551.jpg', NULL, now(), now()),
(12, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u4e09\u5e74\u6210\u719f35ml'), '', 'product/image/2016/04/04/076c9928-9a90-476b-9fad-f0c3bc9c60cd.jpg', NULL, now(), now()),
(13, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u4e09\u5e74\u6210\u719f60ml'), '', 'product/image/2016/04/04/be3925f8-222a-4494-bc72-5795026a1bec.jpg', NULL, now(), now()),
(14, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u4e09\u5e74\u6210\u719f120ml'), '', 'product/image/2016/04/04/80af9362-1bbf-4423-b383-00eacbbef96f.jpg', NULL, now(), now()),
(15, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u4e94\u5e74\u6210\u719f35ml'), '', 'product/image/2016/04/04/69f110d7-dcfc-46cb-bdcb-bd607c2d83c6.jpg', NULL, now(), now()),
(16, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u4e94\u5e74\u6210\u719f60ml'), '', 'product/image/2016/04/04/0e316b3b-c5d9-4c5b-8432-d901f560704f.jpg', NULL, now(), now()),
(17, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u4e94\u5e74\u6210\u719f120ml'), '', 'product/image/2016/04/04/e2c64b3b-c91f-40ef-8fd0-884ab9cdcaf0.jpg', NULL, now(), now());
INSERT INTO PUBLIC.PRODUCT(ID, PRODUCT_TYPE, NAME, SKU, IMAGE, RICH_TEXT, CREATE_TIME, UPDATE_TIME) VALUES
(18, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u5341\u5e74\u6210\u719f35ml'), '', 'product/image/2016/04/04/14215da1-160d-4101-a275-017fdf621378.jpg', NULL, now(), now()),
(19, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u5341\u5e74\u6210\u719f60ml'), '', 'product/image/2016/04/04/75b2397e-d27d-4e24-ace0-2ace8d5176e3.jpg', NULL, now(), now()),
(20, 1, STRINGDECODE('\u8702\u80f6\u3010\u871c\u8702\u7684\u6069\u60e0\u3011\u5341\u5e74\u6210\u719f120ml'), '', 'product/image/2016/04/04/e7295406-e464-4d69-a7d4-8a8b87653a47.jpg', NULL, now(), now()),
(21, 1, STRINGDECODE('\u8702\u80f6\u55b7\u96fe\u5242\uff01'), '', 'product/image/2016/04/04/35f8ab54-c8ce-4978-85a7-1696b841cc59.jpg', NULL, now(), now()),
(22, 1, STRINGDECODE('\u8702\u80f6\u5589\u5499\u7cd6/\u9178\u6a58\u7cbe\u534e\u8fdb\u5165'), '', 'product/image/2016/04/04/4ec12fca-63b2-47d7-9709-9c57bfd73f9f.jpg', NULL, now(), now()),
(23, 1, STRINGDECODE('\u751f\u59dc\u8702\u80f6\u7684\u7cd6'), '', 'product/image/2016/04/04/5733b278-54a9-41af-8e41-daebaed1573c.jpg', NULL, now(), now()),
(24, 1, STRINGDECODE('\u9884\u8b66\u5bdf\u7684\u5589\u5499\u7cd6/\u6885\u8089\u7cbe\u534e\u8fdb\u5165'), '', 'product/image/2016/04/04/90ffc941-c3c3-43ea-bedd-e143b311ccda.jpg', NULL, now(), now()),
(25, 1, STRINGDECODE('\u73ab\u7470\u7ea2'), '', 'product/image/2016/04/04/193e0d66-d74e-4078-850f-0822482ada8b.jpg', NULL, now(), now()),
(26, 1, STRINGDECODE('\u559c\u754c\u5cf6\u751f\u7802\u7cd6'), '', 'product/image/2016/04/04/e923b9ad-2454-4d8f-a04b-db0d0d83f4b8.jpg', NULL, now(), now()),
(27, 1, STRINGDECODE('\u559c\u754c\u5cf6\u6252\u624b\u829d\u9ebb'), '', 'product/image/2016/04/04/9517ec15-1554-4857-bc38-efd80a9a5e3b.jpg', NULL, now(), now()),
(28, 1, STRINGDECODE('\u7eff\u8272\u30b9\u30e0\u30fc\u30b8\u30fc'), '', 'product/image/2016/04/04/aaf7140e-32bd-49e2-a833-270a6bbeff80.jpg', NULL, now(), now()),
(29, 1, STRINGDECODE('\u30d3\u30d3\u30c3\u30c8\u80f6\u539f\u86cb\u767d'), '', 'product/image/2016/04/04/513be067-71ef-4711-8fea-fed22d3ee341.jpg', NULL, now(), now()),
(30, 1, STRINGDECODE('\u6211\u7684\u7fbd\u8863\u7518\u84dd'), '', 'product/image/2016/04/04/8b467b29-89c6-4798-bb83-4e19ea0f9d63.jpg', NULL, now(), now()),
(31, 1, STRINGDECODE('\u84dd\u8393\u9ed1\u918b\u6817\u9ed1\u918b'), '', 'product/image/2016/04/04/c30c2851-4149-4713-a6b7-bf857aaf0ec6.jpg', NULL, now(), now()),
(32, 1, STRINGDECODE('\u7f8e\u5473\u7684\u918b\u86cb'), '', 'product/image/2016/04/04/3a9b935d-0e1b-4d04-b59b-132732ce60d6.jpg', NULL, now(), now()),
(33, 1, STRINGDECODE('\u59dc\u9ec4\u529b\u91cf/ 10\u672c'), '', NULL, NULL, now(), now()),
(34, 1, STRINGDECODE('\u8461\u8404\u7cd6\u80fa'), '', 'product/image/2016/04/04/d3741605-167b-45a0-827e-60d910ed83e8.jpg', NULL, now(), now()),
(35, 1, STRINGDECODE('\u84dd\u8393\u80fd30 / 5'), '', 'product/image/2016/04/04/10a4ba77-5aff-40b1-a735-68d0587cd1d1.jpg', NULL, now(), now());

INSERT INTO PUBLIC.NOTICE(ID, NOTICE_TYPE, TITLE, CONTENT, RICH_TEXT, CREATE_TIME, UPDATE_TIME) VALUES
(1, 1, STRINGDECODE('4\u6708\u7684\u4e34\u65f6\u4f11\u4e1a\u65e5'), '', 'richText/2016/04/09/4ae225d9-097c-44a6-a832-dd34d3c6ba92.html', now(), now()),
(2, 1, STRINGDECODE('\u9752\u6c41\u4f18\u60e0\u52b5'), '', 'richText/2016/04/09/10a6f304-3fd9-4cb8-bdda-b59fe97fb4e4.html', now(), now()),
(3, 1, STRINGDECODE('\u30d3\u30d3\u30c3\u30c8\u80f6\u539f\u86cb\u767d'), '', 'richText/2016/04/09/1601d716-3e56-48dd-a7be-699824419de1.html', now(), now()),
(4, 1, STRINGDECODE('\u901a\u544a\uff08\u6a21\u677f\uff09'), '', 'richText/2016/04/09/9a70beff-30f2-45c5-9a95-601e0e2a7f7b.html', now(), now()),
(5, 1, STRINGDECODE('\u660e\u5929\u653e\u5047\uff08\u6d82\u9e26\uff09'), '', 'richText/2016/04/09/02385ded-9cfc-43a9-a149-e895852cc089.html', now(), now());
