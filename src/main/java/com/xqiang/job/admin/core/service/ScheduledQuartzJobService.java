package com.xqiang.job.admin.core.service;

import com.xqiang.job.admin.common.param.request.JobTaskOperateBO;
import com.xqiang.job.admin.common.param.request.JobTaskPageQueryBO;
import com.xqiang.job.admin.common.param.request.JobTaskSaveBO;
import com.xqiang.job.admin.common.param.request.JobTaskUpdateBO;
import com.xqiang.job.admin.common.param.response.HomeResultVO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobDetailVO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobPageVO;

/**
 * 任务管理接口
 *
 * @author mengq
 * @version 1.0
 */
public interface ScheduledQuartzJobService {

    /**
     * 主页统计与系统信息
     *
     * @return 主页统计与系统信息
     * @author mengq
     */
    HomeResultVO getHomeCount();

    /**
     * 分页任务列表
     *
     * @param queryBO 参数
     * @return 任务列表
     * @author mengq
     */
    ScheduledQuartzJobPageVO listPageJob(JobTaskPageQueryBO queryBO);

    /**
     * 任务详情
     *
     * @param operateBO 参数
     * @return 任务详情
     * @author mengq
     */
    ScheduledQuartzJobDetailVO getJobDetail(JobTaskOperateBO operateBO);

    /**
     * 新增任务
     *
     * @param jobSaveBO 参数
     * @author mengq
     */
    void addJob(JobTaskSaveBO jobSaveBO);


    /**
     * 启动任务
     *
     * @param operateBO 参数
     * @author mengq
     */
    void startJob(JobTaskOperateBO operateBO);


    /**
     * 停止任务
     *
     * @param operateBO 参数
     * @author mengq
     */
    void stopJob(JobTaskOperateBO operateBO);


    /**
     * 修改任务
     *
     * @param updateBO 修改参数
     */
    void updateJob(JobTaskUpdateBO updateBO);

    /**
     * 删除任务
     *
     * @param operateBO 删除参数
     */
    void deleteJob(JobTaskOperateBO operateBO);

}
