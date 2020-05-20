package com.xqiang.job.admin.core.web.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.alibaba.fastjson.JSON;
import com.xqiang.job.admin.common.enums.SysExceptionEnum;
import com.xqiang.job.admin.common.exception.JobAdminExceptionJobAdmin;
import com.xqiang.job.admin.core.service.ScheduledQuartzUserService;
import com.xqiang.job.admin.core.shiro.JobAdminShiroOperation;
import com.xqiang.job.admin.common.param.base.JobAdminResponse;
import com.xqiang.job.admin.common.param.request.JobTaskUserLoginBO;
import com.xqiang.job.admin.common.param.response.UserLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户登录
 *
 * @author mengq
 */
@Slf4j
@RestController
@RequestMapping("/job-admin/login")
public class ScheduledQuartUserLoginController {

    @Resource
    private ScheduledQuartzUserService scheduledQuartzUserService;
    /**
     * cookie_key
     */
    public static final String COOKIE_USER_INFO = "COOKIE_USER_INFO";

    /**
     * 缓存验证码
     */
    public static final String CACHE_VERIFY_CODE_KEY = "CACHE_VERIFY_CODE_KEY";


    @RequestMapping("/getVerifyCode")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        log.debug("[request] getCaptcha");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 60);
        String verifyCode = lineCaptcha.getCode();
        log.info("生成验证码, sessionId={} , verifyCode={}", verifyCode, JobAdminShiroOperation.getSessionId());
        JobAdminShiroOperation.setCacheParam(CACHE_VERIFY_CODE_KEY, verifyCode);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //生成图片
            lineCaptcha.write(out);
        } finally {
            if (null != out) {
                out.close();
                log.info("生成验证码流关闭");
            }
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/in")
    public JobAdminResponse loginIn(@RequestBody JobTaskUserLoginBO loginParam, HttpServletResponse response) {
        log.info("[ LoginController ] >> 用户登录 loginParam:{}", JSON.toJSONString(loginParam));
        String verifyCode = (String) JobAdminShiroOperation.getCacheParam(CACHE_VERIFY_CODE_KEY);
        if (ObjectUtils.isEmpty(verifyCode) || !verifyCode.equalsIgnoreCase(loginParam.getVerifyCode())) {
            throw new JobAdminExceptionJobAdmin(SysExceptionEnum.USER_CACHE_CODE_ERROR);
        }
        UserLoginResult result = scheduledQuartzUserService.login(loginParam);
        //设置Cookie
        Cookie cookie = new Cookie(COOKIE_USER_INFO, result.getUrlEncoderCookieValue());
        //120分钟
        cookie.setMaxAge(120 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return JobAdminResponse.success(scheduledQuartzUserService.login(loginParam));
    }

    /**
     * 用户登出
     */
    @RequestMapping("/out")
    public JobAdminResponse loginOut(HttpServletRequest request, HttpServletResponse response) {
        UserLoginResult currentUser = JobAdminShiroOperation.getCurrentUser();
        if (null == currentUser) {
            return JobAdminResponse.success();
        }
        log.info("[ LoginController ] >> 用户登出 currentUser:{}", currentUser.getLogInfo());
        JobAdminShiroOperation.loginOut();
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0) {
            return JobAdminResponse.success();
        }
        //清除cookie
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_USER_INFO)) {
                cookie.setValue(null);
                //立即销毁cookie
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }
        return JobAdminResponse.success();
    }

    /**
     * 获取登录用户信息
     */
    @RequestMapping("/getUserInfo")
    public JobAdminResponse getUserInfo() {
        Map<String, Object> map = new HashMap<>(2);
        //当前登录用户
        map.put("currentUser", JobAdminShiroOperation.getCurrentUser());
        //权限信息
        map.put("permissions", JobAdminShiroOperation.getPermissions());
        return JobAdminResponse.success(map);
    }

}
