
DROP PROCEDURE IF EXISTS `base_popc_release`;
DELIMITER ;;
CREATE  PROCEDURE `base_popc_release`(IN `v_userId` varchar(36),IN `v_assetType` varchar(20))
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

		set v_asset_reservedBalance =0;
		set v_asset_availableBalance =0;

		SELECT reservedBalance,availableBalance  into v_asset_reservedBalance,v_asset_availableBalance FROM tb_user_asset WHERE currency = v_assetType and userId=v_userId;
		if v_asset_reservedBalance>v_releaseNum then
			update tb_user_asset_base set
           currentReleseNum = v_base_currentReleseNum,
            sendFinish = v_sendFinish,
            afterAmount = v_asset_availableBalance+v_releaseNum,
            lastReleaseTime=UNIX_TIMESTAMP() where assetType=v_assetType and userId=v_userId;

			UPDATE tb_user_asset set
								availableBalance=availableBalance+v_releaseNum,
                reservedBalance=reservedBalance-v_releaseNum

            WHERE userId=v_userId and currency=v_assetType;

			INSERT INTO tb_user_statement(id, userId, currency,
							availableBefore, availableAfter,availableChange,
							reservedBefore, reservedAfter,reservedChange,
							tradeType,referenceId,status,mark,createTime
							)
					VALUES
							(UUID(),v_userId,v_assetType,
								v_asset_availableBalance,v_asset_availableBalance+v_releaseNum,v_releaseNum,
								v_asset_reservedBalance,v_asset_reservedBalance-v_releaseNum,-v_releaseNum,
								24,v_userId,"0","基础数据释放",NOW()
							);
		end if;

END
;;
DELIMITER ;

alter table tb_user_asset_base add `lastReleaseTime` bigint(20) DEFAULT '0' COMMENT '最近释放的时间';