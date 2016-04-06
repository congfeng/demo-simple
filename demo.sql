
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
(1, 'admin', 'admin', TIMESTAMP '2016-04-03 15:00:49.679');

INSERT INTO PUBLIC.MSG_RECEIVER(ID, ADDRESS, CREATE_TIME, UPDATE_TIME) VALUES 
(1, '3024992@qq.com', TIMESTAMP '2016-04-06 10:23:24.943', NULL);

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
(1, 1, STRINGDECODE('\u808c\u3054\u3053\u308d\u305b\u3063\u3051\u3093'), '', 'product/image/2016/04/04/4011a08c-f1d3-499e-baf9-4f7955ac1a6e.jpg', 'product/richText/2016/04/04/bceb745d-3650-47a7-8d5f-5d75c0c13c6a.html', TIMESTAMP '2016-04-04 18:12:02.112', TIMESTAMP '2016-04-04 21:32:26.662'), 
(2, 1, STRINGDECODE('\u4e07\u7530\u9175\u7d20/\u7c92\u30bf\u30a4\u30d7'), '', 'product/image/2016/04/04/cce3a96f-1634-46f6-a48a-e5caeb6a5bf8.jpg', NULL, TIMESTAMP '2016-04-04 18:12:39.919', NULL), 
(3, 1, STRINGDECODE('\u4e07\u7530\u9175\u7d20/\u5206\u5305\u30bf\u30a4\u30d7'), '', 'product/image/2016/04/04/b096e6ed-f71c-42c7-9f5b-9e44914bd216.jpg', NULL, TIMESTAMP '2016-04-04 18:13:22.912', NULL), 
(4, 1, STRINGDECODE('\u4e07\u7530\u9175\u7d20/\u74f6\u30bf\u30a4\u30d7'), '', 'product/image/2016/04/04/7edfb942-834f-496f-9ac5-06278618c6fb.jpg', NULL, TIMESTAMP '2016-04-04 18:14:00.662', NULL), 
(5, 1, STRINGDECODE('\u85ac\u81b3\u30ab\u30ec\u30fc'), '', 'product/image/2016/04/04/a28ad2b2-2950-4d2a-b3ac-a097fcd01fbf.jpg', NULL, TIMESTAMP '2016-04-04 18:14:46.368', NULL), 
(6, 1, STRINGDECODE('\u85ac\u81b3\u30af\u30ec\u30bd\u30f3\u30ab\u30ec\u30fc'), '', 'product/image/2016/04/04/18a91ac1-a4d7-4228-9b9d-d62b7181d7fb.jpg', NULL, TIMESTAMP '2016-04-04 18:15:25.419', NULL), 
(7, 1, STRINGDECODE('\u85ac\u81b3\u3086\u305a\u30ab\u30ec\u30fc'), '', 'product/image/2016/04/04/66e57d33-be63-4d44-b339-196a072cdb05.jpg', NULL, TIMESTAMP '2016-04-04 18:15:51.443', NULL), 
(8, 1, STRINGDECODE('\u30b5\u30b8\u30fc\u30aa\u30fc\u30ac\u30cb\u30c3\u30af'), '', 'product/image/2016/04/04/6486c616-dbd8-4c1e-886c-5813469c4ca7.jpg', NULL, TIMESTAMP '2016-04-04 18:16:22.23', NULL), 
(9, 1, STRINGDECODE('\u30b5\u30b8\u30fc\u30de\u30a4\u30eb\u30c9'), '', 'product/image/2016/04/04/2d743209-5da6-476b-a2ac-68ad8c84edb0.jpg', NULL, TIMESTAMP '2016-04-04 18:16:55.074', NULL), 
(10, 1, STRINGDECODE('\u5929\u516d\u7a40'), '', 'product/image/2016/04/04/63ea5f79-cea6-4f24-9731-fdd06d03accf.jpg', '', TIMESTAMP '2016-04-04 18:26:43.149', NULL), 
(11, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u30b9\u30fc\u30d1\u30fcEX'), '', 'product/image/2016/04/04/04f67b43-b620-4afa-a77d-6b93db717551.jpg', NULL, TIMESTAMP '2016-04-04 18:27:04.08', NULL), 
(12, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u4e09\u5e74\u719f\u621035ml'), '', 'product/image/2016/04/04/076c9928-9a90-476b-9fad-f0c3bc9c60cd.jpg', NULL, TIMESTAMP '2016-04-04 18:27:23.428', NULL), 
(13, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u4e09\u5e74\u719f\u621060ml'), '', 'product/image/2016/04/04/be3925f8-222a-4494-bc72-5795026a1bec.jpg', '', TIMESTAMP '2016-04-04 18:27:46.083', NULL), 
(14, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u4e09\u5e74\u719f\u6210120ml'), '', 'product/image/2016/04/04/80af9362-1bbf-4423-b383-00eacbbef96f.jpg', NULL, TIMESTAMP '2016-04-04 18:28:04.143', NULL), 
(15, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u4e94\u5e74\u719f\u621035ml'), '', 'product/image/2016/04/04/69f110d7-dcfc-46cb-bdcb-bd607c2d83c6.jpg', NULL, TIMESTAMP '2016-04-04 18:28:28.309', NULL), 
(16, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u4e94\u5e74\u719f\u621060ml'), '', 'product/image/2016/04/04/0e316b3b-c5d9-4c5b-8432-d901f560704f.jpg', NULL, TIMESTAMP '2016-04-04 18:28:47.012', NULL), 
(17, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u4e94\u5e74\u719f\u6210120ml'), '', 'product/image/2016/04/04/e2c64b3b-c91f-40ef-8fd0-884ab9cdcaf0.jpg', NULL, TIMESTAMP '2016-04-04 18:29:01.879', NULL), 
(18, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u5341\u5e74\u719f\u621035ml'), '', 'product/image/2016/04/04/14215da1-160d-4101-a275-017fdf621378.jpg', NULL, TIMESTAMP '2016-04-04 18:29:20.964', NULL), 
(19, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u5341\u5e74\u719f\u621060ml'), '', 'product/image/2016/04/04/75b2397e-d27d-4e24-ace0-2ace8d5176e3.jpg', NULL, TIMESTAMP '2016-04-04 18:31:42.166', NULL);
INSERT INTO PUBLIC.PRODUCT(ID, PRODUCT_TYPE, NAME, SKU, IMAGE, RICH_TEXT, CREATE_TIME, UPDATE_TIME) VALUES 
(20, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u3010\u8702\u306e\u6075\u307f\u3011\u5341\u5e74\u719f\u6210120ml'), '', 'product/image/2016/04/04/e7295406-e464-4d69-a7d4-8a8b87653a47.jpg', NULL, TIMESTAMP '2016-04-04 18:31:58.676', NULL), 
(21, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u30b9\u30d7\u30ec\u30fc\u3000\u5589\u3059\u3063\u304d\u308a\uff01'), '', 'product/image/2016/04/04/35f8ab54-c8ce-4978-85a7-1696b841cc59.jpg', NULL, TIMESTAMP '2016-04-04 18:32:17.393', NULL), 
(22, 1, STRINGDECODE('\u30d7\u30ed\u30dd\u30ea\u30b9\u306e\u3069\u98f4/\u30b9\u30c0\u30c1\u30a8\u30ad\u30b9\u5165\u308a'), '', 'product/image/2016/04/04/4ec12fca-63b2-47d7-9709-9c57bfd73f9f.jpg', NULL, TIMESTAMP '2016-04-04 18:32:33.863', NULL), 
(23, 1, STRINGDECODE('\u3057\u3087\u3046\u304c\u30d7\u30ed\u30dd\u30ea\u30b9\u306e\u3069\u98f4'), '', 'product/image/2016/04/04/5733b278-54a9-41af-8e41-daebaed1573c.jpg', NULL, TIMESTAMP '2016-04-04 18:32:47.742', NULL), 
(24, 1, STRINGDECODE('\u30d7\u30ea\u30dd\u30ea\u30b9\u306e\u3069\u98f4/\u6885\u8089\u30a8\u30ad\u30b9\u5165\u308a'), '', 'product/image/2016/04/04/90ffc941-c3c3-43ea-bedd-e143b311ccda.jpg', NULL, TIMESTAMP '2016-04-04 18:33:04.516', NULL), 
(25, 1, STRINGDECODE('\u30ed\u30fc\u30ba\u30bd\u30eb\u30c8'), '', 'product/image/2016/04/04/193e0d66-d74e-4078-850f-0822482ada8b.jpg', NULL, TIMESTAMP '2016-04-04 18:33:21.206', NULL), 
(26, 1, STRINGDECODE('\u559c\u754c\u5cf6\u751f\u3056\u3089\u3081'), '', 'product/image/2016/04/04/e923b9ad-2454-4d8f-a04b-db0d0d83f4b8.jpg', NULL, TIMESTAMP '2016-04-04 18:33:39.269', NULL), 
(27, 1, STRINGDECODE('\u559c\u754c\u5cf6\u3059\u308a\u3054\u307e'), '', 'product/image/2016/04/04/9517ec15-1554-4857-bc38-efd80a9a5e3b.jpg', NULL, TIMESTAMP '2016-04-04 18:33:54.225', NULL), 
(28, 1, STRINGDECODE('\u30b0\u30ea\u30fc\u30f3\u30b9\u30e0\u30fc\u30b8\u30fc'), '', 'product/image/2016/04/04/aaf7140e-32bd-49e2-a833-270a6bbeff80.jpg', NULL, TIMESTAMP '2016-04-04 18:35:53.597', NULL), 
(29, 1, STRINGDECODE('\u30d3\u30d3\u30c3\u30c8\u30b3\u30e9\u30fc\u30b2\u30f3'), '', 'product/image/2016/04/04/513be067-71ef-4711-8fea-fed22d3ee341.jpg', NULL, TIMESTAMP '2016-04-04 18:36:10.047', NULL), 
(30, 1, STRINGDECODE('\u30de\u30a4\u30b1\u30fc\u30eb'), '', 'product/image/2016/04/04/8b467b29-89c6-4798-bb83-4e19ea0f9d63.jpg', NULL, TIMESTAMP '2016-04-04 18:36:28.24', NULL), 
(31, 1, STRINGDECODE('\u30d6\u30eb\u30fc\u30d9\u30ea\u30fc\u30ab\u30b7\u30b9\u9ed2\u9162'), '', 'product/image/2016/04/04/c30c2851-4149-4713-a6b7-bf857aaf0ec6.jpg', NULL, TIMESTAMP '2016-04-04 18:36:42.932', NULL), 
(32, 1, STRINGDECODE('\u304a\u3044\u3057\u3044\u9162\u5375'), '', 'product/image/2016/04/04/3a9b935d-0e1b-4d04-b59b-132732ce60d6.jpg', NULL, TIMESTAMP '2016-04-04 18:36:58.649', NULL), 
(33, 1, STRINGDECODE('\u30a6\u30b3\u30f3\u30d1\u30ef\u30fc/10\u672c\u5165\u308a'), '', NULL, NULL, TIMESTAMP '2016-04-04 18:37:10.752', NULL), 
(34, 1, STRINGDECODE('\u30b0\u30eb\u30b3\u30b5\u30df\u30f3\uff06\u30b3\u30f3\u30c9\u30ed\u30a4\u30c1\u30f3'), '', 'product/image/2016/04/04/d3741605-167b-45a0-827e-60d910ed83e8.jpg', NULL, TIMESTAMP '2016-04-04 18:37:25.885', NULL), 
(35, 1, STRINGDECODE('\u30d6\u30eb\u30fc\u30d9\u30ea\u30fc\u30d1\u30ef\u30fc30/5\u672c\u5165\u308a'), '', 'product/image/2016/04/04/10a4ba77-5aff-40b1-a735-68d0587cd1d1.jpg', NULL, TIMESTAMP '2016-04-04 18:37:39.95', NULL);

INSERT INTO PUBLIC.NOTICE(ID, NOTICE_TYPE, TITLE, CONTENT, RICH_TEXT, CREATE_TIME, UPDATE_TIME) VALUES 
(1, 1, STRINGDECODE('4\u6708\u306e\u81e8\u6642\u4f11\u696d\u65e5'), '', 'richText/2016/04/04/65a5a86b-20ff-49c1-bdd5-d480aff90423.html', TIMESTAMP '2016-04-04 19:52:44.589', NULL), 
(2, 1, STRINGDECODE('\u9752\u6c41\u56de\u6570\u5238'), '', 'richText/2016/04/04/463d0441-f816-4996-918d-f683a3a75978.html', TIMESTAMP '2016-04-04 19:57:09.409', NULL), 
(3, 1, STRINGDECODE('\u30d3\u30d3\u30c3\u30c8\u3000\u30b3\u30e9\u30fc\u30b2\u30f3'), '', 'richText/2016/04/04/bebfe19e-214b-4e92-8ec3-ec15970da2e7.html', TIMESTAMP '2016-04-04 19:59:59.169', NULL), 
(4, 1, STRINGDECODE('\u30cb\u30e5\u30fc\u30b93\u30cb\u30e5\u30fc\u30b93\u30cb\u30e5\u30fc\u30b93\u30cb\u30e5\u30fc\u30b93\u30cb\u30e5\u30fc\u30b93\u30cb\u30e5\u30fc\u30b93'), '', 'richText/2016/04/04/9ff1f88f-1622-4385-9335-06cd30d2460e.html', TIMESTAMP '2016-04-04 20:00:25.463', NULL), 
(5, 1, STRINGDECODE('\u30cb\u30e5\u30fc\u30b92'), '', 'richText/2016/04/04/f93bf21e-ae38-4352-82cb-acf5356a530a.html', TIMESTAMP '2016-04-04 20:00:50.894', NULL), 
(6, 1, STRINGDECODE('\u30cb\u30e5\u30fc\u30b91'), '', 'richText/2016/04/04/5126a066-faf1-40d3-a0e7-0f05a5a54ec1.html', TIMESTAMP '2016-04-04 20:03:09.822', NULL), 
(7, 1, 'Hello world!', '', 'richText/2016/04/04/9f7b0493-1d15-4fdf-8373-72123241e5ae.html', TIMESTAMP '2016-04-04 20:03:50.579', NULL), 
(8, 1, STRINGDECODE('\u7684\u9876\u9876\u9876\u9876\u9876'), '', 'richText/2016/04/04/0d666ad3-4066-4ceb-b90c-40fe90e14cbe.html', TIMESTAMP '2016-04-04 20:04:08.04', NULL), 
(9, 1, '1', '', 'richText/2016/04/04/0977f8e0-b838-4809-aafa-b59e6777aaae.html', TIMESTAMP '2016-04-04 20:04:52.49', NULL), 
(10, 1, 'dddx', '', 'richText/2016/04/05/ba463156-d1ee-49ab-8346-bd4c4b3bad6a.html', TIMESTAMP '2016-04-05 11:30:23.101', NULL), 
(11, 2, 'sdf', '', 'richText/2016/04/05/40f972ad-483b-493e-a09c-60e13f42812c.html', TIMESTAMP '2016-04-05 13:51:54.506', NULL), 
(12, 2, '1', '', 'richText/2016/04/05/19b844de-535f-45bd-96b3-f76d7162f956.html', TIMESTAMP '2016-04-05 13:52:58.892', TIMESTAMP '2016-04-05 13:53:35.212'), 
(36, 2, 'dddd', '', 'richText/2016/04/05/b960e8da-26fc-4a90-8268-ca8ba72f9964.html', TIMESTAMP '2016-04-05 14:07:10.58', TIMESTAMP '2016-04-05 14:07:27.268');
