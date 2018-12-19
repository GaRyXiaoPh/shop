package cn.kt.mall.im.rong.controller;

import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.im.rong.constant.Constants;
import cn.kt.mall.im.rong.entity.RongTokenEntity;
import cn.kt.mall.im.rong.mapper.RongTokenMapper;
import cn.kt.mall.im.rong.model.RongUser;
import cn.kt.mall.im.rong.service.RongCloudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "IM-融云管理", tags = "im-rong")
@RequestMapping("/im/rong")
@RestController
public class RongController {

    @Autowired
    RongCloudService rongCloudService;

    @Autowired
    RongTokenMapper rongTokenMapper;

    @Autowired
    UserDAO userDAO;

    //获取对应的融云token
    @ApiOperation(value = "获取融云token", notes = "")
    @GetMapping("/getToken")
    @ResponseBody
    public RongTokenEntity getToken(){
        String userId = SubjectUtil.getCurrent().getId();
        UserEntity userEntity = userDAO.getById(userId);
        RongTokenEntity rongTokenEntity = rongTokenMapper.getTokenByUser(userId);
        if (rongTokenEntity==null){
            if (userEntity.getAvatar()==null)
                userEntity.setAvatar("http://www.baidu.com");
            RongUser rongUser = new RongUser(userId, userEntity.getNick(), userEntity.getAvatar());
            String token = rongCloudService.getToken(rongUser);
            rongTokenEntity = new RongTokenEntity(IDUtil.getUUID(), userId, token);
            rongTokenMapper.addToken(rongTokenEntity);
        } else {
            if (Constants.TokenStatus.TOKEN_INVALID.equals(rongTokenEntity.getStatus())){
                RongUser rongUser = new RongUser(userId, userEntity.getNick(), userEntity.getAvatar());
                String token = rongCloudService.getToken(rongUser);
                rongTokenMapper.updateToken(userId, token);
            }
        }
        return rongTokenMapper.getTokenByUser(userId);
    }

}
