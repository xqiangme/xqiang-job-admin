package com.xqiang.job.admin.core.web.controller;


import com.xqiang.job.admin.common.param.request.JobTaskLogDetailBO;
import com.xqiang.job.admin.common.param.request.JobTaskLogPageQueryBO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobLogPageVO;
import com.xqiang.job.admin.core.service.ScheduledQuartzJobLogService;
import com.xqiang.job.admin.common.param.base.JobAdminResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务日志controller
 *
 * @author mengqiang
 */
@Slf4j
@RestController
@RequestMapping("/job-admin/job-log")
public class SchedulerQuartJobLogController {

    @Resource
    private ScheduledQuartzJobLogService scheduledTaskLogService;

    /**
     * 分页任务列表
     *
     * @param queryBO 参数
     * @return 任务列表
     * @author mengq
     */
    @RequestMapping("/listPage")
    public JobAdminResponse listPageJob(JobTaskLogPageQueryBO queryBO) {
        ScheduledQuartzJobLogPageVO result = scheduledTaskLogService.listPageLog(queryBO);
        JobAdminResponse jobAdminResponse = JobAdminResponse.success(result.getList());
        jobAdminResponse.setCount(result.getTotal());
        return jobAdminResponse;
    }

    /**
     * 日志详情
     *
     * @param detailBO 参数
     * @return 日志详情
     * @author mengq
     */
    @RequestMapping("/getLogDetail")
    public JobAdminResponse getJobDetail(@RequestBody JobTaskLogDetailBO detailBO) {
        return JobAdminResponse.success(scheduledTaskLogService.getLogDetail(detailBO));
    }

}
