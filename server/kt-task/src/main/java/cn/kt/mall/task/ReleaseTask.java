package cn.kt.mall.task;

import cn.kt.mall.common.common.service.SysSettingsService;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.common.wallet.base.TradeType;
import cn.kt.mall.common.wallet.entity.StatementEntity;
import cn.kt.mall.common.wallet.entity.UserAssetEntity;
import cn.kt.mall.common.wallet.mapper.UserAssetDAO;
import cn.kt.mall.common.wallet.service.UserAssetService;
import cn.kt.mall.shop.coupon.entity.*;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import cn.kt.mall.shop.coupon.mapper.ReleaseCouponsDAO;
import cn.kt.mall.shop.coupon.mapper.ReleaseUserAssetBaseDAO;
import cn.kt.mall.shop.coupon.repository.UserCouponLogEntityMapper;
import cn.kt.mall.shop.coupon.repository.UserCouponLogPatchMapper;
import cn.kt.mall.shop.coupon.service.*;
import cn.kt.mall.shop.coupon.vo.BasePointVO;
import cn.kt.mall.shop.coupon.vo.BasePopcVO;
import cn.kt.mall.shop.coupon.vo.CouponTimeConfigVo;
import cn.kt.mall.shop.coupon.vo.CouponsVO;
import cn.kt.mall.shop.coupon.vo.TBUserCouponVO;
import cn.kt.mall.shop.coupon.vo.TradePopcAndPointVO;
import cn.kt.mall.shop.coupon.vo.UserAssertAndStateMentVO;
import cn.kt.mall.shop.coupon.vo.UserAssetBaseSearchVO;
import cn.kt.mall.shop.coupon.vo.UserAssetEntityVO;
import cn.kt.mall.shop.coupon.vo.UserCouponSearchVO;
import cn.kt.mall.shop.release.service.ReleaseService;
import cn.kt.mall.shop.release.service.impl.ReleaseServiceImpl;
import cn.kt.mall.shop.trade.service.TradeService;
import cn.kt.mall.task.service.PatchService;
import com.graphbuilder.struc.LinkedList;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class ReleaseTask {
    private static Logger logger = LoggerFactory.getLogger(ReleaseTask.class);

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    @Value("${task.release_task_number:10}")
    private Integer releaseTaskNumber;
    @Autowired
    private UserAssetService userAssetService;
    @Autowired
    private UserAssetDAO userAssetDAO;
    @Autowired
    private SysSettingsService sysSettingsService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private CouponsService couponsService;
    @Autowired
    private UserReleaseCouponsLogService userReleaseCouponsLogService;

    @Autowired
    private UserCouponLogService userCouponLogService;

    @Autowired
    private UserCouponEntityService userCouponEntityService;

    @Autowired
    private CouponsDAO couponsDAO;

    @Autowired
    private UserReturnCouponLogService userReturnCouponLogService;
    @Autowired
    private UserAssetBaseService userAssetBaseService;
    @Autowired
    private PatchService patchService;

    @Autowired
    private UserCouponLogEntityMapper userCouponLogEntityMapper;

    @Autowired
    private ReleaseServiceImpl releaseService;

    @Autowired
    private ReleaseCouponsDAO releaseCouponsDAO;

    @Autowired
    private ReleaseUserAssetBaseDAO releaseUserAssetBaseDAO;

    private static int poolSieze =20;
    //线程池
    private static  ExecutorService pool = Executors.newFixedThreadPool(poolSieze);

    //
    private static CountDownLatch countDownLatch = null;
    //时间对应的信息
   // private static long times = 24 * 60 * 60 * 1000;

    //87916019-19e1-4383-8539-a7a972134d81
    private static String pointId="87916019-19e1-4383-8539-a7a972134d81";
    private static String popcId ="825a52fd-737e-4c02-9a25-1aa8de132fce";


    private static int pageNum = 10000;

    private static Thread releaseThread =null;

    /**
     * 每天定时任务凌晨刷新，针对于用户优惠券发放作用*/
    //@Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public synchronized void couponsDailyTask() {

        if(releaseThread!=null&&releaseThread.isAlive()){
            return ;
        }
        releaseThread = new Thread() {
            public void run(){
                logger.info("---------------解冻定时任务启动1---------------------------");
                //获取当前时间
                Date now = new Date();
                Date forMatDate = now;
                long searchTimes = getFormatDate(now).getTime() / 1000;
                long forMatDateTime = getNextFreshDateByLong(forMatDate.getTime()).getTime();

                //查询优惠券出配置表的信息
                List<CouponsVO> tbCouponList = couponsDAO.getAllTbCouponList();

                //订单popc释放
                tradePopcRelease(forMatDate, forMatDateTime, tbCouponList, searchTimes);

                //订单彩票积分释放
                tradePointRelease(forMatDate, forMatDateTime, tbCouponList, searchTimes);
                // 基础数据popc积分
                basePopcRelease(forMatDateTime, tbCouponList, searchTimes);

                //基础数据ponit
                basePointRelease(forMatDateTime, tbCouponList, searchTimes);



                Date endTime = new Date();
                long minute = (endTime.getTime() - now.getTime()) / 1000;
                logger.info("---------------------------------------------------------");
                logger.info("                     解冻定时任务完成                     ");
                logger.info("---------------完成时长：" + minute + "秒--------------------");
                logger.info("---------------------------------------------------------");
            }
        };
        releaseThread.start();
    }



    //手动释放
    public synchronized void handleToRelease(){

                //获取当前时间
                Date now = new Date();
                Date forMatDate = now;
                Long searchTimes = null;
                long forMatDateTime = getNextFreshDateByLong(forMatDate.getTime()).getTime();

                //查询优惠券出配置表的信息
                List<CouponsVO> tbCouponList = couponsDAO.getAllTbCouponList();

                //订单popc释放
                tradePopcRelease(forMatDate, forMatDateTime, tbCouponList, searchTimes);

                //订单彩票积分释放
                tradePointRelease(forMatDate, forMatDateTime, tbCouponList, searchTimes);
                // 基础数据popc积分
                basePopcRelease(forMatDateTime, tbCouponList, searchTimes);

                //基础数据ponit
                basePointRelease(forMatDateTime, tbCouponList, searchTimes);


                Date endTime = new Date();
                long minute = (endTime.getTime() - now.getTime()) / 1000;
                logger.info("---------------------------------------------------------");
                logger.info("                     手动释放任务完成                     ");
                logger.info("---------------完成时长：" + minute + "秒--------------------");
                logger.info("---------------------------------------------------------");

    }



    //存储过程===================释放
    //基础数据popc释放
    public synchronized void basePopcRelease(long forMatDateTime,List<CouponsVO>  tbCouponList,Long searchTimes) {
        logger.info("释放基础导入的popc数据开始");
        HashSet<String> canReleasCouponSet = getCanReleasCouponSet(forMatDateTime, tbCouponList);
        boolean bool = false;
        if (canReleasCouponSet != null && canReleasCouponSet.size() > 0) {
            for (String coupid : canReleasCouponSet) {
                for (CouponsVO vo : tbCouponList) {
                    if (vo.getCouponEnglishName().equals("popc") && vo.getId().equals(coupid)) {
                        bool = true;
                        break;
                    }
                }
            }
        }
        if (bool) {
            UserAssetBaseSearchVO searchVO = new UserAssetBaseSearchVO();
            searchVO = new UserAssetBaseSearchVO();
            searchVO.setAssetType("popc");
            searchVO.setLastReleaseTime(searchTimes);
            //int allCount = userAssetBaseService.getUserAssetBaseCountByAssetType(searchVO);
            int allCount = releaseUserAssetBaseDAO.getReleaseUserAssetBaseCountByAssetType(searchVO);
            if(allCount>0) {
                logger.info("allCount=" + allCount);
                //总页数
                int numLength = (allCount + pageNum - 1) / pageNum;
                logger.info("page num=" + numLength);
                //算出总页数

                List<UserAssetBaseEntity> list = new ArrayList<>();
                searchVO = new UserAssetBaseSearchVO();
                searchVO.setAssetType("popc");
                searchVO.setStart(0);
                searchVO.setEnd(allCount);
                searchVO.setLastReleaseTime(searchTimes);
                //list = userAssetBaseService.getUserAssetBaseLsitByassetType(searchVO);
                list = releaseUserAssetBaseDAO.getReleaseUserAssetBaseLsitByassetType(searchVO);
                //可以释放的优惠券Id
                int workNum = (list.size() + 999) / 1000;
                BasePopcReleaseRun run = null;
                countDownLatch = new CountDownLatch(workNum);
                int count = 0;
                for (UserAssetBaseEntity entry : list) {
                    if (count % 1000 == 0) {
                        if (run != null) {
                            pool.execute(run);
                            run = null;
                        }
                        run = new BasePopcReleaseRun();
                        run.countDownLatch = countDownLatch;
                        run.releaseService = releaseService;
                    }
                    count++;
                    run.ls.add(entry);
                }
                if (run != null) {
                    pool.execute(run);
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException ex) {
                    logger.warn("", ex);
                }
            }
            }else{
            logger.info("today_popc_no_relese");
        }


    }

    public static class BasePopcReleaseRun implements Runnable {
        private static Logger logger = LoggerFactory.getLogger(BasePopcReleaseRun.class);

        public List<UserAssetBaseEntity> ls = new ArrayList<UserAssetBaseEntity>(1000);
        public CountDownLatch countDownLatch;
        public ReleaseServiceImpl releaseService;

        public void run() {
            try {
                logger.info("base_popc_begin run ");
                long start = System.currentTimeMillis();
                List<BasePopcVO> basePopcVOList = new ArrayList<BasePopcVO>(1000);
                for(UserAssetBaseEntity data : ls) {
                    BasePopcVO basePopcVO = new BasePopcVO();
                    basePopcVO.setV_assetType("popc");
                    basePopcVO.setV_userId(data.getUserId());
                    basePopcVOList.add(basePopcVO);
                }
                // sql
                releaseService.releaseBasePopc(basePopcVOList);
                countDownLatch.countDown();
                logger.info("base_popc_begin_end run,size=" + ls.size() + ",time(ms)=" + (System.currentTimeMillis() -start));
            }catch (Exception e){
                logger.error("base_popc_begin_Excepion==",e);
            }
        }
    }

    //基础数据point释放
    public synchronized void basePointRelease(long forMatDateTime,List<CouponsVO>  tbCouponList,Long searchTimes){
        HashSet<String> canReleasCouponSet = getCanReleasCouponSet(forMatDateTime, tbCouponList);
        boolean bool = false;
        if (canReleasCouponSet != null && canReleasCouponSet.size() > 0) {
            for (String coupid : canReleasCouponSet) {
                for (CouponsVO vo : tbCouponList) {
                    if ( vo.getId().equals(coupid)) {
                        bool = true;
                        break;
                    }
                }
            }
        }
        if (bool) {
            logger.info("release_base_coupon_release");
            UserAssetBaseSearchVO searchVO = new UserAssetBaseSearchVO();
            searchVO = new UserAssetBaseSearchVO();
            searchVO.setAssetType("coupon");
            searchVO.setLastReleaseTime(searchTimes);
           // int allCount = userAssetBaseService.getUserAssetBaseCountByAssetType(searchVO);
            int allCount = releaseUserAssetBaseDAO.getReleaseUserAssetBaseCountByAssetType(searchVO);
            //总页数
            //int numLength = (allCount + pageNum - 1)/pageNum;
            //算出总页数
            if(allCount>0){
                List<UserAssetBaseEntity> list = new ArrayList<>();
                searchVO = new UserAssetBaseSearchVO();
                searchVO.setAssetType("coupon");
                searchVO.setStart(0);
                searchVO.setEnd(allCount);
                searchVO.setLastReleaseTime(searchTimes);
                //list = userAssetBaseService.getUserAssetBaseLsitByassetType(searchVO);
                list = releaseUserAssetBaseDAO.getReleaseUserAssetBaseLsitByassetType(searchVO);
                //可以释放的优惠券Id
                int workNum = (list.size() + 999) / 1000;
                BasePointReleaseRun run = null;
                countDownLatch = new CountDownLatch(workNum);
                int count = 0;
                for (UserAssetBaseEntity entry : list) {
                    if (count % 1000 == 0) {
                        if (run != null) {
                            pool.execute(run);
                            run = null;
                        }
                        run = new BasePointReleaseRun();
                        run.countDownLatch = countDownLatch;
                        run.releaseService = releaseService;
                    }
                    count++;
                    run.ls.add(entry);
                }
                if (run != null) {
                    pool.execute(run);
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException ex) {
                    logger.warn("", ex);
                }
            }
        }else{
            logger.info("today_coupon_no_relese");
        }
    }


    public static class BasePointReleaseRun implements Runnable {
        private static Logger logger = LoggerFactory.getLogger(BasePointReleaseRun.class);

        public List<UserAssetBaseEntity> ls = new ArrayList<UserAssetBaseEntity>(1000);
        public CountDownLatch countDownLatch;
        public ReleaseServiceImpl releaseService;

        public void run() {
            try {
                logger.info("base_coupon_begin run ");
                long start = System.currentTimeMillis();
                List<BasePointVO> basePointVOList = new ArrayList<BasePointVO>(1000);
                for (UserAssetBaseEntity data : ls) {
                    BasePointVO basePointVO = new BasePointVO();
                    basePointVO.setV_userId(data.getUserId());
                    basePointVO.setV_couponId(pointId);
                    basePointVO.setV_assetType("coupon");
                    basePointVOList.add(basePointVO);
                }
                // sql
                releaseService.releaseBasePoint(basePointVOList);
                countDownLatch.countDown();
                logger.info("base_coupon_begin_end run,size=" + ls.size() + ",time(ms)=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                logger.error("base_coupon_begin_Excepion==", e);
            }
        }
    }


    //释放玩家订单生产的优惠劵=========================//
    public synchronized void tradePopcRelease(Date forMatDate,long forMatDateTime, List<CouponsVO>  tbCouponList,Long searchTimes){
        HashSet<String> canReleasCouponSet = getCanReleasCouponSet(forMatDateTime, tbCouponList);
        boolean bool =false;
        //判断优惠卷是否可释放
        for (String couponId : canReleasCouponSet) {
            for (CouponsVO vo : tbCouponList) {
                if (couponId.equals(popcId)) {
                    bool = true;
                    break;
                }
            }
        }
        if(bool) {
            UserCouponSearchVO userCouponSearchVO = new UserCouponSearchVO();
            Date nowDate = getFormatDate(forMatDate);
            userCouponSearchVO.setCreateTime(nowDate);
            userCouponSearchVO.setCouponId(popcId);
            userCouponSearchVO.setLastReleaseTime(searchTimes);
            int userCouponLogCount = releaseCouponsDAO.getReleaseUserCoupobLogByTimeCount(userCouponSearchVO);
            if(userCouponLogCount>0){
                userCouponSearchVO = new UserCouponSearchVO();
                userCouponSearchVO.setCreateTime(nowDate);
                userCouponSearchVO.setCouponId(popcId);
                userCouponSearchVO.setStart(0);
                userCouponSearchVO.setEnd(userCouponLogCount);
                userCouponSearchVO.setLastReleaseTime(searchTimes);
                List<UserCouponLogEntity> list = releaseCouponsDAO.getReleaseUserCouponsLogListByTime(userCouponSearchVO);

                //可以释放的优惠券Id
                int workNum = (list.size() + 999)/1000;
                TradePopcReleaseRun run = null;
                countDownLatch = new CountDownLatch(workNum);
                int count = 0;
                for(UserCouponLogEntity entry : list) {
                    if(count % 1000 == 0) {
                        if(run != null) {
                            pool.execute(run);
                            run = null;
                        }
                        run = new TradePopcReleaseRun();
                        run.countDownLatch = countDownLatch;
                        run.releaseService = releaseService;
                    }
                    count++;
                    run.ls.add(entry);
                }
                if(run != null) {
                    pool.execute(run);
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException ex) {
                    logger.warn("", ex);
                }
            }
        }else{
            logger.info("today_point_no_relese");
        }
    }

    public static class TradePopcReleaseRun implements Runnable{

        private static Logger logger = LoggerFactory.getLogger(TradePopcReleaseRun.class);

        public List<UserCouponLogEntity> ls = new ArrayList<UserCouponLogEntity>(1000);
        public CountDownLatch countDownLatch;
        public ReleaseServiceImpl releaseService;

        public void run() {
            try {
                logger.info("trade_popc_begin run ");
                long start = System.currentTimeMillis();
                List<TradePopcAndPointVO> tradePopcVOList = new ArrayList<TradePopcAndPointVO>(1000);
                for (UserCouponLogEntity data : ls) {
                    TradePopcAndPointVO tradePopcAndPointVO = new TradePopcAndPointVO();
                    tradePopcAndPointVO.setV_assetType("popc");
                    tradePopcAndPointVO.setV_couponId(popcId);
                    tradePopcAndPointVO.setV_id(data.getId());
                    tradePopcVOList.add(tradePopcAndPointVO);

                }
                // sql
                releaseService.callTradePopcRelease(tradePopcVOList);
                countDownLatch.countDown();
                logger.info("trade_popc_begin_end run,size=" + ls.size() + ",time(ms)=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                logger.error("trade_popc_begin_Excepion==", e);
            }
        }
    }

    //释放玩家订单生产的彩票积分========================//
    public synchronized void tradePointRelease(Date forMatDate,long forMatDateTime, List<CouponsVO>  tbCouponList,Long searchTimes){
        HashSet<String> canReleasCouponSet = getCanReleasCouponSet(forMatDateTime, tbCouponList);
        boolean bool =false;
        //判断优惠卷是否可释放
        for (String couponId : canReleasCouponSet) {
            for (CouponsVO vo : tbCouponList) {
                if (couponId.equals(pointId)) {
                    bool = true;
                    break;
                }
            }
        }
        if(bool) {
            UserCouponSearchVO userCouponSearchVO = new UserCouponSearchVO();
            Date nowDate = getFormatDate(forMatDate);
            userCouponSearchVO.setCreateTime(nowDate);
            userCouponSearchVO.setCouponId(pointId);
            System.out.println(searchTimes+"=="+searchTimes);
            userCouponSearchVO.setLastReleaseTime(searchTimes);
            //int userCouponLogCount = couponsService.getUserCouponLogCountByTime(userCouponSearchVO);
            int userCouponLogCount = releaseCouponsDAO.getReleaseUserCoupobLogByTimeCount(userCouponSearchVO);
            if(userCouponLogCount>0){
                userCouponSearchVO = new UserCouponSearchVO();
                userCouponSearchVO.setCreateTime(nowDate);
                userCouponSearchVO.setCouponId(pointId);
                userCouponSearchVO.setStart(0);
                userCouponSearchVO.setEnd(userCouponLogCount);
                userCouponSearchVO.setLastReleaseTime(searchTimes);
               // List<UserCouponLogEntity> list = couponsService.getUserCouponsLogListByTime(userCouponSearchVO);
                List<UserCouponLogEntity> list = releaseCouponsDAO.getReleaseUserCouponsLogListByTime(userCouponSearchVO);
                //可以释放的优惠券Id
                int workNum = (list.size() + 999)/1000;
                TradePointReleaseRun run = null;
                countDownLatch = new CountDownLatch(workNum);
                int count = 0;
                for(UserCouponLogEntity entry : list) {
                    if(count % 1000 == 0) {
                        if(run != null) {
                            pool.execute(run);
                            run = null;
                        }
                        run = new TradePointReleaseRun();
                        run.countDownLatch = countDownLatch;
                        run.releaseService = releaseService;
                    }
                    count++;
                    run.ls.add(entry);
                }
                if(run != null) {
                    pool.execute(run);
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException ex) {
                    logger.warn("", ex);
                }
            }
        }
    }


    public static class TradePointReleaseRun implements Runnable{

        private static Logger logger = LoggerFactory.getLogger(TradePointReleaseRun.class);

        public List<UserCouponLogEntity> ls = new ArrayList<UserCouponLogEntity>(1000);
        public CountDownLatch countDownLatch;
        public ReleaseServiceImpl releaseService;

        public void run() {
            try {
                logger.info("trade_popc_begin run ");
                long start = System.currentTimeMillis();
                List<TradePopcAndPointVO> tradePopcVOList = new ArrayList<TradePopcAndPointVO>(1000);
                for (UserCouponLogEntity data : ls) {
                    TradePopcAndPointVO tradePopcAndPointVO = new TradePopcAndPointVO();
                    tradePopcAndPointVO.setV_assetType("coupon");
                    tradePopcAndPointVO.setV_couponId(pointId);
                    tradePopcAndPointVO.setV_id(data.getId());
                    tradePopcVOList.add(tradePopcAndPointVO);

                }
                // sql
                releaseService.callTradePointRelease(tradePopcVOList);
                countDownLatch.countDown();
                logger.info("trade_point_begin_end run,size=" + ls.size() + ",time(ms)=" + (System.currentTimeMillis() - start));
            } catch (Exception e) {
                logger.error("trade_point_begin_Excepion==", e);
            }
        }
    }
    /**
     *  获取可以释放的优惠券Id
     */

    public HashSet<String> getCanReleasCouponSet(long  forMatDateTime, List<CouponsVO>  tbCouponList){
        //今天可以释放的优惠券Id
        HashSet<String> canReleasCouponSet = new HashSet<String>();
        //今天不可以释放的优惠券Id
        //HashSet<String> notReleaseCouponSet = new HashSet<>();
        //优惠劵对应的不可释放时间列表
        List<CouponTimeConfigVo>  couponTimeConfigVoList = couponsDAO.getCouponTimeConfigList();
        boolean bool =true;
        if(couponTimeConfigVoList!=null&&couponTimeConfigVoList.size()>0){
            for(CouponTimeConfigVo timeConfigVo:couponTimeConfigVoList){
                //System.out.println("timeConfigVo.getNotSendTime().getTime():"+timeConfigVo.getNotSendTime().getTime()+";forMatDateTime="+forMatDateTime);
                if(timeConfigVo.getNotSendTime().getTime()==forMatDateTime){

                    bool = false;
                    break;
                }
            }
        }
        if (bool) {
            for (CouponsVO couponsVO : tbCouponList) {
                if(couponsVO.getSendDays()>0&&couponsVO.getIsSend()==1) {
                    logger.info("couponsVO.getId():="+couponsVO.getId());
                    canReleasCouponSet.add(couponsVO.getId());
                }
            }
        }
        return canReleasCouponSet;
    }


    /**
     * @param logEntity
     * @param now
     * @param releaseNum
     */

    //添加或者修改玩家的释放游戏盾的统计表
    public UserCouponEntity  getUserCouponEntity(UserCouponLogEntity logEntity,Date now,BigDecimal releaseNum){

        UserCouponEntity userCouponEntity = new UserCouponEntity();
        userCouponEntity.setCdkeyNum(0);
        userCouponEntity.setCouponId(logEntity.getCouponid());
        userCouponEntity.setCouponNum(releaseNum);
        userCouponEntity.setCouponType(0);
        userCouponEntity.setCreateTime(now);
        userCouponEntity.setStatus("0");
        userCouponEntity.setId(IDUtil.getUUID());
        userCouponEntity.setUserId(logEntity.getUserid());
        userCouponEntity.setReservedCouponNum(new BigDecimal(0));

        return userCouponEntity;
    }


    //转换成想要的时间格式
    public Date getFormatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        String dateStr = format.format(date);
        try {
            Date dates = format.parse(dateStr);
            return dates;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //时间搓转时间
    public Date getNextFreshDateByLong(long times){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00");//这个是你要转成后的时间的格式
        //SimpleDateFormat sdf=new SimpleDateFormat("2018-07-20 00:00:00");//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(times))));   // 时
        try {
            Date dates = sdf.parse(sd);
            return dates;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ReleaseThread implements Runnable {
        private UserAssetService userAssetService;
        private UserAssetDAO userAssetDAO;
        private int startPage;
        BigDecimal userReleaseRate;
        private String currency;
        CountDownLatch countDownLatch;
        private Integer releaseTaskNumber;
        private String dayString;
        private BigDecimal profitReleaseRatio;
        private BigDecimal totalProfit;
        private BigDecimal totalReservedBalance;

        public ReleaseThread(String currency, BigDecimal totalProfit, String dayString, BigDecimal profitReleaseRatio,
                             Integer releaseTaskNumber, CountDownLatch countDownLatch,
                             int startPage, UserAssetService userAssetService,
                             UserAssetDAO userAssetDAO, BigDecimal userReleaseRate, BigDecimal totalReservedBalance) {
            this.currency = currency;
            this.dayString = dayString;
            this.releaseTaskNumber = releaseTaskNumber;
            this.startPage = startPage;
            this.userAssetDAO = userAssetDAO;
            this.userAssetService = userAssetService;
            this.userReleaseRate = userReleaseRate;
            this.countDownLatch = countDownLatch;
            this.profitReleaseRatio = profitReleaseRatio;
            this.totalProfit = totalProfit;
            this.totalReservedBalance = totalReservedBalance;
        }

        @Override
        public void run() {
            int pageNo = startPage;
            List<UserAssetEntity> assetList = userAssetDAO.getAssetToRelease(currency, new RowBounds(pageNo, 1000));

            while (!assetList.isEmpty()) {
                for (UserAssetEntity u : assetList) {
                    try {
                        userAssetService.releaseCoin(dayString, currency, totalProfit, profitReleaseRatio, u.getUserId(), u.getReservedBalance().multiply(userReleaseRate), totalReservedBalance, 1);
                    } catch (Throwable e) {
                        logger.error("failed to release coin to user: {}", u.getUserId(), e);
                    }
                }
                pageNo += releaseTaskNumber;
                assetList = userAssetDAO.getAssetToRelease(currency, new RowBounds(pageNo, 1000));
            }

            countDownLatch.countDown();
        }
    }

    public static void main(String args[]){
        Date now =new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00");//这个是你要转成后的时间的格式
       // 1532707200000
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf("1532707200000"))));   // 时
        //String sd = sdf.format(new Date(Long.parseLong(String.valueOf(now.getTime()))));   // 时
        try {
            Date dates = sdf.parse(sd);
           System.out.println(dates.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("1532275200000"+sd);
    }
}
