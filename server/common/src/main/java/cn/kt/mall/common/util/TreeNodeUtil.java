package cn.kt.mall.common.util;

import cn.kt.mall.common.vo.JsonTreeVO;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeUtil {

    public final static List<JsonTreeVO> getParentNode(List<JsonTreeVO> treeVOList) {
        List<JsonTreeVO> newTreeVOList = new ArrayList<>();
        for (JsonTreeVO jsonTreeVO : treeVOList) {
            if (jsonTreeVO.getPid() == null || "".equals(jsonTreeVO.getPid())) {
                jsonTreeVO.setChildren(getChildrenNode(jsonTreeVO.getId(), treeVOList));
                jsonTreeVO.setState("open");
                newTreeVOList.add(jsonTreeVO);
            }
        }
        return newTreeVOList;
    }

    public final static List<JsonTreeVO> getChildrenNode(String pid, List<JsonTreeVO> treeVOList) {
        List<JsonTreeVO> newTreeVOList = new ArrayList<>();
        for (JsonTreeVO jsonTreeVO : treeVOList) {
            if (jsonTreeVO.getPid() == null || "".equals(jsonTreeVO.getPid())) {
                continue;
            }
            if (jsonTreeVO.getPid().equals(pid)) {
                jsonTreeVO.setChildren(getChildrenNode(jsonTreeVO.getId(), treeVOList));
                newTreeVOList.add(jsonTreeVO);
            }
        }
        return newTreeVOList;
    }
}
