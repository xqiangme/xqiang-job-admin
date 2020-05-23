package com.xqiang.job.admin.core.web.controller;

import com.xqiang.job.admin.common.param.request.*;
import com.xqiang.job.admin.common.param.response.UserLoginResult;
import com.xqiang.job.admin.common.util.JobAdminStringUtils;
import com.xqiang.job.admin.core.service.ScheduledQuartzJobService;
import com.xqiang.job.admin.core.shiro.JobAdminShiroOperation;
import com.xqiang.job.admin.common.param.base.JobAdminResponse;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobPageVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 任务管理 Controller
 *
 * @author mengqiang
 */
@RestController
@RequestMapping("/job-admin/job")
public class SchedulerQuartJobController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private ScheduledQuartzJobService scheduledQuartzJobService;

    /**
     * 首页统计
     *
     * @return 首页统计
     * @author mengq
     */
    @RequestMapping("/getHomeCount")
    public JobAdminResponse getHomeCount() {
        return JobAdminResponse.success(scheduledQuartzJobService.getHomeCount());
    }


    /**
     * 分页任务列表
     *
     * @param queryBO 查询参数
     * @return 任务列表
     * @author mengq
     */
    @RequestMapping("/listPage")
    public JobAdminResponse listPageJob(JobTaskPageQueryBO queryBO) {
        ScheduledQuartzJobPageVO result = scheduledQuartzJobService.listPageJob(queryBO);
        JobAdminResponse jobAdminResponse = JobAdminResponse.success(result.getList());
        jobAdminResponse.setCount(result.getTotal());
        return jobAdminResponse;
    }

    /**
     * 任务详情
     *
     * @param queryBO 参数
     * @return 任务详情
     * @author mengq
     */
    @RequestMapping("/getJobDetail")
    public JobAdminResponse getJobDetail(@RequestBody JobTaskOperateBO queryBO) {
        return JobAdminResponse.success(scheduledQuartzJobService.getJobDetail(queryBO));
    }

    /**
     * 启动任务
     *
     * @param operateBO 参数
     * @return 启动任务
     * @author mengq
     */
    @RequestMapping("/start")
    public JobAdminResponse startJob(@RequestBody JobTaskOperateBO operateBO) {
        this.buildOperate(operateBO);
        scheduledQuartzJobService.startJob(operateBO);
        return JobAdminResponse.success();
    }

    /**
     * 停止任务
     *
     * @param operateBO 入参
     * @return 停止任务
     * @author mengq
     */
    @RequestMapping("/stop")
    public JobAdminResponse stopJob(@RequestBody JobTaskOperateBO operateBO) {
        this.buildOperate(operateBO);
        scheduledQuartzJobService.stopJob(operateBO);
        return JobAdminResponse.success();
    }

    /**
     * 修改任务
     *
     * @param updateBO 入参
     * @return 修改任务
     */
    @RequestMapping("/update")
    public JobAdminResponse updateJob(@RequestBody JobTaskUpdateBO updateBO) {
        this.buildOperate(updateBO);
        scheduledQuartzJobService.updateJob(updateBO);
        return JobAdminResponse.success();
    }

    /**
     * 新增任务
     *
     * @param jobSaveBO 入参
     * @return 新增任务
     * @author mengq
     */
    @RequestMapping("/save")
    public JobAdminResponse addJob(@RequestBody JobTaskSaveBO jobSaveBO) {
        this.buildOperate(jobSaveBO);
        scheduledQuartzJobService.addJob(jobSaveBO);
        return JobAdminResponse.success();
    }

    /**
     * 删除任务
     *
     * @param operateBO 入参
     * @return 删除任务
     */
    @RequestMapping("/delete")
    public JobAdminResponse deleteJob(@RequestBody JobTaskOperateBO operateBO) {
        this.buildOperate(operateBO);
        scheduledQuartzJobService.deleteJob(operateBO);
        return JobAdminResponse.success();
    }

    private void buildOperate(JobBaseOperateBO operateBO) {
        UserLoginResult currentUser = JobAdminShiroOperation.getCurrentUser();
        if (null != currentUser) {
            operateBO.setOperateBy(String.valueOf(currentUser.getUserId()));
            operateBO.setOperateName(JobAdminStringUtils.isBlank(currentUser.getRealName()) ?
                    currentUser.getUsername() : currentUser.getRealName());
            operateBO.setClientIp(JobAdminStringUtils.isBlank(currentUser.getClientIp()) ? "" : currentUser.getClientIp());
            operateBO.setIpAddress(JobAdminStringUtils.isBlank(currentUser.getIpAddress()) ? "" : currentUser.getIpAddress());
            operateBO.setBrowserName(JobAdminStringUtils.isBlank(currentUser.getBrowserName()) ? "" : currentUser.getBrowserName());
            operateBO.setOs(JobAdminStringUtils.isBlank(currentUser.getOs()) ? "" : currentUser.getOs());
        }
    }
}
