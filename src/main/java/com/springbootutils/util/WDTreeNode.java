package com.springbootutils.util;

import lombok.Data;

import java.util.List;

/**
 * @author daocers
 * @date 2020/11/13 10:07
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
     *
     * @param
     * @return
     * @author daocers
     * @date 2020/11/13 10:08
     */
    private Object data;
    private Integer seq;
}
