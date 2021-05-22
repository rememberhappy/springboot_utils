package com.springbootutils.util;

import lombok.Data;

/**
 * @author daocers
 * @date 2020/11/30 17:53
 */
@Data
public class ExcelErrorDto {
    private Integer lineNo;
    private String column;
    private String message;
}
