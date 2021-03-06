package com.springbootutils.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树
 */
@Service
public class TreeService {

    /**
     * 使用平铺的树状节点处理
     *
     * @param nodeFlatList
     * @param withNodeDetail
     * @return java.util.List<com.springbootutils.util.WDTreeNode>
     * @Throws
     * @Author zhangdj
     * @date 2021/6/15 15:53
     */
    public List<WDTreeNode> getTree(List nodeFlatList, Boolean... withNodeDetail) {
        if (CollectionUtils.isEmpty(nodeFlatList)) {
            return new ArrayList<>();
        }
        List<WDTreeNode> nodes = new ArrayList<>();
        for (Object item : nodeFlatList) {
            WDTreeNode node = new WDTreeNode();
            BeanUtils.copyProperties(item, node);
            if (withNodeDetail != null && withNodeDetail.length > 0 && withNodeDetail[0]) {
                node.setData(item);
            }
            nodes.add(node);
        }
        return getTree(nodes);
    }

    /**
     * 获取树状列表
     *
     * @param list
     * @return java.util.List<com.springbootutils.util.WDTreeNode>
     * @Throws
     * @Author zhangdj
     * @date 2021/6/15 15:53
     */
    public List<WDTreeNode> getTree(List<WDTreeNode> list) {
        List<WDTreeNode> nodes = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return nodes;
        }
        for (WDTreeNode permission : list) {
            WDTreeNode dto = new WDTreeNode();
            if (permission == null) {
                continue;
            }
            BeanUtils.copyProperties(permission, dto);
            nodes.add(dto);
        }
        Map<Long, List<WDTreeNode>> info = new HashMap<>();
        List<WDTreeNode> rootList = new ArrayList<>();
        for (WDTreeNode dto : nodes) {
            Long id = dto.getParentId();
            if (!info.containsKey(id)) {
                info.put(id, new ArrayList<>());
            }
            info.get(id).add(dto);
            if (null == id || id < 1) {
                rootList.add(dto);
            }
        }
        for (WDTreeNode dto : rootList) {
            dto.setChildren(getChildren(dto.getId(), info));
        }
        return rootList;
    }

    /**
     * 递归处理树节点
     *
     * @param id
     * @param info
     * @return java.util.List<com.springbootutils.util.WDTreeNode>
     * @Throws
     * @Author zhangdj
     * @date 2021/6/15 15:54
     */
    private List<WDTreeNode> getChildren(Long id, Map<Long, List<WDTreeNode>> info) {
        List<WDTreeNode> children = info.get(id);
        if (CollectionUtils.isNotEmpty(children)) {
            for (WDTreeNode dto : children) {
                dto.setChildren(getChildren(dto.getId(), info));
            }
        }
        return children;
    }
}
