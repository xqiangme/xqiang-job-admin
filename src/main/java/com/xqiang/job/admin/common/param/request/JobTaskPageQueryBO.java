package com.xqiang.job.admin.common.param.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mengq
 * @version 1.0
 * @date 2020-05-16
 */
@Data
public class JobTaskPageQueryBO implements Serializable {
    private static final long serialVersionUID = 3234394229041271191L;

    private Integer page;

    private Integer limit;

    private String jobNameLike;

    private String jobMethodLike;

    private Integer jobStatus;

}
