package com.xqiang.job.admin.common.param.request;


import lombok.Data;

@Data
public class JobTaskUpdateBO extends JobTaskSaveBO {

    /**
     * 任务Id
     */
    private Integer id;

}
