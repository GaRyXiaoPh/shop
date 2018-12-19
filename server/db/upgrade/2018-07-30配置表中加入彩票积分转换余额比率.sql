insert into `tb_sys_settings`(`id`,`code`,`label`,`mark`,`createTime`) values('15','coupon_transfer_point_ratio','55','彩票积分转换余额比例%','2018-07-30 16:41:56');
alter table tb_user_extract_coupon_log add operateType int(10) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '彩票积分操作类型';
alter table tb_shop_trade_item add raisePrice decimal(20,8) COLLATE utf8mb4_unicode_ci DEFAULT '0.00000000' COMMENT '商品加价';


alter table tb_shop_good add raisePrice decimal(20,8) COLLATE utf8mb4_unicode_ci DEFAULT '0.00000000' COMMENT '商品加价';
alter table tb_user_coupon_log add totalRaisePrice decimal(20,8) COLLATE utf8mb4_unicode_ci DEFAULT '0.00000000' COMMENT '总加价金额';

insert into `tb_good_pay_config`(`id`,`payName`,`payType`,`balanceRatio`,`otherRatio`) values
('3','信用金+优惠券','2','10','90'),
('4','信用金+优惠券','2','20','80'),
('5','信用金+优惠券','2','30','70'),
('6','信用金+优惠券','2','40','60'),
('7','信用金+优惠券','2','50','50'),
('8','信用金+优惠券','2','70','30'),
('9','信用金+优惠券','2','80','20'),
('10','信用金+优惠券','2','90','10');