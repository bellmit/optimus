package com.optimus.util.page;

import java.io.Serializable;

import lombok.Data;

/**
 * 分页对象
 * 
 * @author sunxp
 */
@Data
public class Page implements Serializable {

    private static final long serialVersionUID = 6257341442895693999L;

    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 页面大小
     */
    private Integer pageSize = 1000;

}
