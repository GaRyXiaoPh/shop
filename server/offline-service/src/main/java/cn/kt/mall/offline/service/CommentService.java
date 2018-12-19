package cn.kt.mall.offline.service;

import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.offline.entity.CommentEntity;
import cn.kt.mall.offline.entity.DataEntity;
import cn.kt.mall.offline.vo.CommentResVO;
import cn.kt.mall.offline.vo.CommentVO;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
public interface CommentService {

    /**
     * 添加评论
     *
     * @param commentVO
     * @return
     */
    int addComment(CommentVO commentVO);

    /**
     * 获取评论管理
     *
     * @param commentResVO
     * @return
     */
    PageVO<CommentEntity> getCommentManage(CommentResVO commentResVO);
}
