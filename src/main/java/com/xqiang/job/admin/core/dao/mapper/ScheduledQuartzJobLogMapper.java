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
     * @return int
     */
    void addLog(ScheduledQuartzJobLogInfo entity);

    /**
     * 根据日志ID查询
     *
     * @param id
     * @return
     */
    ScheduledQuartzJobLogInfo getById(Long id);

    /**
     * 根据任务ID查询
     *
     * @param jobId jobId
     * @return List<ScheduledQuartzJobLogDO>
     */
    List<ScheduledQuartzJobLogInfo> getByJobId(Integer jobId);

    /**
     * 根据条件统计
     *
     * @param queryBO
     * @return
     */
    Integer countByCondition(JobTaskLogPageQueryDTO queryBO);

    /**
     * 根据条件分页查询任务
     *
     * @param queryBO
     * @return
     */
    List<ScheduledQuartzJobLogItemVO> listPageByCondition(JobTaskLogPageQueryDTO queryBO);

}
