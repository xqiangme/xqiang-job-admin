package com.xqiang.job.admin.core.service;

import com.xqiang.job.admin.common.param.request.*;
import com.xqiang.job.admin.common.param.response.UserInfoDetailVO;
import com.xqiang.job.admin.common.param.response.UserInfoPageVO;
import com.xqiang.job.admin.common.param.response.UserLoginResult;

/**
 * 任务平台用户管理接口
 *
 * @author mengq
 */
public interface ScheduledQuartzUserService {

    /**
     * 用户登录
     *
     * @param loginParam 登录参数
     * @return 用户信息
     */
    UserLoginResult login(JobTaskUserLoginBO loginParam);

    /**
     * 分页列表
     *
     * @param queryBO 查询参数
     * @return 任务列表
     * @author mengq
     */
    UserInfoPageVO listPage(JobTaskUserPageQueryBO queryBO);

    /**
     * 用户详情
     *
     * @param operateBO 参数
     * @return 用户详情
     * @author mengq
     */
    UserInfoDetailVO getUserDetail(JobTaskUserOperateBO operateBO);

    /**
     * 个人详情
     *
     * @return 个人详情
     * @author mengq
     */
    UserInfoDetailVO getUserPersonDetail();


    /**
     * 新增用户
     *
     * @param saveParam 新增参数
     */
    void saveUser(JobTaskUserSaveBO saveParam);

    /**
     * 修改用户
     *
     * @param updateBO 参数
     */
    void updateUser(JobTaskUserUpdateBO updateBO);

    /**
     * 修改用户权限
     *
     * @param updateBO
     */
    void updateUserPower(JobTaskUserUpdatePowerBO updateBO);

    /**
     * 修改密码
     *
     * @param updateBO 参数
     */
    void updatePwd(JobTaskUserUpdatePwdBO updateBO);

    /**
     * 删除用户
     *
     * @param operateBO 删除参数
     */
    void deleteUser(JobTaskUserOperateBO operateBO);

}
