package cn.kt.mall.common.wallet.service;

import cn.kt.mall.common.bitcoin.service.LemService;
import cn.kt.mall.common.wallet.base.Constants;
import cn.kt.mall.common.wallet.entity.TransferEntity;
import cn.kt.mall.common.wallet.mapper.TransferDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class TransferService {
    private static Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    TransferDAO transferDAO;
    @Autowired
    LemService lemService;  //莱姆币节点

    //添加转账记录
    public void addTransfer(String hash, String fromUserId, String toAddress, String mark, BigDecimal amount){
        TransferEntity entity = new TransferEntity(hash, fromUserId, toAddress, amount, mark, Constants.TransferStatus.UNCONFIRM);
        transferDAO.add(entity);
    }


    //-----------------------------------------------------------------------------------------------
    //定时服务调用确认交易完成
    public void confirmTransferJob(){
        logger.debug("confirmTransfer start ......");
        List<TransferEntity> list = transferDAO.getUnConfirmTransfer();
        for (int i=0; i<list.size(); i++){
            try {
                //获取节点交易,确认数大于12就认为是OK的
                //BitcoindRpcClient.RawTransaction trans = lemService.getTransaction(list.get(i).getHash());
                //if (trans.confirmations() > 6){
                //    transferDAO.confirm(trans.txId());
                 //   logger.debug("confirm:"+trans.txId());
                //}
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }
}
