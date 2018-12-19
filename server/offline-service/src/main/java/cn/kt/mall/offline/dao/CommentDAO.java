package cn.kt.mall.offline.dao;

import cn.kt.mall.offline.entity.CommentEntity;
import cn.kt.mall.offline.vo.CommentInfo;
import cn.kt.mall.offline.vo.CommentResVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
@Mapper
public interface CommentDAO {

    /**
     * 添加评论
     *
     * @param commentEntity
     * @return
     */
    int addComment(CommentEntity commentEntity);

    /**
     * 查询店铺的所有评论
     *
     * @param shopId
     * @return
     */
    List<CommentInfo>  selectComment(@Param("shopId") String shopId);

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    CommentEntity getOrderComment(@Param("orderId") String orderId);

    /**
     * 获取评论管理
     *
     * @param commentResVO
     * @return
     */
    List<CommentEntity> getCommentManage(CommentResVO commentResVO);
    int getCommentCount(CommentResVO commentResVO);
}
