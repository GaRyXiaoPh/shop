DROP PROCEDURE IF EXISTS `base_point_release`;
DELIMITER ;;
CREATE PROCEDURE `base_point_release`(IN `v_userId` varchar(36),IN `v_couponId` varchar(36),IN `v_assetType` varchar(20))
BEGIN

  DECLARE v_reservedBalance DECIMAL(18, 10);
  DECLARE v_releaseNum DECIMAL(18, 10);
	DECLARE v_afterAmount DECIMAL(18, 10);
	DECLARE v_beforRealseNum DECIMAL(18, 10);
	DECLARE v_base_needReleseDays INTEGER;
	DECLARE v_asset_reservedBalance DECIMAL(18,10);
	DECLARE v_asset_availableBalance DECIMAL(18,10);
	DECLARE v_sendFinish INTEGER;
	DECLARE v_base_availableBalance DECIMAL(18,10);
	DECLARE v_base_reservedBalance DECIMAL(18,10);
	DECLARE v_base_currentReleseNum INTEGER;
	DECLARE v_couponNum DECIMAL(18,10);
	DECLARE v_i INTEGER;
	DECLARE v_releaseAfterNum DECIMAL(18,10);
	DECLARE v_releaseBeforCouponNum DECIMAL(18,10);
		set v_base_availableBalance = 0;
		set v_base_reservedBalance = 0;
		set v_base_currentReleseNum =0;
		set v_base_needReleseDays = 0;
		set v_releaseNum = 0;
		set v_sendFinish = 0;
		SELECT
			reservedBalance,currentReleseNum,needReleseDays
			into  v_base_reservedBalance, v_base_currentReleseNum,v_base_needReleseDays
		from tb_user_asset_base where userId=v_userId and assetType=v_assetType;

		set v_releaseNum = v_base_reservedBalance/v_base_needReleseDays;
		set v_base_currentReleseNum = v_base_currentReleseNum+1;
		if v_base_currentReleseNum =v_base_needReleseDays then
			set v_sendFinish=1;
		end if;


		set v_couponNum =0;
		set v_releaseBeforCouponNum =0;

		select couponNum,reservedCouponNum into v_couponNum,v_releaseBeforCouponNum from tb_user_coupon  where userId=v_userId and couponId=v_couponId;

		update tb_user_coupon SET couponNum = couponNum+v_releaseNum ,reservedCouponNum =reservedCouponNum-v_releaseNum where userId=v_userId and couponId=v_couponId;
		set v_releaseAfterNum = v_couponNum+v_releaseNum;

		update tb_user_asset_base set
						currentReleseNum = v_base_currentReleseNum,
            sendFinish = v_sendFinish,
            afterAmount = v_releaseAfterNum,
            lastReleaseTime=UNIX_TIMESTAMP() where assetType=v_assetType and userId=v_userId;
		#增加玩家日志表



		#增加释放日志
		 insert into tb_user_release_coupon_log(userId, couponLogId, amount,createTime,releaseAfterNum,releaseBeforNum,releaseType)
        VALUES

        (v_userId,"database",v_releaseNum,NOW(),v_releaseAfterNum,v_releaseBeforCouponNum,1);

END
;;
DELIMITER ;