package com.xqiang.job.admin.core.web.controller;

import cn.hutool.core.date.DateUtil;
import com.xqiang.job.admin.common.exception.JobAdminExceptionJobAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author mengq
 */
@Slf4j
@RestController
@RequestMapping("/monitor")
public class JobAdminMonitorController {

    @RequestMapping("/")
    public String monitor() {
        return "job admin success " + DateUtil.formatDateTime(new Date());
    }

    @RequestMapping("/test-exce")
    public String exceTest(int num) {
        if (1 == num) {
            throw new RuntimeException("测试 RuntimeException");
        }
        if (2 == num) {
            throw new JobAdminExceptionJobAdmin("测试 BusinessException");
        }
        return "success";
    }
}