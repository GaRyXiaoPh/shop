package cn.kt.mall.front.user.controller;


import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.Response;
import cn.kt.mall.common.http.Success;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.wallet.entity.ContactEntity;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.entity.WalletLemEntity;
import cn.kt.mall.common.wallet.service.ContactService;
import cn.kt.mall.common.wallet.service.StatementService;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.common.wallet.service.WalletLemService;
import cn.kt.mall.common.wallet.vo.UserAssetVO;
import cn.kt.mall.front.password.service.TransactionPasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "用户-钱包管理", tags = "user-wallet")
@RequestMapping("/wallet")
@RestController
public class WalletController {

    @Autowired
    UserAssetService userAssetService;        //钱包服务

    @Autowired
    StatementService statementService;  //账户流水服务

    @Autowired
    ContactService constantService;    //联系人服务

    @Autowired
    WalletLemService walletLemService;  //提币地址服务

    @Autowired
    private TransactionPasswordService transactionPasswordService;


    @ApiOperation(value = "获取会员的所有资产")
    @GetMapping("wallet")
    public List<UserAssetEntity> getWallet(){
        return userAssetService.getUserAsset(SubjectUtil.getCurrent().getId());
    }

    @ApiOperation(value = "获取账户流水")
    @GetMapping("getStatement")
    public PageVO<StatementEntity> getStatement(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize){
        return statementService.getStatement(SubjectUtil.getCurrent().getId(), pageNo, pageSize);
    }



    @ApiOperation(value="添加联系人")
    @PostMapping("addConstant")
    public Success addConstant(@RequestParam("nick")String nick, @RequestParam("address")String address){
        constantService.add(SubjectUtil.getCurrent().getId(), nick, address);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "删除联系人")
    @DeleteMapping("delConstant")
    public Success delConstant(@RequestParam("id")String id){
        constantService.del(SubjectUtil.getCurrent().getId(), id);
        return Response.SUCCESS;
    }

    @ApiOperation(value = "修改联系人")
    @PostMapping("updateConstant")
    public Success updateConstant(@RequestParam("id")String id, @RequestParam("nick")String nick, @RequestParam("address")String address){
        constantService.update(SubjectUtil.getCurrent().getId(), id, nick, address);
        return Response.SUCCESS;
    }

    @ApiOperation(value="查询联系人")
    @GetMapping("getConstant")
    public List<ContactEntity> getConstant(){
        return constantService.getConstant(SubjectUtil.getCurrent().getId());
    }

    @ApiOperation(value="查询提币地址(单条)")
    @GetMapping("queryWalletAddress")
    public WalletLemEntity queryWalletAddress() {
        return walletLemService.queryWalletAddress(SubjectUtil.getCurrent().getId());
    }

    @ApiOperation(value="新增提币地址")
    @GetMapping("addWalletAddress")
    public Success addWalletAddress(@RequestParam("popcAddress")String popcAddress,
                                    @RequestParam("remark")String remark){
        WalletLemEntity walletLemEntity = new WalletLemEntity();
        walletLemEntity.setUserId(SubjectUtil.getCurrent().getId());
        walletLemEntity.setPopcAddress(popcAddress);
        walletLemEntity.setRemark(remark);
        walletLemService.addWalletAddress(walletLemEntity);
        return Response.SUCCESS;
    }

    @ApiOperation(value="删除提币地址")
    @GetMapping("delWallet")
    public Success delWallet(@RequestParam("id")String id){
        A.check(id == null || id.equals(""),"参数错误");
        walletLemService.delWallet(id);
        return Response.SUCCESS;
    }

    @ApiOperation(value="修改提币地址")
    @GetMapping("updateWalletAddress")
    public Success updateWalletAddress(@RequestParam("popcAddress")String popcAddress,
                                       @RequestParam("remark")String remark,
                                       @RequestParam("id")String id){
        WalletLemEntity walletLemEntity = new WalletLemEntity();
        walletLemEntity.setId(id);
        walletLemEntity.setUserId(SubjectUtil.getCurrent().getId());
        walletLemEntity.setPopcAddress(popcAddress);
        walletLemEntity.setRemark(remark);
        walletLemService.updateWalletAddress(walletLemEntity);
        return Response.SUCCESS;
    }
}
