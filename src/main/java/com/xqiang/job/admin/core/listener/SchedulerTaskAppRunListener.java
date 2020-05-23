package com.xqiang.job.admin.core.listener;

import com.xqiang.job.admin.core.dao.mapper.ScheduledQuartzJobMapper;
import com.xqiang.job.admin.core.quartz.QuartzSchedulerUtil;
import com.xqiang.job.admin.core.config.BasicJobConfig;
import com.xqiang.job.admin.core.dao.bean.ScheduledQuartzJobInfo;
import com.xqiang.job.admin.common.enums.BaseSchedulerRunEnum;
import com.xqiang.job.admin.common.enums.ScheduledJobStatusEnum;
import com.xqiang.job.admin.core.quartz.QuartzJobBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务监听
 * 监听项目启动后查询到需要启动的项目并启动
 *
 * @author mengq
 */
@Slf4j
@Component
public class SchedulerTaskAppRunListener extends ApplicationObjectSupport implements
        ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private BasicJobConfig basicJobConfig;

    @Resource
    private ScheduledQuartzJobMapper scheduledQuartzJobMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationReadyEvent) {
        //防止重复加载
        if (applicationReadyEvent.getApplicationContext().getParent() != null) {
            return;
        }
        if (basicJobConfig == null || scheduledQuartzJobMapper == null) {
            return;
        }
        log.info("[ SchedulerTaskAppRunListener  ]  projectKey:{}  init task start ", basicJobConfig.getProjectKey());
        ApplicationContext ac = getApplicationContext();
        QuartzJobBean.setAc(ac);
        int enableTaskSize = this.startAll();
        log.info("[ SchedulerTaskAppRunListener  ]  projectKey:{}  init task end enableTaskSize : {}", basicJobConfig.getProjectKey(), enableTaskSize);
    }


    /**
     * 启动全部任务 本地测试的时候，调度器不会启动； 测试环境默认只启动一个Debug用的JOB 线上会按照数据库读取任务
     *
     * @return 数量
     */
    public int startAll() {
        try {
            QuartzSchedulerUtil.startScheduler();
            if (null == basicJobConfig) {
                return 0;
            }

            if (!BaseSchedulerRunEnum.ON.getModel().equalsIgnoreCase(basicJobConfig.getStart())) {
                log.error("[ SchedulerTaskAppRunListener  ] >> projectKey:{} , init switch is off", basicJobConfig.getProjectKey());
                return 0;
            }

            return this.doRunTaskAfterApplicationRun();
        } catch (Exception e) {
            log.error("[ SchedulerTaskAppRunListener  ] >> projectKey:{} , init task exception ", basicJobConfig.getProjectKey(), e);
        }
        return 0;
    }

    /**
     * 启动需要启动的任务
     *
     * @return 行数
     */
    private int doRunTaskAfterApplicationRun() {
        //启用的任务
        int jobStatus = ScheduledJobStatusEnum.ON.getValue();
        //查询任务
        List<ScheduledQuartzJobInfo> list = scheduledQuartzJobMapper.getJobListByProjectAndStatus(basicJobConfig.getProjectKey(), jobStatus);
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        log.info("[ SchedulerTaskAppRunListener  ] >> projectKey:{} , need init task size:{} ", basicJobConfig.getProjectKey(), list.size());
        try {
            for (ScheduledQuartzJobInfo job : list) {
                //任务类方法存在校验
                if (!QuartzSchedulerUtil.checkBeanAndMethodIsExists(job.getJobClass(), job.getJobMethod(), job.getJobArguments())) {
                    continue;
                }
                QuartzSchedulerUtil.enable(job);
            }
        } catch (Exception e) {
            log.error("[SchedulerTaskAppRunListener]error when add job. " + e.getMessage(), e);
        }
        return list.size();
    }

}

