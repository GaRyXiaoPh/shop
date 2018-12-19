package cn.kt.mall.test;

import cn.kt.mall.common.user.dao.UserLoginDAO;
import cn.kt.mall.common.user.entity.UserLoginEntity;
import cn.kt.mall.common.user.model.UserInfo;
import cn.kt.mall.common.user.vo.LoginInfoVO;
import cn.kt.mall.common.user.vo.LoginVO;
import cn.kt.mall.front.user.service.UserService;
import io.shardingjdbc.core.api.HintManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLReadWriteDelayTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginDAO userLoginDAO;

    @Test
    public void loginAndGetInfo() {
        login();
        login();
    }

    public void login() {
        LoginVO vo = new LoginVO();
        vo.setUsername("13410815370");
        vo.setPassword("a123456");
        LoginInfoVO login = userService.login(vo);
        HintManager hintManager = HintManager.getInstance() ;
        hintManager.setMasterRouteOnly();
        UserLoginEntity byUserId = userLoginDAO.getByUserId(login.getId());
        hintManager.close();
        assertEquals(login.getAccessToken(), byUserId.getToken());
    }
}
