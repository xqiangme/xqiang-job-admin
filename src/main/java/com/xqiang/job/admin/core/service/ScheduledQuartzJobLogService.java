package com.xqiang.job.admin.core.service;

import com.xqiang.job.admin.common.param.request.JobTaskLogDetailBO;
import com.xqiang.job.admin.common.param.request.JobTaskLogPageQueryBO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobLogDetailVO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobLogPageVO;

/**
 * 任务日志 service
 *
 * @author mengq
 * @version 1.0
 */
public interface ScheduledQuartzJobLogService {

    /**
     * 分页日志列表
     *
     * @param queryBO 参数
     * @return 任务列表
     * @author mengq
     */
    ScheduledQuartzJobLogPageVO listPageLog(JobTaskLogPageQueryBO queryBO);

    /**
     * 日志详情
     *
     * @param detailBO 参数
     * @return 日志详情
     * @author mengq
     */
    ScheduledQuartzJobLogDetailVO getLogDetail(JobTaskLogDetailBO detailBO);

}
