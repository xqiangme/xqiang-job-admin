package com.xqiang.job.admin.core.quartz;

import com.xqiang.job.admin.common.util.JobAdminRandomUtils;
import com.xqiang.job.admin.common.util.JobAdminStringUtils;
import com.xqiang.job.admin.core.config.ApplicationContextHelper;
import com.xqiang.job.admin.core.dao.bean.ScheduledQuartzJobInfo;
import com.xqiang.job.admin.core.dao.mapper.ScheduledQuartzJobMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * Quartz 定时任务核心 Bean
 *
 * @author mengq
 */
public class QuartzJobBean implements Job {
    private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);

    public static final String JOB_ID = "jobId";
    public static final String TARGET_CLASS = "class";
    public static final String TARGET_METHOD = "method";
    public static final String TARGET_ARGUMENTS = "arguments";
    public static final String PROJECT_KEY = "projectKey";

    private static ApplicationContext applicationContext;

    public static void setAc(ApplicationContext applicationContext) {
        QuartzJobBean.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过类名和方法名去获取目标对象，再通过反射执行 类名和方法名保存在jobDetail中
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //任务ID
        Integer jobId = (Integer) context.getMergedJobDataMap().get(JOB_ID);
        //项目key
        String projectKey = (String) context.getMergedJobDataMap().get(PROJECT_KEY);
        //目标类名
        String targetClass = (String) context.getMergedJobDataMap().get(TARGET_CLASS);
        //目标方法
        String targetMethod = (String) context.getMergedJobDataMap().get(TARGET_METHOD);
        //方法参数
        String methodArgs = (String) context.getMergedJobDataMap().get(TARGET_ARGUMENTS);

        if (JobAdminStringUtils.isBlank(targetClass) || JobAdminStringUtils.isBlank(targetMethod)) {
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            //任务日志标识
            MDC.put("logId", JobAdminRandomUtils.randomAlphanumeric(15));
            log.info("[ QuartzJob ] >> job start jobId:{} , targetClass:{} ,targetMethod:{} , methodArgs:{}", jobId, targetClass, targetMethod, methodArgs);
            //任务参数
            Object[] jobArs = QuartzSchedulerUtil.getJobArgs(methodArgs);
            Object target = applicationContext.getBean(targetClass);
            if (null != target) {
                Class tc = target.getClass();
                Class[] parameterType = QuartzSchedulerUtil.getParameters(jobArs);
                //执行任务方法
                Method method = tc.getDeclaredMethod(targetMethod, parameterType);
                if (null != method) {
                    method.invoke(target, jobArs);
                }
            }
        } catch (Exception e) {
            log.error("[ QuartzJob ] >> job execute exception jobId:{} , targetClass:{} ,targetMethod:{} , methodArgs:{}"
                    , jobId, targetClass, targetMethod, methodArgs, e);
            throw new JobExecutionException(e);
        }

        //执行完成后更新-任务最后执行时间
        this.updateAfterRun(jobId, projectKey);

        log.info("[ QuartzJob ] >> job end jobId:{} , targetClass:{} ,targetMethod:{} , methodArgs:{} , time:{} ms"
                , jobId, targetClass, targetMethod, methodArgs, (System.currentTimeMillis() - startTime));
    }

    private void updateAfterRun(Integer jobId, String projectKey) {
        try {
            ScheduledQuartzJobMapper scheduledQuartzJobMapper = ApplicationContextHelper.getContext().getBean(ScheduledQuartzJobMapper.class);
            ScheduledQuartzJobInfo jobInfo = new ScheduledQuartzJobInfo();
            jobInfo.setLastRunTimestamp(System.currentTimeMillis());
            jobInfo.setId(jobId);
            jobInfo.setProjectKey(projectKey);
            //更新最后执行时间
            scheduledQuartzJobMapper.updateByProjectAndId(jobInfo);
        } catch (Exception e) {
            log.error("[ QuartzJob ] >> job updateAfterRun exception jobId:{} , projectKey:{} ", jobId, projectKey, e);
        }
    }
}
