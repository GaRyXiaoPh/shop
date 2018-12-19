DROP PROCEDURE  IF EXISTS releaseCoin;

DELIMITER //
CREATE PROCEDURE releaseCoin(IN totalIncome DECIMAL(10,4), IN releaseRate DECIMAL(10,4), IN releaseDate DATE)
BEGIN

DECLARE releaseBalanceValue decimal;
DECLARE reservedBalanceValue decimal;
DECLARE totalReservedCoin decimal;
DECLARE totalIncomeCoinRatio decimal;
DECLARE userIdValue CHAR(36);
DECLARE c_user_asset_id CHAR(36);
DECLARE done INT DEFAULT 0;

DECLARE user_asset_id_cursor CURSOR FOR SELECT id,userId,reservedBalance FROM tb_user_asset where currency ='popc';
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;


SET totalReservedCoin = (SELECT sum(reservedBalance) from tb_user_asset where currency='popc');
SET totalIncomeCoinRatio = (totalIncome * releaseRate) / totalReservedCoin;

OPEN user_asset_id_cursor;

REPEAT

INSERT INTO tb_unfreeze_log VALUES(uuid(), 1, 'popc', releaseRate,totalIncome, (totalIncome * releaseRate),0,NOW(),NOW());

FETCH user_asset_id_cursor INTO c_user_asset_id,userIdValue,reservedBalanceValue;

INSERT INTO tb_unfreeze_log VALUES(uuid(), 1, 'popc', releaseRate,totalIncome, (totalIncome * releaseRate),0,NOW(),NOW());

SET releaseBalanceValue = reservedBalanceValue * totalIncomeCoinRatio;

IF releaseBalanceValue > reservedBalanceValue THEN
SET releaseBalanceValue = reservedBalanceValue;
END IF;

INSERT INTO tb_unfreeze_log VALUES(uuid(), userIdValue, 'popc', releaseRate,totalIncome, (totalIncome * releaseRate),releaseBalanceValue,NOW(),NOW());

UPDATE tb_user_asset set reservedBalance=reservedBalance-releaseBalanceValue,availableBalance=availableBalance+releaseBalanceValue where id=c_user_asset_id;


UNTIL done END REPEAT;
CLOSE user_asset_id_cursor;

END //
DELIMITER ;