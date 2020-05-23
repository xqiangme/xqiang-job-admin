package com.xqiang.job.admin.core.web.controller;

import com.xqiang.job.admin.common.param.request.*;
import com.xqiang.job.admin.core.service.ScheduledQuartzUserService;
import com.xqiang.job.admin.core.shiro.JobAdminShiroOperation;
import com.xqiang.job.admin.common.util.JobAdminStringUtils;
import com.xqiang.job.admin.common.param.base.JobAdminResponse;
import com.xqiang.job.admin.common.param.response.UserInfoPageVO;
import com.xqiang.job.admin.common.param.response.UserLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务平台 用户登录
 *
 * @author mengqiang
 */
@Slf4j
@RestController
@RequestMapping("/job-admin/user")
public class ScheduledQuartUserController {

    @Resource
    private ScheduledQuartzUserService scheduledQuartzUserService;

    /**
     * 分页列表
     *
     * @param queryBO 入参
     * @return 分页列表
     * @author mengq
     */
    @RequestMapping("/listPage")
    public JobAdminResponse listPageJob(JobTaskUserPageQueryBO queryBO) {
        UserInfoPageVO result = scheduledQuartzUserService.listPage(queryBO);
        JobAdminResponse jobAdminResponse = JobAdminResponse.success(result.getList());
        jobAdminResponse.setCount(result.getTotal());
        return jobAdminResponse;
    }

    /**
     * 任务详情
     *
     * @param queryBO 入参
     * @return 任务详情
     * @author mengq
     */
    @RequestMapping("/getUserDetail")
    public JobAdminResponse getUserDetail(@RequestBody JobTaskUserOperateBO queryBO) {
        return JobAdminResponse.success(scheduledQuartzUserService.getUserDetail(queryBO));
    }

    /**
     * 用户详情
     *
     * @return 用户详情
     * @author mengq
     */
    @RequestMapping("/getUserPersonDetail")
    public JobAdminResponse getUserPersonDetail() {
        return JobAdminResponse.success(scheduledQuartzUserService.getUserPersonDetail());
    }

    /**
     * 新增用户
     *
     * @param saveBO 入参
     * @return 新增用户
     * @author mengq
     */
    @RequestMapping("/save")
    public JobAdminResponse saveUser(@RequestBody JobTaskUserSaveBO saveBO) {
        this.buildOperate(saveBO);
        scheduledQuartzUserService.saveUser(saveBO);
        return JobAdminResponse.success();
    }

    /**
     * 修改用户
     *
     * @param updateBO 入参
     * @return 修改用户
     * @author mengq
     */
    @RequestMapping("/update")
    public JobAdminResponse updateUser(@RequestBody JobTaskUserUpdateBO updateBO) {
        this.buildOperate(updateBO);
        scheduledQuartzUserService.updateUser(updateBO);
        return JobAdminResponse.success();
    }

    /**
     * 修改密码
     *
     * @param updateBO 入参
     * @return 修改密码
     * @author mengq
     */
    @RequestMapping("/update-pwd")
    public JobAdminResponse updatePwd(@RequestBody JobTaskUserUpdatePwdBO updateBO) {
        this.buildOperate(updateBO);
        scheduledQuartzUserService.updatePwd(updateBO);
        return JobAdminResponse.success();
    }

    /**
     * 修改权限
     *
     * @param updateBO 入参
     * @return 修改权限
     * @author mengq
     */
    @RequestMapping("/update-power")
    public JobAdminResponse updatePower(@RequestBody JobTaskUserUpdatePowerBO updateBO) {
        this.buildOperate(updateBO);
        scheduledQuartzUserService.updateUserPower(updateBO);
        return JobAdminResponse.success();
    }

    /**
     * 删除用户
     *
     * @param saveBO 入参
     * @return 删除用户
     * @author mengq
     */
    @RequestMapping("/delete")
    public JobAdminResponse deleteUser(@RequestBody JobTaskUserOperateBO saveBO) {
        this.buildOperate(saveBO);
        scheduledQuartzUserService.deleteUser(saveBO);
        return JobAdminResponse.success();
    }

    private void buildOperate(JobBaseOperateBO operateBO) {
        UserLoginResult currentUser = JobAdminShiroOperation.getCurrentUser();
        if (null != currentUser) {
            operateBO.setOperateBy(String.valueOf(currentUser.getUserId()));
            operateBO.setOperateName(JobAdminStringUtils.isBlank(currentUser.getRealName()) ?
                    currentUser.getUsername() : currentUser.getRealName());
        }
    }

}
