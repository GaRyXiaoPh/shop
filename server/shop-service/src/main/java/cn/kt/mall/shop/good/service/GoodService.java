package cn.kt.mall.shop.good.service;

import java.math.BigDecimal;
import java.util.*;

import cn.kt.mall.common.jwt.JWTRSAHelper;
import cn.kt.mall.common.user.dao.UserDAO;
import cn.kt.mall.common.user.entity.UserEntity;
import cn.kt.mall.shop.advertise.dao.AdDao;
import cn.kt.mall.shop.coupon.entity.CouponsEntity;
import cn.kt.mall.shop.coupon.mapper.CouponsDAO;
import cn.kt.mall.shop.good.mapper.*;
import cn.kt.mall.shop.good.vo.*;
import cn.kt.mall.shop.coupon.service.CouponService;
import cn.kt.mall.shop.good.entity.*;
import com.alibaba.fastjson.JSONArray;
import io.shardingjdbc.core.api.HintManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.common.SerialCodeGenerator;
import cn.kt.mall.common.constant.Constants;
import cn.kt.mall.common.http.vo.CommonPageVO;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.util.CommonUtil;
import cn.kt.mall.common.util.IDUtil;
import cn.kt.mall.shop.collect.mapper.CollectDAO;
import cn.kt.mall.shop.shop.entity.ShopEntity;
import cn.kt.mall.shop.shop.mapper.ShopDAO;

@Service
public class GoodService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private GoodDAO goodDAO;
    @Autowired
    private CollectDAO collectDAO;
    @Autowired
    private GoodPropertyDAO propertyDAO;
    @Autowired
    private ShopDAO shopDAO;
    @Autowired
    CouponService couponService;
    @Autowired
    private GoodCouponCenterDao goodCouponCenterDao;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private JWTRSAHelper jwtHelper;
    @Autowired
    private CouponsDAO couponsDAO;
    @Autowired
    private GoodShopCenterDao goodShopCenterDao;
    @Autowired
    private GoodPropertyDAO goodPropertyDAO;
    @Autowired
    private AdDao adDao;
    @Autowired
    private ShopGoodTypeDAO shopGoodTypeDAO;

    @Autowired
    RedisTemplate redisTemplate;

    // 获取商品ID查询商品信息
    public GoodVO getGoodByGoodId(String goodId, String userId, String shopId, String status) {
        GoodVO goodVO = new GoodVO();
        GoodEntity goodBase = goodDAO.getGoodEntityByGoodIdAndShopId(goodId, shopId);
        A.check(goodBase == null || status == null ? false : !goodBase.getStatus().equals(status), "商品无效或不存在");
        UserEntity userEntity = userDAO.getById(userId);
        String myShopId = shopDAO.getShopIdByUserId(userId);
        if (userEntity != null) {
            ShopEntity shop = shopDAO.getShopEntityByShopId(goodBase.getShopShopId());
            if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3") || userEntity.getLevel().equals("4")) {//准站长，站长中心主任登陆
                if (myShopId != null) {
                    if (shopId.equals(myShopId)) {
                        goodBase.setPidFlag(1);
                    }
                }
            } else if (shop.getUserId().equalsIgnoreCase(userEntity.getPid())) {//会员登陆
                goodBase.setPidFlag(1);
            }
        }
        //优惠券列表
        List<GoodCouponCenterEntity> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByGoodId(goodId);
        //支付方式列表
        List<GoodPayReqVO> goodPayEntityList = goodDAO.getGoodPayByGoodIdList(goodId);
        goodBase.setGoodCouponCenterEntityList(goodCouponCenterEntityList);
        goodBase.setGoodPayEntityList(goodPayEntityList);
        goodVO.setShopName(goodBase.getShopName());
        BeanUtils.copyProperties(goodBase, goodVO);
        // 构建分类字段值
        buildGoodType(goodVO);
        // 设置属性字段
        goodVO.setGoodPropertys(propertyDAO.getGoodProperty(goodId));
        // 设置图片信息
        List<GoodImgEntity> imgList = propertyDAO.getGoodImg(goodId);
        buildGoodImg(imgList, goodVO);
        // 判断改用户是否收藏该商品
        if (StringUtils.isEmpty(userId) || collectDAO.getGoodCollectEntity(userId, goodId) == null) {
            goodVO.setCollectType(Constants.DEFAULT_0);
        } else {
            goodVO.setCollectType(Constants.DEFAULT_1);
        }
        return goodVO;
    }

    private void buildGoodImg(List<GoodImgEntity> imgList, GoodVO goodVO) {
        List<GoodImgEntity> imgGoodList = new ArrayList<>();
        List<GoodImgEntity> imgDetailsList = new ArrayList<>();
        for (GoodImgEntity goodImgEntity : imgList) {
            if (goodImgEntity.getType() == 1) {
                imgGoodList.add(goodImgEntity);
            } else if (goodImgEntity.getType() == 2) {
                imgDetailsList.add(goodImgEntity);
            }
        }
        goodVO.setGoodImgs(imgGoodList);
        goodVO.setGoodDetails(imgDetailsList);
    }

    // 商家端管理商品查看详情接口
    public GoodVO managerGoodByGoodId(String shopId, String goodId) {
        GoodVO goodVO = new GoodVO();
        GoodEntity goodBase = goodDAO.getGoodEntityByGoodId(goodId);
        A.check(goodBase == null || !goodBase.getShopId().equals(shopId), "商品无效或不存在");
        BeanUtils.copyProperties(goodBase, goodVO);
        // 构建分类字段值
        buildGoodType(goodVO);
        // 设置属性字段
        goodVO.setGoodPropertys(propertyDAO.getGoodProperty(goodId));
        // 设置图片信息
        List<GoodImgEntity> imgList = propertyDAO.getGoodImg(goodId);
        buildGoodImg(imgList, goodVO);
        return goodVO;
    }

    /**
     * 构建商品分类详情
     *
     * @param goodVO
     */
    private void buildGoodType(GoodVO goodVO) {
        List<String> ids = new ArrayList<>();
        ids.add(goodVO.getFirstGoodType());
        ids.add(goodVO.getSecondGoodType());
        ids.add(goodVO.getThirdGoodType());
        List<GoodTypeEntity> listType = propertyDAO.listByIds(ids);
        if (!CollectionUtils.isEmpty(listType)) {
            for (GoodTypeEntity goodTypeEntity : listType) {
                if (goodTypeEntity.getId().equals(goodVO.getFirstGoodType())) {
                    goodVO.setFirstGoodTypeValue(goodTypeEntity.getName());
                    continue;
                } else if (goodTypeEntity.getId().equals(goodVO.getSecondGoodType())) {
                    goodVO.setSecondGoodTypeValue(goodTypeEntity.getName());
                    continue;
                } else if (goodTypeEntity.getId().equals(goodVO.getThirdGoodType())) {
                    goodVO.setThirdGoodTypeValue(goodTypeEntity.getName());
                    continue;
                }
            }
        }
    }

    // 获取商品分类
    public List<GoodTypeEntity> getGoodType(String parentId) {
        // 空获取顶级''
        if (parentId == null) {
            parentId = new String();
        }
        return propertyDAO.getGoodType(parentId);
    }

    // 获取所有商品分类
    public List<GoodTypeEntity> listAllType() {
        return shopGoodTypeDAO.listAllType();
    }

    // 商品搜索，like 名称 && 商品类型
    public CommonPageVO<GoodRespVO> searchGoods(String shopId, String goodName, String goodType,
                                                String payTypes,
                                                int searchMode, Date startTime, Date endTime,
                                                int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<GoodRespVO> list = goodDAO.searchGoods(shopId, goodName, goodType, payTypes, searchMode, startTime, endTime,
                rowBounds);
        for (GoodRespVO goodRespVO : list) {
            List<GoodCouponCenterReqVO> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByShopIdAndGoodId(goodRespVO.getShopId(), goodRespVO.getId());
            if (goodCouponCenterEntityList.size() > 0) {
                goodRespVO.setCouponEntityList(goodCouponCenterEntityList);
            }
        }

        PageInfo<GoodRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    // web前台商品搜索，like 名称 && 商品类型
    public CommonPageVO<GoodRespVO> searchGoodsFront(String shopId, String userId, String goodName, String goodType, String status,
                                                     String searchMode, Date startTime, Date endTime, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<GoodRespVO> list = new ArrayList<>();
        if (userId != null) {
            UserEntity userEntity = userDAO.getById(userId);
            A.check(userEntity == null, "用户不存在:");
            if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3") || userEntity.getLevel().equals("4")) {//准站长，站长，中心主任登陆时自己的商品显示Plus标记
                shopId = shopDAO.getShopIdByUserId(userId);
                list = goodDAO.searchGoodsFront(shopId, goodName, goodType, status, searchMode, startTime, endTime, rowBounds);
            } else {//会员登陆
                shopId = shopDAO.getShopIdByUserId(userEntity.getPid());
                list = goodDAO.searchGoodsFront(shopId, goodName, goodType, status, searchMode, startTime, endTime, rowBounds);
            }

        } else {
            list = goodDAO.searchGoodsFront(shopId, goodName, goodType, status, searchMode, startTime, endTime, rowBounds);
        }
        for (GoodRespVO goodRespVO : list) {
            List<GoodCouponCenterReqVO> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByShopIdAndGoodId(goodRespVO.getShopId(), goodRespVO.getId());
            if (goodCouponCenterEntityList.size() > 0) {
                goodRespVO.setCouponEntityList(goodCouponCenterEntityList);
            }
        }

        PageInfo<GoodRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }


    // 商品分类列表
    public PageVO<GoodRespVO> searchGoodsByType(String goodType, String status, String userId, int pageNo, int pageSize) {
        //RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<GoodRespVO> list = new ArrayList<>();
        String shopId = "";
        List<GoodRespVO> listByGoodType = new ArrayList<>();
        int count  = 0;//总页数
  /*      if (pageNo > 1) {
            //TODO: 第一页之后的数据直接返回空, 看结果
            logger.info("return empty list");
            PageInfo<GoodRespVO> pageInfo = new PageInfo<>(list);
            pageInfo.setPageNum(pageNo);
            return CommonUtil.copyFromPageInfo(pageInfo, list);
        }*/

        RedisSerializer<Object> deserialize = new JdkSerializationRedisSerializer();
  /*      if (userId != null) {
            UserEntity userEntity = userDAO.getById(userId);
            A.check(userEntity == null, "用户不存在:");
            if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3") || userEntity.getLevel().equals("4")) {//准站长，站长，中心主任登陆时自己的商品显示Plus标记
                shopId = shopDAO.getShopIdByUserId(userId);
                if(pageNo == 1 ){
                    if(goodType == null || goodType == ""){
                        if(shopId == null){
                            object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home","noShop")) ;
                            list = ( List<GoodRespVO>) object;
                        }else{
                            object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home",shopId)) ;
                            list = ( List<GoodRespVO>) object;
                        }
                    }else{
                        //object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get(goodType,shopId)) ;
                        //list = ( List<GoodRespVO>) object;
                        list = goodDAO.searchGoodsByType(shopId, goodType, status, rowBounds);
                    }
                }else{
                    //list =  goodDAO.initSearchGoodsStartFifty(shopId ,goodType,rowBounds);
                    list = goodDAO.searchGoodsByType(shopId, goodType, status, rowBounds);
                }
            } else {//会员登陆
                shopId = shopDAO.getShopIdByUserId(userEntity.getPid());
                if(pageNo == 1 ){
                    if(goodType == null || goodType == ""){
                        if(shopId == null){
                            object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home","noShop")) ;
                            list = ( List<GoodRespVO>) object;
                        }else{
                            object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home",shopId)) ;
                            list = ( List<GoodRespVO>) object;
                        }
                    } else{
                        //object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get(goodType,shopId)) ;
                        //list = ( List<GoodRespVO>) object;
                        list = goodDAO.searchGoodsByType(shopId, goodType, status, rowBounds);
                    }
                }else{
                    //list =  goodDAO.initSearchGoodsStartFifty(shopId ,goodType,rowBounds);
                    list = goodDAO.searchGoodsByType(shopId, goodType, status, rowBounds);
                }
            }

        } else {
            if(pageNo == 1){
                if(goodType == null || goodType == ""){
                    object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home","noShop")) ;
                    list = ( List<GoodRespVO>) object;
                }else{
                    //object = deserialize.deserialize((byte[])redisTemplate.opsForHash().get(goodType,shopId)) ;
                    //list = ( List<GoodRespVO>) object;
                    list = goodDAO.searchGoodsByType(shopId, goodType, status, rowBounds);
                }
            }else{
                //list =  goodDAO.initSearchGoodsStartFifty(shopId ,goodType,rowBounds);
                list = goodDAO.searchGoodsByType(shopId, goodType, status, rowBounds);
            }
        }*/
        ShopEntity shopEntity = new ShopEntity();
        if (userId != null) { //登陆
            UserEntity userEntity = userDAO.getById(userId);
            A.check(userEntity == null, "用户不存在:");
            if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3") || userEntity.getLevel().equals("4")) {//准站长，站长，中心主任登陆时自己的商品显示Plus标记
                //shopId = shopDAO.getShopIdByUserId(userId);
                shopEntity = shopDAO.getShopByUserId(userId);
            } else {//会员登陆
                //shopId = shopDAO.getShopIdByUserId(userEntity.getPid());
                shopEntity = shopDAO.getShopByUserId(userEntity.getPid());
            }
        }
        if(shopEntity == null){
            list = ( List<GoodRespVO>)deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home","noShop")) ;
        }else{
            if(shopEntity.getId() == null || shopEntity.getId() == "" || shopEntity.getStatus().equals("3")){
                list = ( List<GoodRespVO>)deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home","noShop")) ;
            }else{
                list = ( List<GoodRespVO>) deserialize.deserialize((byte[])redisTemplate.opsForHash().get("home",shopEntity.getId())) ;
            }
        }

        if(goodType != null && goodType != ""){
            if(list != null){
                for( GoodRespVO goodRespVO : list){
                    if(goodRespVO.getFirstGoodType().equals(goodType)){
                        listByGoodType.add(goodRespVO);
                    }
                }
            }
            list = listByGoodType;
        }
        if(list != null){
            count = list.size();
        }
        /*if(list != null && pageNo == 1 && goodType == null && list.size() > 0 ){
            int start = (pageNo - 1) * pageSize;
            if (start > list.size() - 1) {
                start = list.size() - 1;
            }
            int end = start +  pageSize;
            if(end > list.size()){
                end = list.size();
            }
            list = list.subList(start, end);
        }*/
        if(list != null  && list.size() > 0 ){
            int start = (pageNo - 1) * pageSize;
            if (start > list.size() - 1) {
                start = list.size();
            }
            int end = start +  pageSize;
            if(end > list.size()){
                end = list.size();
            }
            list = list.subList(start, end);
        }
        if(list == null){
            list = new ArrayList<>();
        }
/*        List<GoodRespVO> list = goodDAO.searchGoodsByType(goodType, status, rowBounds);
        if (userId != null) {
            UserEntity userEntity = userDAO.getById(userId);
            for (GoodRespVO goodRespVO : list) {
                if (userEntity.getPid().equals(goodRespVO.getUserId())) {
                    goodRespVO.setPidFlag(1);
                }
            }
            Collections.sort(list, new Comparator<GoodRespVO>() {
                public int compare(GoodRespVO o1, GoodRespVO o2) {
                    if (o1.getPidFlag() < o2.getPidFlag()) {
                        return 1;
                    }
                    if (o1.getPidFlag() == o2.getPidFlag()) {
                        return 0;
                    }
                    return -1;
                }
            });
        }*/
       /* for (GoodRespVO goodRespVO : list) {
            List<GoodCouponCenterReqVO> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByShopIdAndGoodId(goodRespVO.getShopId(), goodRespVO.getId());
            if (goodCouponCenterEntityList.size() > 0) {
                goodRespVO.setCouponEntityList(goodCouponCenterEntityList);
            }
        }*/
        //PageInfo<GoodRespVO> pageInfo = new PageInfo<>(list);
        //pageInfo.setPageNum(pageNo);
        //return CommonUtil.copyFromPageInfo(pageInfo, list);
        return new PageVO<GoodRespVO>(pageNo, pageSize, count, list);
    }

    // 首页轮播图
    public List<AdResVO> getAdResVOList() {
        return adDao.getAdResVOList();
    }

    // 获取店铺所有商品
    public PageVO<GoodEntity> getGoodByShopId(String shopId, String goodType, int pageNo, int pageSize, String userId, String sort) {
        int srcPageNo = pageNo;
        if (pageNo >= 1)
            pageNo = pageNo - 1;
        int offset = pageNo * pageSize;
        UserEntity userEntity = userDAO.getById(userId);
        int count = goodDAO.getGoodByShopIdCount(shopId);
        String pidShopId = shopDAO.getShopIdByUserId(userEntity.getPid());
        String myShopId = shopDAO.getShopIdByUserId(userId);
        List<GoodEntity> list = goodDAO.getGoodByShopId(shopId, goodType, sort, offset, pageSize);
        if (userEntity.getLevel().equals("2") || userEntity.getLevel().equals("3") || userEntity.getLevel().equals("4")) {//准站长，站长中心主任登陆
            if (shopId.equals(myShopId)) {
                for (GoodEntity goodEntity : list) {
                    goodEntity.setPidFlag(1);
                }
            }

        } else if (pidShopId != null && pidShopId.equals(shopId)) {//会员登陆
            for (GoodEntity goodEntity : list) {
                goodEntity.setPidFlag(1);
            }
        } else {
            for (GoodEntity goodEntity : list) {
                goodEntity.setPidFlag(0);
            }
        }
        return new PageVO<GoodEntity>(srcPageNo, pageSize, count, list);
    }


    /**
     * 修改商品状态
     *
     * @param shopId
     * @param goodsId
     * @param currentStatus
     * @param targetStatus
     */
    public void modifyStatus(String shopId, String goodsId, String currentStatus, String targetStatus, Date auditTime) {
        int rows = goodDAO.modifyStatus(shopId, goodsId, currentStatus, targetStatus, auditTime);
        A.check(rows != 1, "操作异常");
    }

    /**
     * 商品删除
     *
     * @param
     * @param goodsId
     */
    public void delGoods(String shopId, String goodsId) {
        GoodEntity goodEntity = goodDAO.getGoodEntityByGoodId(goodsId);
        A.check(goodEntity == null || !goodEntity.getShopId().equals(shopId), "商品不存在");
        int rows = goodDAO.delGoods(goodsId);
        A.check(rows != 1, "商品删除异常");
    }

 /*   @Transactional
    public String addGoods(String shopId, GoodReqVO goodReqVO) {
        ShopEntity shopEntity = shopDAO.getShopEntityByShopId(shopId);
        A.check(shopEntity == null, "获取店铺信息出错");
        GoodEntity goodEntity = new GoodEntity();
        BeanUtils.copyProperties(goodReqVO, goodEntity);
        int rows = 0;
        goodEntity.setShopId(shopId);
        goodEntity.setUserId(shopEntity.getUserId());
//        goodEntity.setStatus(cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_APPLY);

        *//*if (!CollectionUtils.isEmpty(goodReqVO.getCouponList())) {

            for (int i = 0; i < goodReqVO.getCouponList().size(); i++) {
                //验证优惠卷是否存在
            }
            //如果都存在，那么添加商品对应优惠卷表中

        }*//*

        if (!CollectionUtils.isEmpty(goodReqVO.getGoodImgs())) {
            goodEntity.setGoodImg(goodReqVO.getGoodImgs().get(0));
        }
        if (StringUtils.isEmpty(goodReqVO.getId())) {
            goodEntity.setId(IDUtil.getUUID());
            goodEntity.setGoodNo(SerialCodeGenerator.getNext());
            rows = goodDAO.addGoodSelective(goodEntity);
        } else {
            rows = goodDAO.updateGoodSelective(goodEntity);
            propertyDAO.delGoodImg(goodReqVO.getId());
            propertyDAO.delGoodProperty(goodReqVO.getId());
        }
        List<GoodImgEntity> imgList = new ArrayList<>();
        for (String img : goodReqVO.getGoodImgs()) {
            GoodImgEntity goodImgEntity = new GoodImgEntity(IDUtil.getUUID(), goodEntity.getId(), img, (short) 1);
            imgList.add(goodImgEntity);
        }
        for (String img : goodReqVO.getGoodDetails()) {
            GoodImgEntity goodImgEntity = new GoodImgEntity(IDUtil.getUUID(), goodEntity.getId(), img, (short) 2);
            imgList.add(goodImgEntity);
        }
        if (!CollectionUtils.isEmpty(imgList)) {
            propertyDAO.addGoodImg(imgList);
        }
        List<GoodPropertyEntity> propertyList = new ArrayList<>();
        for (GoodPropertyEntity goodPropertyEntity : goodReqVO.getGoodPropertys()) {
            goodPropertyEntity.setId(IDUtil.getUUID());
            goodPropertyEntity.setGoodId(goodEntity.getId());
            propertyList.add(goodPropertyEntity);
        }
        if (!CollectionUtils.isEmpty(propertyList)) {
            propertyDAO.addGoodProperty(propertyList);
        }


        A.check(rows != 1, "编辑商品失败");
        return goodEntity.getId();
    }*/

    public void addGoodType(GoodTypeEntity goodTypeEntity) {
        if (!StringUtils.isBlank(goodTypeEntity.getParentId())) {
            GoodTypeEntity parent = propertyDAO.getGoodTypeById(goodTypeEntity.getParentId());
            A.check(parent == null, "父级元素不存在");
            if (!StringUtils.isBlank(parent.getParentId())) {
                A.check(parent == null, "最多添加三级分类");
            }
        }
        int rows = propertyDAO.addGoodType(goodTypeEntity);
        A.check(rows != 1, "保存失败");
    }

    public void delGoodType(String typeId) {
        int rows = propertyDAO.delGoodType(typeId);
        A.check(rows != 1, "删除失败");
    }

    public void updateGoodType(GoodTypeEntity goodTypeEntity) {
        int rows = propertyDAO.updateGoodType(goodTypeEntity);
        A.check(rows != 1, "修改失败");
    }


    /********************以下为Admin后台所用接口****************************/
    // 商品搜索，like 名称 && 商品类型
    public CommonPageVO<GoodRespVO> searchGoodss(String shopId, String goodName, String goodType,
                                                 String status, int searchMode, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<GoodRespVO> list = goodDAO.searchGoodss(shopId, goodName, goodType, status, searchMode, rowBounds);
        for (GoodRespVO goodRespVO : list) {
            //查询商品关联的优惠券
            List<GoodCouponCenterReqVO> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByShopIdAndGoodId(goodRespVO.getShopId(), goodRespVO.getId());
            if (goodCouponCenterEntityList.size() > 0) {
                goodRespVO.setCouponEntityList(goodCouponCenterEntityList);
            }
            //支付类型信息
            List<GoodPayReqVO> goodPayList = goodDAO.getGoodPayByGoodIdList(goodRespVO.getId());
            if(goodPayList != null && goodPayList.size() > 1){
                for (GoodPayReqVO goodPayReqVO : goodPayList) {
                    if(!goodPayReqVO.getPayType().equals("1")){
                        goodRespVO.setPayRatio("信用金:" + goodPayReqVO.getBalanceRatio().setScale(2) + "% " + "优惠券支付:" + goodPayReqVO.getOtherRatio().setScale(2) + "%");
                    }
                }
            }
            goodRespVO.setGoodPayList(goodPayList);

            /*List<GoodPayReqVO> goodPayList = goodDAO.getGoodPayByGoodId(goodRespVO.getId());
            if (goodPayList != null && goodPayList.size() > 0) {
                GoodPayReqVO reqVO = new GoodPayReqVO();
                if (goodPayList.size() == 1) {
                    reqVO.setPayType("余额");
                } else {
                    reqVO.setPayType("余额+优惠券");
                }
                List<GoodPayReqVO> goodPay = new ArrayList<GoodPayReqVO>();
                goodPay.add(reqVO);
                goodRespVO.setGoodPayList(goodPay);
            }*/
        }
        //商品分类信息
        List<GoodTypeEntity> listClassify = propertyDAO.listAllType();
        Map<String, String> map = new HashMap<String, String>();
        for (GoodTypeEntity goodTypeEntity : listClassify) {
            map.put(goodTypeEntity.getId(), goodTypeEntity.getName());
        }
        for (GoodRespVO goodRespVO : list) {
            goodRespVO.setFirstGoodType(map.get(goodRespVO.getFirstGoodType()));
        }
        PageInfo<GoodRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    /**
     * 商品删除
     *
     * @param
     * @param goodsIds
     */
    @Transactional
    public void delGoodss(String goodsIds) {
        String[] goodsId = goodsIds.split(",");
        for (int i = 0; i < goodsId.length; i++) {
            GoodEntity goodEntity = goodDAO.getGoodEntityByGoodId(goodsId[i]);
            logger.error("good: {} doesn't not exist, ignore it", goodsId[i]);
            int rows = goodDAO.delGoods(goodsId[i]);
            A.check(rows != 1, "商品删除异常");
        }
        //批量删除商品跟店铺中间表
        int count = goodShopCenterDao.batchGoodStatus(Arrays.asList(goodsId), "4");
        A.check(count <= 0, "商品删除失败");
    }

    /**
     * 修改平台商品库存
     *
     * @param goodId     所修改库存的商品的ID
     * @param stockDelta 所修改库存的商品的数量
     */
    public void updateKtStock(String goodId, Integer stockDelta) {
        int rows = 0;
        GoodEntity good = goodDAO.getGoodEntityByGoodId(goodId);
        A.check(good == null, "商品不存在:" + goodId);
        if (stockDelta > 0) {
            rows = goodDAO.addStock(goodId, stockDelta);
        } else rows = goodDAO.reduceStock(goodId, 0 - stockDelta);

        A.check(rows == 0, "更新库存失败");
    }

    /**
     * 新增、修改商品
     */
    @Transactional
    public String addGoodss(GoodReqVO goodReqVO) {
        GoodEntity goodEntity = new GoodEntity();
        BeanUtils.copyProperties(goodReqVO, goodEntity);
        int rows = 0;

//        goodEntity.setStatus(cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_APPLY);
        A.check(StringUtils.isBlank(goodReqVO.getGoodImgs()), "头图不能为空");
        if (!StringUtils.isBlank(goodReqVO.getGoodImgs())) {
            goodEntity.setGoodImg(goodReqVO.getGoodImgs().split(",")[0]);
        }
        String goodId = "";
        if (StringUtils.isEmpty(goodReqVO.getId())) {

            // 锁店铺表
            shopDAO.shopForUpdate();

            goodId = IDUtil.getUUID();
            //平台商品所属的门店id固定是kt
            goodEntity.setShopId("kt");
            goodEntity.setId(goodId);
            goodEntity.setUserId(" ");
            goodEntity.setSecondGoodType(" ");
            goodEntity.setThirdGoodType(" ");
            goodEntity.setFreightFree(new BigDecimal(0));
            goodEntity.setStatus(cn.kt.mall.shop.good.constant.Constants.GoodStatus.GOOD_DOWN);
            goodEntity.setGoodNo(SerialCodeGenerator.getNext());
            //goodEntity.setUnit(" ");
            rows = goodDAO.addGoodSelective(goodEntity);
            //查询所有店铺
            List<String> shopList = shopDAO.getShopsList();
            if (shopList != null && shopList.size() > 0) {
                List<GoodShopCenterEntity> goodShopCenter = new ArrayList<>();
                for (String shopId : shopList) {
                    GoodShopCenterEntity entity = new GoodShopCenterEntity();
                    entity.setId(UUID.randomUUID().toString());
                    entity.setShopId(shopId);
                    entity.setGoodId(goodId);
                    entity.setGoodStatus("3");
                    goodShopCenter.add(entity);
                }
                int count = goodShopCenterDao.insertByBatchGoodShopCenter(goodShopCenter);
                A.check(count != shopList.size(), "添加商品失败");
            }
        } else {
            goodId = goodEntity.getId();
            rows = goodDAO.updateGoodSelective(goodEntity);
            propertyDAO.delGoodImg(goodReqVO.getId());
            propertyDAO.delGoodProperty(goodReqVO.getId());
            //删除商品优惠券相关信息
            couponsDAO.delGoodCouponCenter(goodReqVO.getId());
            //删除商品支付类型相关信息
            goodDAO.delGoodPay(goodReqVO.getId());
        }
        //添加支付类型信息
        A.check(StringUtils.isBlank(goodReqVO.getPayIds()), "支付方式不能为空");
        addGoodPays(goodId, goodReqVO.getPayIds());

        //添加优惠券信息
        //A.check(StringUtils.isBlank(goodReqVO.getCouponIdList()), "优惠券列表不能为空");
        //A.check(goodReqVO.getCouponNum() == null || goodReqVO.getCouponNum().compareTo(new BigDecimal(0)) <= 0, "可选的优惠券数量不能为空");
        if(StringUtils.isNotBlank(goodReqVO.getCouponIdList())){
            addGoodCouponCenters(goodId, goodReqVO.getCouponIdList(), goodReqVO.getCouponNum(), "kt");
        }
        //添加图片
        List<GoodImgEntity> imgList = new ArrayList<>();
        String[] imgs = goodReqVO.getGoodImgs().split(",");
        for (String img : imgs) {
            GoodImgEntity goodImgEntity = new GoodImgEntity(IDUtil.getUUID(), goodEntity.getId(), img, (short) 1);
            imgList.add(goodImgEntity);
        }
        String[] imgss = goodReqVO.getGoodDetails().split(",");
        for (String img : imgss) {
            GoodImgEntity goodImgEntity = new GoodImgEntity(IDUtil.getUUID(), goodEntity.getId(), img, (short) 2);
            imgList.add(goodImgEntity);
        }
        if (!CollectionUtils.isEmpty(imgList)) {
            propertyDAO.addGoodImg(imgList);
        }
        //本项目无商品属性，以下代码注释掉
       /* List<GoodPropertyEntity> propertyList = new ArrayList<>();
        for (GoodPropertyEntity goodPropertyEntity : goodReqVO.getGoodPropertys()) {
            goodPropertyEntity.setId(IDUtil.getUUID());
            goodPropertyEntity.setGoodId(goodEntity.getId());
            propertyList.add(goodPropertyEntity);
        }
        if (!CollectionUtils.isEmpty(propertyList)) {
            propertyDAO.addGoodProperty(propertyList);
        }*/
        A.check(rows != 1, "编辑商品失败");
        return goodEntity.getId();
    }

    //添加商品支付类型
    public void addGoodPays(String goodId, String payIds) {
        int count = 0;
        String[] payId = payIds.split(",");
        for (String id : payId) {
            GoodPayVO goodPayVO = new GoodPayVO();
            goodPayVO.setId(IDUtil.getUUID().toString());
            goodPayVO.setGoodId(goodId);
            goodPayVO.setCreateTime(new Date());
            goodPayVO.setGoodPayId(id);
            count = goodDAO.addGoodPay(goodPayVO);
            A.check(count != 1, "添加支付类型出错");
        }

    }

    //建立商品与优惠券的联系
    public void addGoodCouponCenters(String goodId, String couponIds, BigDecimal couponNum, String shopid) {

        if(couponIds != null && !couponIds.equals("")){
            String[] ids = couponIds.split(",");
            //A.check(BigDecimal.valueOf((int) ids.length).compareTo(couponNum) < 0, "可用优惠券数不能大于选择的优惠券种类数");
            for (String id : ids) {
                //验证优惠卷是否存在
                CouponsEntity couponsEntity = couponsDAO.getCouponsByKey(id);
                A.check(couponsEntity == null, "获取优惠券信息出错");

                GoodCouponCenterEntity goodCouponCenterEntity = new GoodCouponCenterEntity();
                goodCouponCenterEntity.setId(IDUtil.getUUID());
                goodCouponCenterEntity.setCreateTime(new Date());
                goodCouponCenterEntity.setCouponId(id);
                goodCouponCenterEntity.setGoodId(goodId);
                goodCouponCenterEntity.setCouponNum(couponNum);
                goodCouponCenterEntity.setShopId("kt");
                int count = couponsDAO.addGoodCouponCenter(goodCouponCenterEntity);
                A.check(count != 1, "添加优惠券信息出错");
            }
        }
    }

    @Transactional
    public void addGoodsToShop(String shopId, List<GoodEntity> goods) {

//        //插入中间表关联店铺与商品得关系
//        for (GoodEntity g : goods) {
//            GoodShopCenterEntity goodShopCenterEntity = new GoodShopCenterEntity();
//            goodShopCenterEntity.setId(IDUtil.getUUID());
//            goodShopCenterEntity.setGoodId(g.getId());
//            goodShopCenterEntity.setShopId(shopId);
//            goodShopCenterEntity.setGoodStatus(g.getStatus());
//            int count = goodShopCenterDao.insertGoodShopCenter(goodShopCenterEntity);
//            A.check(count != 1, "店铺添加商品失败");
//        }

        List<GoodShopCenterEntity> centerList = new ArrayList<>();
        for (GoodEntity entity : goods) {
            GoodShopCenterEntity goodShopCenterEntity = new GoodShopCenterEntity();
            goodShopCenterEntity.setId(IDUtil.getUUID());
            goodShopCenterEntity.setGoodId(entity.getId());
            goodShopCenterEntity.setShopId(shopId);
            goodShopCenterEntity.setGoodStatus(entity.getStatus());
            centerList.add(goodShopCenterEntity);
        }
        int count = goodShopCenterDao.insertByBatchGoodShopCenter(centerList);
        System.out.println("-------------------------------" + count + "----------------------------------------");
//        for (GoodEntity g : goods) {
//            GoodEntity oldEntity = goodDAO.getGoodEntityByShopIdAndGoodNo(shopId, g.getGoodNo());
//            String newGooid = "";
//            if (oldEntity == null) {
//                GoodEntity newEntity = new GoodEntity();
//                BeanUtils.copyProperties(g, newEntity);
//                newEntity.setShopId(shopId);
//                newEntity.setId(IDUtil.getUUID());
//                newEntity.setUserId(null);
//                //商家的库存，默认是0，必须手工调整库存
//                newEntity.setGoodStock(0);
//                newGooid = newEntity.getId();
//                goodDAO.addGoodSelective(newEntity);
//                //克隆优惠券
//                List<GoodCouponCenterEntity> oldCoupon = goodCouponCenterDao.getGoodCollectEntityByGoodId(g.getId());
//                for (GoodCouponCenterEntity couponCenterEntity : oldCoupon) {
//                    //验证优惠券是否存在
//                    CouponsEntity couponsEntity = couponsDAO.getCouponsByKey(couponCenterEntity.getCouponId());
//                    A.check(couponsEntity == null, "获取优惠券信息出错");
//                    GoodCouponCenterEntity goodCouponCenterEntity = new GoodCouponCenterEntity();
//                    goodCouponCenterEntity.setId(IDUtil.getUUID());
//                    goodCouponCenterEntity.setCreateTime(new Date());
//                    goodCouponCenterEntity.setCouponId(couponsEntity.getId());
//                    goodCouponCenterEntity.setGoodId(newGooid);
//                    goodCouponCenterEntity.setCouponNum(couponCenterEntity.getCouponNum());
//                    goodCouponCenterEntity.setShopId(shopId);
//                    int count = couponsDAO.addGoodCouponCenter(goodCouponCenterEntity);
//                    A.check(count != 1, "添加优惠券信息出错");
//                }
//                //克隆商品支付方式
//                List<GoodPayReqVO> oldPlay = goodDAO.getGoodPayByGoodId(g.getId());
//                A.check(oldPlay.isEmpty(), "商品支付方式不存在");
//                for (GoodPayReqVO vo : oldPlay) {
//                    GoodPayVO pay = new GoodPayVO();
//                    pay.setId(UUID.randomUUID().toString());
//                    pay.setGoodId(newGooid);
//                    pay.setCreateTime(new Date());
//                    pay.setPrice(vo.getPrice());
//                    pay.setPayType(vo.getPayType());
//                    int rows = goodDAO.addGoodPay(pay);
//                    A.check(rows != 1, "添加商品支付方式出错");
//                }
//                //克隆商品详情轮播图
//                List<GoodImgEntity> goodImgEntities = propertyDAO.getGoodImg(g.getId());
//                for (GoodImgEntity entity : goodImgEntities) {
//                    GoodImgEntity goodImgEntity = new GoodImgEntity();
//                    BeanUtils.copyProperties(entity, goodImgEntity);
//                    goodImgEntity.setId(IDUtil.getUUID());
//                    goodImgEntity.setGoodId(newGooid);
//                    int rows = propertyDAO.addGoodImgs(goodImgEntity);
//                    A.check(rows != 1, "添加商品失败");
//                }
//
//            } else {
//                //如果存在更新商品删除标识为0
//                oldEntity.setDelFlag((short) 0L);
//                int count = goodDAO.updateGoodSelective(oldEntity);
//                A.check(count != 1, "添加失败");
//                List<GoodCouponCenterEntity> oldCoupon = goodCouponCenterDao.getGoodCollectEntityByGoodId(g.getId());
//                if (oldCoupon == null) {
//                    for (GoodCouponCenterEntity couponCenterEntity : oldCoupon) {
//                        //验证优惠券是否存在
//                        CouponsEntity couponsEntity = couponsDAO.getCouponsByKey(couponCenterEntity.getCouponId());
//                        A.check(couponsEntity == null, "获取优惠券信息出错");
//                        GoodCouponCenterEntity goodCouponCenterEntity = new GoodCouponCenterEntity();
//                        goodCouponCenterEntity.setId(IDUtil.getUUID());
//                        goodCouponCenterEntity.setCreateTime(new Date());
//                        goodCouponCenterEntity.setCouponId(couponsEntity.getId());
//                        goodCouponCenterEntity.setGoodId(newGooid);
//                        goodCouponCenterEntity.setCouponNum(couponCenterEntity.getCouponNum());
//                        goodCouponCenterEntity.setShopId(shopId);
//                        int couponCount = couponsDAO.addGoodCouponCenter(goodCouponCenterEntity);
//                        A.check(couponCount != 1, "添加优惠券信息出错");
//                    }
//                }
//
//                //克隆商品详情轮播图
//                List<GoodImgEntity> goodImgEntities = propertyDAO.getGoodImg(g.getId());
//                if (goodImgEntities.isEmpty()) {
//                    for (GoodImgEntity entity : goodImgEntities) {
//                        GoodImgEntity goodImgEntity = new GoodImgEntity();
//                        BeanUtils.copyProperties(entity, goodImgEntity);
//                        goodImgEntity.setId(IDUtil.getUUID());
//                        goodImgEntity.setGoodId(newGooid);
//                        int rows = propertyDAO.addGoodImgs(goodImgEntity);
//                        A.check(rows != 1, "添加商品失败");
//                    }
//                }
//
//
//            }
//
//
//        }


    }

    /**
     * 修改商品库存，增加库存时，需要减少上级门店的库存。
     * 减少库存： 目前没有这个场景
     *
     * @param shopId     上级门店的ID， 如果是kt，则是平台商品
     * @param goodIds
     * @param stockDelta
     */
    @Transactional
    public void updateStock(String shopId, String[] goodIds, Integer stockDelta) {
        int rows = 0;
        for (String goodId : goodIds) {
            GoodEntity good = goodDAO.getGoodEntityByGoodId(goodId);
            A.check(good == null, "商品不存在:" + goodId);
            if (stockDelta > 0) {

                GoodEntity superiorGood = goodDAO.getGoodEntityByShopIdAndGoodNo(shopId, good.getGoodNo());
                A.check(superiorGood == null, "上级门店没有此编号的商品" + good.getGoodNo());

                rows = goodDAO.reduceStock(superiorGood.getId(), stockDelta);
                A.check(rows == 0, "上级门店商品库存不足");

                rows = goodDAO.addStock(goodId, stockDelta);
            } else {
                rows = goodDAO.reduceStock(goodId, 0 - stockDelta);
            }

            A.check(rows == 0, "更新库存失败");
        }
    }

    /**
     * 根据商品Id查询商品
     *
     * @param goodId
     * @return
     */
    public GoodVO getGoodById(String goodId) {
        GoodVO goodVO = new GoodVO();
        GoodEntity goodBase = goodDAO.getGoodByGoodId(goodId);
        A.check(goodBase == null, "商品无效或不存在");
        //优惠券列表
        List<GoodCouponCenterEntity> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByGoodId(goodId);
        //支付方式列表
        List<GoodPayReqVO> goodPayEntityList = goodDAO.getGoodPayByGoodIdList(goodId);
        goodBase.setGoodCouponCenterEntityList(goodCouponCenterEntityList);
        goodBase.setGoodPayEntityList(goodPayEntityList);
        goodVO.setShopName(goodBase.getShopName());
        BeanUtils.copyProperties(goodBase, goodVO);
        // 构建分类字段值
        buildGoodType(goodVO);
        // 设置属性字段
        //goodVO.setGoodPropertys(propertyDAO.getGoodProperty(goodId));
        // 设置图片信息
        List<GoodImgEntity> imgList = propertyDAO.getGoodImg(goodId);
        buildGoodImg(imgList, goodVO);
        // 判断改用户是否收藏该商品
/*        if (StringUtils.isEmpty(userId) || collectDAO.getGoodCollectEntity(userId, goodId) == null) {
            goodVO.setCollectType(Constants.DEFAULT_0);
        } else {
            goodVO.setCollectType(Constants.DEFAULT_1);
        }*/
        return goodVO;
    }

    /********************以上为Admin后台所用接口****************************/
    // 商铺后台使用
    public CommonPageVO<GoodRespVO> searchGoodByShopId(String shopId, String goodName, String goodType,
                                                       String payTypes,
                                                       int searchMode, Date startTime, Date endTime, String status, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<GoodRespVO> list = goodDAO.searchGoodByShopId(shopId, goodName, goodType, payTypes, searchMode, startTime, endTime, status,
                rowBounds);
        for (GoodRespVO goodRespVO : list) {
            List<GoodCouponCenterReqVO> goodCouponCenterEntityList = goodCouponCenterDao.getGoodCollectEntityByShopIdAndGoodId(goodRespVO.getShopId(), goodRespVO.getId());
            if (goodCouponCenterEntityList.size() > 0) {
                goodRespVO.setCouponEntityList(goodCouponCenterEntityList);
            }
            //支付类型信息
            List<GoodPayReqVO> goodPayList = goodDAO.getGoodPayByGoodId(goodRespVO.getId());
            goodRespVO.setGoodPayList(goodPayList);
        }


        PageInfo<GoodRespVO> pageInfo = new PageInfo<>(list);
        return CommonUtil.copyFromPageInfo(pageInfo, list);
    }

    public List<GoodEntity> getGoodsByKtList(String shopId) {
        return goodDAO.getGoodsByKtList(shopId);
    }

    /**
     * @param shopId     店铺ID
     * @param goodId     商品ID
     * @param goodStatus 商品状态 1上架 3下架
     */
    @Transactional
    public void upGoodShelf(String shopId, List<String> goodId, String goodStatus) {
        for (String good : goodId) {
            GoodEntity goodEntity = goodDAO.getGoodByGoodId(good);
            A.check(goodEntity == null, "商品不存在");
            A.check(goodEntity.getDelFlag() == 1, "商品已经删除,不能操作");
            if (goodStatus.equals("1")) {
                //上架操作判断总后台商品是否下架
                A.check(goodEntity.getStatus().equals("3"), "商品已经被后台下架，不能上架");
            }
            int count = goodShopCenterDao.modifyStatus(shopId, good, goodStatus);
            A.check(count <= 0, "操作失败");
        }

    }

    @Transactional
    public int batchGoodStatus(List<String> list, String goodStatus) {
        return goodShopCenterDao.batchGoodStatus(list, goodStatus);
    }

    @Transactional
    public int insertByBatchGoodShopCenter(List<GoodEntity> list) {
        List<GoodShopCenterEntity> centerList = new ArrayList<>();
        for (GoodEntity entity : list) {
            GoodShopCenterEntity good = new GoodShopCenterEntity();
            good.setGoodStatus(entity.getStatus());
            good.setShopId("1");
            good.setGoodId(entity.getId());
            good.setId(UUID.randomUUID().toString());
            centerList.add(good);
        }
        return goodShopCenterDao.insertByBatchGoodShopCenter(centerList);
    }
    public void checkShopSalesAmountByShopIdAndGoodId(String shopId,String goodId,int buyNum){
        //正宇汽车只能卖20辆
        //BigDecimal shopPointBase = sysSettingsService.getShopPointBase();
        //https://github.com/sharding-sphere/sharding-sphere/issues/767 暂时不支持for update走主库
        //HintManager 会自动关闭
        int sales = 0;
        try (HintManager hintManager = HintManager.getInstance()) {
            //强制走主库
            hintManager.setMasterRouteOnly();
            sales = goodDAO.getShopGoodSales(shopId,goodId);
            int currentSales  = sales + buyNum;
            A.check(new BigDecimal(sales).compareTo(new BigDecimal(20)) >= 0, "店铺该商品库存不足");
            A.check(new BigDecimal(currentSales).compareTo(new BigDecimal(20)) > 0, "店铺该商品当前可卖数量为：" + (20 - sales) );
        }

    }
}

