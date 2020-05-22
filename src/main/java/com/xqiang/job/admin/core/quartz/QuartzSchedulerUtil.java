package com.xqiang.job.admin.core.quartz;

import com.xqiang.job.admin.common.util.JobAdminStringUtils;
import com.xqiang.job.admin.core.config.ApplicationContextHelper;
import com.xqiang.job.admin.core.dao.bean.ScheduledQuartzJobInfo;
import com.xqiang.job.admin.common.enums.SysExceptionEnum;
import com.xqiang.job.admin.common.exception.JobAdminExceptionJobAdmin;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import java.lang.reflect.Method;

/**
 * @author mengqiang
 */
public class QuartzSchedulerUtil {
    private static Logger log = LoggerFactory.getLogger(QuartzSchedulerUtil.class);
    private static Scheduler scheduler;
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    static {
        log.info("[ QuartzSchedulerUtil ] >> init");
        try {
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            log.error("[ QuartzSchedulerUtil ] init exception ", e);
        }
    }

    /**
     * 启动
     */
    public static void startScheduler() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 是否已经启动
     */
    public static boolean isStart(ScheduledQuartzJobInfo quartzJob) {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJob.getTriggerName(),
                quartzJob.getJobGroup());
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            return (null != trigger);
        } catch (Exception e) {
            log.info("[ QuartzSchedulerUtil ] >> isStart exception triggerName:{},JobGroup:{}", quartzJob.getTriggerName(),
                    quartzJob.getJobGroup(), e);
            return false;
        }
    }

    /**
     * Quartz启动任务
     */
    public static void enable(ScheduledQuartzJobInfo quartzJob) {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJob.getTriggerName(),
                quartzJob.getJobGroup());
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //若已经启动-则刷新任务
            if (null != trigger) {
                trigger.getTriggerBuilder().withIdentity(quartzJob.getTriggerName(), quartzJob.getJobGroup()).withSchedule(
                        CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
                        .build();
                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("[ QuartzSchedulerUtil ] >> enable exist task end triggerName:{},JobGroup:{}", quartzJob.getTriggerName(), quartzJob.getJobGroup());
                return;
            }

            // 任务执行类
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobBean.class)
                    // 任务名（类名+方法名+参数）任务组
                    .withIdentity(quartzJob.getQuartzJobName(), quartzJob.getJobGroup())
                    .build();
            jobDetail.getJobDataMap().put(QuartzJobBean.JOB_ID, quartzJob.getId());
            jobDetail.getJobDataMap().put(QuartzJobBean.TARGET_CLASS, quartzJob.getJobClass());
            jobDetail.getJobDataMap().put(QuartzJobBean.TARGET_METHOD, quartzJob.getJobMethod());
            jobDetail.getJobDataMap().put(QuartzJobBean.TARGET_ARGUMENTS, quartzJob.getJobArguments());
            jobDetail.getJobDataMap().put(QuartzJobBean.PROJECT_KEY, quartzJob.getProjectKey());

            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression())).build();

            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            log.info("[ QuartzSchedulerUtil ] >> start exception triggerName:{},JobGroup:{}", quartzJob.getTriggerName(),
                    quartzJob.getJobGroup(), e);
            throw new JobAdminExceptionJobAdmin("任务启动失败");
        }

        log.info("[ QuartzSchedulerUtil ] >> enable new task end triggerName:{},JobGroup:{}", quartzJob.getTriggerName(), quartzJob.getJobGroup());
    }

    /**
     * Quartz停止任务
     */
    public static void disable(ScheduledQuartzJobInfo quartzJob) {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJob.getTriggerName(),
                quartzJob.getJobGroup());

        try {

            Trigger trigger = scheduler.getTrigger(triggerKey);

            if (null != trigger) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(new JobKey(quartzJob.getQuartzJobName(), quartzJob.getJobGroup()));
            }

        } catch (Exception e) {
            log.info("[ QuartzSchedulerUtil ] >> disable exception triggerName:{},JobGroup:{}", quartzJob.getTriggerName(),
                    quartzJob.getJobGroup(), e);
            throw new JobAdminExceptionJobAdmin("任务停止失败");
        }
        log.info("[ QuartzSchedulerUtil ] >> disable job end triggerName:{},JobGroup:{}", quartzJob.getTriggerName(), quartzJob.getJobGroup());
    }

    /**
     * 校验任务类或-方法是否在环境中存在
     */
    public static Boolean checkBeanAndMethodIsExists(String jobClass, String targetMethod, String methodArgs) {
        try {
            checkBeanAndMethodExists(jobClass, targetMethod, methodArgs);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 校验任务类或-方法是否在环境中存在
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void checkBeanAndMethodExists(String jobClass, String targetMethod, String methodArgs) {
        if (null == jobClass) {
            throw new JobAdminExceptionJobAdmin(SysExceptionEnum.JOB_CLASS_NOT_EXISTS);
        }
        try {
            Object jobClassInfo = ApplicationContextHelper.getContext().getBean(jobClass);
            //任务参数
            Object[] jobArs = getJobArgs(methodArgs);
            Class jobClazz = jobClassInfo.getClass();
            Class[] parameterType = getParameters(jobArs);
            //执行任务方法
            Method method = jobClazz.getDeclaredMethod(targetMethod, parameterType);
            if (null == method) {
                throw new JobAdminExceptionJobAdmin(SysExceptionEnum.JOB_CLASS_METHOD_NOT_EXISTS, jobClass, targetMethod);
            }
        } catch (Exception e) {
            log.error("[ QuartzSchedulerUtil ] >> checkBeanAndMethodIsExists error ", e);
            if (e instanceof BeansException) {
                throw new JobAdminExceptionJobAdmin(SysExceptionEnum.JOB_CLASS_NOT_EXISTS, jobClass);
            } else {
                throw new JobAdminExceptionJobAdmin(SysExceptionEnum.JOB_CLASS_METHOD_NOT_EXISTS, jobClass, targetMethod);
            }
        }
    }

    /**
     * 处理Job设置的参数
     * 多个使用#&分隔，推荐使用单个String 类型JSON参数
     *
     * @param methodArgs
     * @return
     */
    public static Object[] getJobArgs(String methodArgs) {
        //参数处理
        Object[] args = null;
        if (!JobAdminStringUtils.isBlank(methodArgs)) {
            methodArgs = methodArgs + " ";
            String[] argString = methodArgs.split("#&");
            args = new Object[argString.length];
            for (int i = 0; i < argString.length; i++) {
                args[i] = argString[i].trim();
            }
        }
        return args;
    }

    /**
     * 处理参数
     *
     * @param jobArs
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public static Class[] getParameters(Object[] jobArs) {
        if (jobArs == null) {
            return null;
        }
        Class[] parameterType = null;
        parameterType = new Class[jobArs.length];
        for (int i = 0; i < jobArs.length; i++) {
            parameterType[i] = String.class;
        }
        return parameterType;
    }

}
