package com.xqiang.job.admin.core.quartz;

import com.xqiang.job.admin.common.util.JobAdminRandomUtils;
import com.xqiang.job.admin.common.util.JobAdminStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * Quartz 定时任务核心 Bean
 *
 * @author mengq
 */
@Slf4j
public class QuartzJobBean implements Job {

    public static final String JOB_ID = "jobId";
    public static final String TARGET_CLASS = "class";
    public static final String TARGET_METHOD = "method";
    public static final String TARGET_ARGUMENTS = "arguments";

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
        log.info("[ QuartzJob ] >> job end jobId:{} , targetClass:{} ,targetMethod:{} , methodArgs:{} , time:{} ms"
                , jobId, targetClass, targetMethod, methodArgs, (System.currentTimeMillis() - startTime));
    }

}
