package com.springbootutils.util;

import lombok.Data;

import java.util.List;

/**
 * 树节点
 */
@Data
public class WDTreeNode {
    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private List<WDTreeNode> children;
    /**
     * 附加的本条信息
     */
    private Object data;
    private Integer seq;
}
