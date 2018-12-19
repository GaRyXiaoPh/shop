package cn.kt.mall.task;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.common.user.model.UserLevel;
import cn.kt.mall.shop.good.mapper.GoodDAO;
import cn.kt.mall.shop.shop.mapper.ShopDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TeamCountTask {
    private static Logger logger = LoggerFactory.getLogger(TeamCountTask.class);

    @Autowired
    private UserDAO userDAO;

    /*** 每天定时任务2点刷新，针对于更新团队人数*/
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void updateTeamCountTask() {
        logger.info("---------------update team person count task start---------------------------");
        try {
            Date now = new Date();
            //1.统计等级为3：站长的团队人数-----查询列表
            List<UserEntity> listUser = userDAO.getUserByLevelNum(UserLevel.L2.getSort());
            //2.统计等级为3：站长的团队人数-----计算每个用户的团队人数
            for (UserEntity userEntity : listUser) {
                int teamCount = userDAO.getCountByPid(userEntity.getId());
                userEntity.setTeamCount(teamCount);
            }
            //3.更新等级为3：站长的团队人数
            int rows = userDAO.updateUserTeamCountByIds(listUser);
            if (rows <= 0) {
                logger.error("failed to updateUserTeamCountByIds" + "zhan zhang");
            }
            //4.统计等级为4：中心主任的团队人数-----查询列表
            List<UserEntity> listUsers = userDAO.getUserByLevelNum(UserLevel.L3.getSort());
            for (UserEntity user : listUsers) {
                //1.统计该中心主任下属店长团队总人数
                int team = userDAO.getUserByPidStr(user.getId(),String.valueOf(UserLevel.L2.getSort()));
                //2.统计该中心主任下属普通会员总人数
                int levelOne = userDAO.getlevelOneCountByPid(user.getId(),String.valueOf(UserLevel.DEFAULT.getSort()));
                user.setTeamCount(team + levelOne);
            }
            //5.更新等级为4：中心主任的团队人数
            int rowss = userDAO.updateUserTeamCountByIds(listUsers);
            if (rowss <= 0) {
                logger.error("failed to updateUserTeamCountByIds====" + "zhong xin zhu ren");
            }
            Date endTime = new Date();
            long minute = (endTime.getTime() - now.getTime()) / 1000;
            logger.info("---------------------------------------------------------");
            logger.info("                     update team person count task complete                     ");
            logger.info("---------------complete time：" + minute + "second--------------------");
            logger.info("---------------------------------------------------------");
            logger.info("---------------zhan zhang count：" + rows + "--------------------");
            logger.info("---------------------------------------------------------");
            logger.info("---------------zhongxin zhuren count：" + rowss + "--------------------");
            logger.info("---------------------------------------------------------");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
