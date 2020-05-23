package com.xqiang.job.admin.core.dao.mapper;

import com.xqiang.job.admin.common.param.dto.JobTaskPageQueryDTO;
import com.xqiang.job.admin.core.dao.bean.ScheduledQuartzJobInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 任务表 Mapper
 *
 * @author mengq
 */
@Repository
public interface ScheduledQuartzJobMapper {

    /**
     * 新增任务，初始状态为不启动
     *
     * @param job 参数
     */
    void addByJob(ScheduledQuartzJobInfo job);

    /**
     * 更新任务
     *
     * @param job 参数
     * @return 更新行数
     */
    int updateByProjectAndId(ScheduledQuartzJobInfo job);

    /**
     * 移除任务
     *
     * @param id         任务ID
     * @param projectKey 项目key
     * @param updateBy   更新人
     * @param updateName 更新人名
     * @return 移除行数
     */
    int removeByProjectAndId(@Param("id") Integer id, @Param("projectKey") String projectKey, @Param("updateBy") String updateBy, @Param("updateName") String updateName);

    /**
     * 根据ID查询任务
     *
     * @param id 任务ID
     * @return 任务详情
     */
    ScheduledQuartzJobInfo getJobById(@Param("id") Integer id);


    /**
     * 根据项目与ID查询任务
     *
     * @param id         任务ID
     * @param projectKey 项目key
     * @return 列表
     */
    ScheduledQuartzJobInfo getJobByProjectAndId(@Param("id") Integer id, @Param("projectKey") String projectKey);


    /**
     * 根据条件统计
     *
     * @param queryBO 参数
     * @return 统计
     */
    Integer countByCondition(JobTaskPageQueryDTO queryBO);

    /**
     * 根据条件分页查询任务
     *
     * @param queryBO 参数
     * @return 列表
     */
    List<ScheduledQuartzJobInfo> listPageByCondition(JobTaskPageQueryDTO queryBO);

    /**
     * 根据项目key与任务状态任务
     *
     * @param projectKey 项目key
     * @param jobStatus  状态
     * @return 列表
     */
    List<ScheduledQuartzJobInfo> getJobListByProjectAndStatus(@Param("projectKey") String projectKey, @Param("jobStatus") Integer jobStatus);


    /**
     * 查询project下，组和名称相同的任务
     *
     * @param projectKey 项目 key
     * @param jobGroup   分组
     * @param jobName    任务名
     * @return 任务列表
     */
    List<ScheduledQuartzJobInfo> getJobListByProjectGroupAndName(@Param("projectKey") String projectKey,
                                                                 @Param("jobGroup") String jobGroup,
                                                                 @Param("jobName") String jobName);

    /**
     * 统计project下，组和名称相同的任务
     *
     * @param projectKey 项目 key
     * @param jobGroup   分组
     * @param jobClass   任务名
     * @param jobMethod  任务方法
     * @return ""
     */
    int countByProjectGroupAndMethod(@Param("projectKey") String projectKey,
                                     @Param("jobGroup") String jobGroup,
                                     @Param("jobClass") String jobClass,
                                     @Param("jobMethod") String jobMethod);

    /**
     * 查询project下，组和名称相同的任务-排除自己
     *
     * @param projectKey   项目 key
     * @param jobGroup     分组
     * @param jobClass     任务名
     * @param jobMethod    任务方法
     * @param excludeJobId ""
     * @return 统计值
     */
    int countByProjectGroupAndMethodExclude(@Param("projectKey") String projectKey,
                                            @Param("jobGroup") String jobGroup,
                                            @Param("jobClass") String jobClass,
                                            @Param("jobMethod") String jobMethod,
                                            @Param("excludeJobId") Integer excludeJobId);


    /**
     * 根据项目与状态统计
     *
     * @param projectKey 项目key
     * @param jobStatus  任务状态
     * @return 行数
     */
    int countByProjectAndStatus(@Param("projectKey") String projectKey, @Param("jobStatus") Integer jobStatus);


}
