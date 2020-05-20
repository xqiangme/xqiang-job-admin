package com.xqiang.job.admin.common.constant;

/**
 * Job -admin 常量类
 *
 * @author mengq
 * @date 2020-05-16
 */
public class JobAdminOpenConstant {

    /**
     * 需要扫描的包所在地址
     */
    public static final String SCAN_PACKAGE = "com.xqiang.job.admin.core";

    /**
     * Mapper 包所在地址
     */
    public static final String MAPPER_PACKAGE = "com.xqiang.job.admin.core.dao.mapper";

    /**
     * xml 地址
     */
    public static final String XML_LOCATIONS = "classpath*:/mybatis/job/admin/**/*Mapper.xml";

}