package cn.kt.mall.offline.service.impl;

import cn.kt.mall.common.asserts.A;
import cn.kt.mall.common.http.vo.PageVO;
import cn.kt.mall.common.jwt.SubjectUtil;
import cn.kt.mall.offline.dao.CommentDAO;
import cn.kt.mall.offline.dao.OfflineUserDAO;
import cn.kt.mall.offline.entity.CommentEntity;
import cn.kt.mall.offline.service.CommentService;
import cn.kt.mall.offline.vo.CommentResVO;
import cn.kt.mall.offline.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private OfflineUserDAO offlineUserDAO;

    @Override
    public int addComment(CommentVO commentVO) {
        String userId = null;
        if(commentVO.getFlag()==1){
            userId = SubjectUtil.getCurrent().getId();
        }
        CommentEntity commentEntity = new CommentEntity(commentVO.getShopId(),
                commentVO.getOrderId(), userId, commentVO.getContent(), commentVO.getScore(),commentVO.getPics());

        int count = commentDAO.addComment(commentEntity);
        A.check(count<1,"添加评论失败!");
        return count;
    }

    @Override
    public PageVO<CommentEntity> getCommentManage(CommentResVO commentResVO) {
        int pageNo = commentResVO.getPageNo();
        int pageSize = commentResVO.getPageSize();
        int srcPageNo = pageNo;
        if (pageNo >= 1)
            pageNo = pageNo - 1;
        int offset = pageNo * pageSize;
        commentResVO.setPageNo(offset);
        commentResVO.setPageSize(pageSize);
        //获取店铺id
        String shopId = offlineUserDAO.getShopId(SubjectUtil.getCurrent().getId());
        commentResVO.setShopId(shopId);
        int count = commentDAO.getCommentCount(commentResVO);
        List<CommentEntity> list = commentDAO.getCommentManage(commentResVO);
        return new PageVO<>(srcPageNo, pageSize, count, list);
    }
}
