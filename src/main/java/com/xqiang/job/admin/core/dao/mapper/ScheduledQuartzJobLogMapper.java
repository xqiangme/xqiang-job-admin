package com.xqiang.job.admin.core.dao.mapper;

import com.xqiang.job.admin.core.dao.bean.ScheduledQuartzJobLogInfo;
import com.xqiang.job.admin.common.param.dto.JobTaskLogPageQueryDTO;
import com.xqiang.job.admin.common.param.response.ScheduledQuartzJobLogItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务日志记录表
 *
 * @author mengq
 */
@Repository
public interface ScheduledQuartzJobLogMapper {

    /**
     * 新增日志
     *
     * @param entity entity
     */
    void addLog(ScheduledQuartzJobLogInfo entity);

    /**
     * 根据日志ID查询
     *
     * @param id id
     * @return 任务详情
     */
    ScheduledQuartzJobLogInfo getById(Long id);

    /**
     * 根据任务ID查询
     *
     * @param jobId jobId
     * @return 列表
     */
    List<ScheduledQuartzJobLogInfo> getByJobId(Integer jobId);

    /**
     * 根据条件统计
     *
     * @param queryBO 参数
     * @return 任务统计
     */
    Integer countByCondition(JobTaskLogPageQueryDTO queryBO);

    /**
     * 根据条件分页查询任务
     *
     * @param queryBO 参数
     * @return 任务
     */
    List<ScheduledQuartzJobLogItemVO> listPageByCondition(JobTaskLogPageQueryDTO queryBO);

}
