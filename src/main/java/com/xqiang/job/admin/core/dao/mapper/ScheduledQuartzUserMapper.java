package com.xqiang.job.admin.core.dao.mapper;

import com.xqiang.job.admin.core.dao.bean.ScheduledQuartUserInfo;
import com.xqiang.job.admin.common.param.dto.JobUserPageQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 人员管理
 *
 * @author mengq
 */
@Repository
public interface ScheduledQuartzUserMapper {

    /**
     * 新增项目下配置
     *
     * @param configInfo 参数
     */
    void addUser(ScheduledQuartUserInfo configInfo);


    /**
     * 根据ID更新
     *
     * @param configInfo 参数
     * @return 处理行数
     */
    int updateSelectiveById(ScheduledQuartUserInfo configInfo);

    /**
     * 移除
     *
     * @param id         ID
     * @param projectKey 项目key
     * @param updateBy   更新人
     * @return 处理行数
     */
    int deleteByProjectAndId(@Param("projectKey") String projectKey, @Param("id") Integer id,
                             @Param("updateBy") String updateBy);

    /**
     * 根据ID查询任务
     *
     * @param id id
     * @return 详情
     */
    ScheduledQuartUserInfo getById(@Param("id") Integer id);

    /**
     * 根据项目与用户名查询
     *
     * @param username   用户名
     * @param projectKey 项目key
     * @return 用户
     */
    ScheduledQuartUserInfo getByProjectAndUsername(@Param("projectKey") String projectKey, @Param("username") String username);

    /**
     * 根据项目与用户名统计
     *
     * @param projectKey 项目key
     * @param username   用户名
     * @return 统计
     */
    int countByProjectAndUsername(@Param("projectKey") String projectKey, @Param("username") String username);

    /**
     * 组和名称相同的任务-排除自己
     *
     * @param projectKey 项目key
     * @param username   用户名
     * @param excludeId  需要排序的ID
     * @return 统计
     */
    int countByProjectAndUsernameExcludeId(@Param("projectKey") String projectKey,
                                           @Param("username") String username,
                                           @Param("excludeId") Integer excludeId);

    /**
     * 根据条件统计
     *
     * @param queryBO 参数
     * @return 统计行
     */
    Integer countByCondition(JobUserPageQueryDTO queryBO);

    /**
     * 根据条件分页查询任务
     *
     * @param queryBO 参数
     * @return 列表
     */
    List<ScheduledQuartUserInfo> listPageByCondition(JobUserPageQueryDTO queryBO);

}
