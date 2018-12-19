DROP PROCEDURE IF EXISTS `trade_popc_release`;
DELIMITER ;;
CREATE PROCEDURE `trade_popc_release`(IN `v_id` varchar(36),IN `v_couponId` varchar(36),IN `v_assetType`  varchar(36))
BEGIN
	DECLARE v_userId VARCHAR(36);
	DECLARE v_asset_reservedBalance DECIMAL(18,10);
	DECLARE v_asset_availableBalance DECIMAL(18,10);
	#tb_user_coupon
	DECLARE v_couponNum DECIMAL(18,10);
	DECLARE v_currentReleseNum INTEGER;
	#
	DECLARE v_sendFinish INTEGER;
	DECLARE v_everyTimeReleaseNum DECIMAL(18,10);

	DECLARE v_needReleseDays INTEGER;
	DECLARE v_releaseAfterNum DECIMAL(18,10);
	DECLARE v_releaseBeforCouponNum DECIMAL(18,10);
	DECLARE v_rechargeNum DECIMAL(18,10);

	DECLARE v_count INTEGER;

			set v_currentReleseNum = 0;
			set v_needReleseDays = 0;
			set v_everyTimeReleaseNum = 0;
			set v_sendFinish = 0;
			set v_userId ="";
			set v_rechargeNum=0;

			select userId,rechargeNum ,currentReleaseNum,needReleseDays  into v_userId ,v_rechargeNum,v_currentReleseNum,v_needReleseDays from tb_user_coupon_log where id = v_id;

			set v_everyTimeReleaseNum = v_rechargeNum/v_needReleseDays;
			set v_currentReleseNum =v_currentReleseNum+1;
			if v_currentReleseNum>= v_needReleseDays then
				set v_sendFinish=1;
			end if;

			SELECT reservedBalance,availableBalance  into v_asset_reservedBalance,v_asset_availableBalance FROM tb_user_asset WHERE currency = v_assetType and userId=v_userId;
			if v_asset_reservedBalance>v_everyTimeReleaseNum  then
				#修改玩家的优惠卷日志表
				UPDATE tb_user_coupon_log set
									sendFinish = v_sendFinish,
									currentReleaseNum = v_currentReleseNum,
									everyTimeReleaseNum = v_everyTimeReleaseNum,
									lastReleaseTime = UNIX_TIMESTAMP()
							WHERE id = v_id;

				set v_releaseAfterNum=0;
				set v_releaseBeforCouponNum=0;
				#查询玩家该类型的统计总表信息
				set v_count = 0;
				select count(*) INTO v_count from tb_user_coupon  where userId=v_userId and couponId=v_couponId;
					if v_count<1 then

						 set v_releaseBeforCouponNum = v_rechargeNum;
						 set v_releaseAfterNum = v_rechargeNum-v_everyTimeReleaseNum;

						 INSERT INTO tb_user_coupon (id, userId,cdkeyNum,couponType,couponNum,createTime,status,couponId,reservedCouponNum)
							values
						(UUID(),v_userId,0,1,v_everyTimeReleaseNum,now(),1,v_couponId,v_releaseBeforCouponNum);

					else
						select couponNum,reservedCouponNum into v_couponNum,v_releaseBeforCouponNum from tb_user_coupon  where userId=v_userId and couponId=v_couponId;

						update tb_user_coupon SET couponNum = couponNum+v_everyTimeReleaseNum ,reservedCouponNum =reservedCouponNum-v_everyTimeReleaseNum where userId=v_userId and couponId=v_couponId;
						set v_releaseAfterNum = v_releaseBeforCouponNum - v_everyTimeReleaseNum;
						set v_releaseBeforCouponNum = v_releaseBeforCouponNum;

					end if;


					UPDATE tb_user_asset set
									availableBalance=availableBalance+v_everyTimeReleaseNum,
									reservedBalance=reservedBalance-v_everyTimeReleaseNum

								WHERE userId=v_userId and currency=v_assetType;

					INSERT INTO tb_user_statement(id, userId, currency,
									availableBefore, availableAfter,availableChange,
									reservedBefore, reservedAfter,reservedChange,
									tradeType,referenceId,status,mark,createTime
									)
							VALUES
									(UUID(),v_userId,v_assetType,
										v_asset_availableBalance,v_asset_availableBalance+v_everyTimeReleaseNum,v_everyTimeReleaseNum,
										v_asset_reservedBalance,v_asset_reservedBalance-v_everyTimeReleaseNum,-v_everyTimeReleaseNum,
										7,v_userId,"0","订单popc释放",NOW()
									);

				#增加释放日志
				 insert into tb_user_release_coupon_log(userId, couponLogId, amount,createTime,releaseAfterNum,releaseBeforNum,releaseType)
						VALUES
						(v_userId,v_id,v_everyTimeReleaseNum,NOW(),v_releaseAfterNum,v_releaseBeforCouponNum,0);

			end if;
	end
;;
DELIMITER ;
