package com.xqiang.job.admin.core.service.impl;

import cn.hutool.core.date.DateUtil;
import com.xqiang.job.admin.common.param.request.JobTaskLogDetailBO;
import com.xqiang.job.admin.common.param.request.JobTaskLogPageQueryBO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobLogDetailVO;
import com.xqiang.job.admin.common.util.JobAdminBeanCopyUtil;
import com.xqiang.job.admin.common.util.JobAdminPageUtils;
import com.xqiang.job.admin.common.util.JobAdminStringUtils;
import com.xqiang.job.admin.core.config.BasicJobConfig;
import com.xqiang.job.admin.core.dao.bean.ScheduledQuartzJobLogInfo;
import com.xqiang.job.admin.core.dao.mapper.ScheduledQuartzJobLogMapper;
import com.xqiang.job.admin.core.service.ScheduledQuartzJobLogService;
import com.xqiang.job.admin.common.param.dto.JobTaskLogPageQueryDTO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobLogItemVO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobLogPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务日志 service
 *
 * @author mengq
 * @version 1.0
 * @date 2020-05-16
 */
@Slf4j
@Service
public class ScheduledTaskJobLogServiceImpl implements ScheduledQuartzJobLogService {

    @Resource
    private BasicJobConfig basicJobConfig;

    @Resource
    private ScheduledQuartzJobLogMapper scheduledQuartzJobLogMapper;

    /**
     * 分页日志列表
     *
     * @param queryBO
     * @return 任务列表
     * @author mengq
     */
    @Override
    public ScheduledQuartzJobLogPageVO listPageLog(JobTaskLogPageQueryBO queryBO) {
        String projectKey = basicJobConfig.getProjectKey();
        if (JobAdminStringUtils.isBlank(projectKey)) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }
        //构建查询参数
        JobTaskLogPageQueryDTO pageQueryDTO = this.buildLogQueryParam(queryBO);
        Integer total = scheduledQuartzJobLogMapper.countByCondition(pageQueryDTO);
        if (total <= 0) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }

        List<ScheduledQuartzJobLogItemVO> logList = scheduledQuartzJobLogMapper.listPageByCondition(pageQueryDTO);
        if (CollectionUtils.isEmpty(logList)) {
            return ScheduledQuartzJobLogPageVO.initDefault();
        }
        return new ScheduledQuartzJobLogPageVO(total, logList);
    }


    /**
     * 日志详情
     *
     * @param detailBO
     * @return 日志详情
     * @author mengq
     */
    @Override
    public ScheduledQuartzJobLogDetailVO getLogDetail(JobTaskLogDetailBO detailBO) {
        ScheduledQuartzJobLogInfo logInfo = scheduledQuartzJobLogMapper.getById(detailBO.getId());
        return JobAdminBeanCopyUtil.copy(logInfo, ScheduledQuartzJobLogDetailVO.class);
    }

    /**
     * 构建查询参数
     *
     * @param queryBO
     * @return
     */
    private JobTaskLogPageQueryDTO buildLogQueryParam(JobTaskLogPageQueryBO queryBO) {
        //构建查询参数
        return JobTaskLogPageQueryDTO.builder()
                .projectKey(basicJobConfig.getProjectKey())
                .limit(JobAdminPageUtils.getStartRow(queryBO.getPage(), queryBO.getLimit()))
                .pageSize(JobAdminPageUtils.getOffset(queryBO.getLimit()))
                .logType(queryBO.getLogType())
                .jobId(queryBO.getJobId())
                .jobNameLike(queryBO.getJobNameLike())
                .operateId(queryBO.getOperateId())
                .operateNameLike(queryBO.getOperateNameLike())
                .contentLike(queryBO.getContentLike())
                .createStartTime(JobAdminStringUtils.isBlank(queryBO.getCreateStartTime()) ?
                        null : DateUtil.parseDateTime(queryBO.getCreateStartTime()))
                .createEndTime(JobAdminStringUtils.isBlank(queryBO.getCreateEndTime()) ?
                        null : DateUtil.parseDateTime(queryBO.getCreateEndTime()))
                .build();
    }
}
