package cn.kt.mall.common.bitcoin.base;

import cn.kt.mall.common.bitcoin.base.javabitcoindrpcclient.BitcoinJSONRPCClient;
import cn.kt.mall.common.bitcoin.base.javabitcoindrpcclient.BitcoindRpcClient;
import cn.kt.mall.common.exception.ServerException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/29.
 */

public class BitcoinUtil {

    static public String double2string(double balance){
        BigDecimal bigDecimal = new BigDecimal(balance);
        return bigDecimal.toString();
    }

    static public String createAddress(String strUserID, String rpcUrl) {
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.getNewAddress(strUserID);
        } catch (Exception e) {
            throw new ServerException("getNewAddress exception");
        }
    }

    static public String getAddressByUserID(String strUserID, String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            List<String> list = coinRpcClient.getAddressesByAccount(strUserID);
            if (list!=null && list.size()>0)
                return list.get(0);
            else
                return null;
        } catch (Exception e) {
            throw new ServerException("getNewAddress exception");
        }
    }

    static public double getBalance(String strUserID, String rpcUrl) {
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.getBalance(strUserID);
        } catch (Exception e) {
            throw new ServerException("getBalance exception");
        }
    }

    static public String sendFrom(String strUserID, String toAddress, double amount, String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.sendFrom(strUserID, toAddress, amount);
        } catch (Exception e) {
            throw new ServerException("sendFrom exception");
        }
    }

    static public String sendToAddress(String toAddress, double amount, String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.sendToAddress(toAddress, amount);
        } catch (Exception e) {
            throw new ServerException("sendToAddress exception");
        }
    }

    static public List<BitcoindRpcClient.Transaction> listTransaction(String strUserID, String rpcUrl) {
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.listTransactions(strUserID);
        } catch (Exception e){
            throw new ServerException("listTransaction exception");
        }
    }

    static public List<BitcoindRpcClient.Transaction> listTransaction(String strUserID, String rpcUrl, int count) {
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.listTransactions(strUserID, count);
        } catch (Exception e){
            throw new ServerException("listTransaction2 exception");
        }
    }

    static public List<BitcoindRpcClient.Transaction> listTransaction(String strUserID, String rpcUrl, int count, int start) {
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.listTransactions(strUserID, count, start);
        } catch (Exception e){
            throw new ServerException("listTransaction3 exception");
        }
    }

    static public BitcoindRpcClient.RawTransaction getTransaction(String txid, String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.getRawTransaction(txid);
        } catch (Exception e){
            return null;
        }
    }

    static public Map<String, Object> getTransactionEx(String txid, String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.getTransactionEx(txid);
        }catch (Exception e){
            return null;
        }
    }

    static public int getBlockCount(String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.getBlockCount();
        } catch (Exception e){
            throw  new ServerException("getBlockCount exception");
        }
    }

    static public BitcoindRpcClient.Block getBlock(int idx, String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            return coinRpcClient.getBlock(idx);
        } catch (Exception e){
            throw new ServerException("getBlockHash exception");
        }
    }

    static public boolean isValid(String address, String rpcUrl){
        try {
            BitcoindRpcClient coinRpcClient = new BitcoinJSONRPCClient(rpcUrl);
            BitcoindRpcClient.AddressValidationResult rst = coinRpcClient.validateAddress(address);
            if (rst.isValid())
                return true;
        }catch (Exception e){
            return  false;
        }
        return  false;
    }

}
