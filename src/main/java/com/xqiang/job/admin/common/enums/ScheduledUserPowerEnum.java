package com.xqiang.job.admin.common.enums;

import com.xqiang.job.admin.common.util.JobAdminStringUtils;

import java.util.*;

/**
 * 用户权限枚举
 *
 * @author mengq
 */
public enum ScheduledUserPowerEnum {

    /* 默认权限  */
    DEFAULT("DEFAULT", "/login/getUserInfo,/user-person,/job/getHomeCount,/user/getUserPersonDetail,/task-corn"),

    /* 人员管理  */
    /**
     * 人员管理列表
     */
    USER_MANGER("userManger", "/user-list,/user/listPage"),

    /**
     * 添加人员
     */
    USER_ADD("userAdd", "/user-add,/user/save"),

    /**
     * 修改人员
     */
    USER_UPDATE("userUpdate", "/user/update,/user/getUserDetail"),

    /**
     * 修改人员密码
     */
    USER_UPDATE_PWD("userUpdatePwd", "/user/update-pwd"),

    /**
     * 修改人员权限
     */
    USER_UPDATE_POWER("userUpdatePower", "/user/getUserDetail,/user/update-power"),

    /**
     * 删除人员
     */
    USER_DELETE("userDelete", "/user/delete"),

    /* 任务管理  */
    /**
     * 任务管理列表
     */
    TASK_MANGER("taskManger", "/job/listPage"),

    /**
     * 添加任务
     */
    TASK_ADD("taskAdd", "/job/save"),

    /**
     * 修改任务
     */
    TASK_UPDATE("taskUpdate", "/job/getJobDetail,/job/update"),

    /**
     * 启动|停用任务
     */
    TASK_CHANGE_STATUS("taskChangeStatus", "/job/start,/job/stop"),

    /**
     * 删除任务
     */
    TASK_DELETE("taskDelete", "/job/delete"),

    /* 日志管理  */
    /**
     * 日志管理列表
     */
    TASK_LOG_MANGER("taskLogManger", "/job-log/listPage,/job-log/getLogDetail"),

    ;

    private String powerKey;
    private String powerUrls;

    ScheduledUserPowerEnum(String powerKey, String powerUrls) {
        this.powerKey = powerKey;
        this.powerUrls = powerUrls;
    }

    public String getPowerKey() {
        return powerKey;
    }

    public String getPowerUrls() {
        return powerUrls;
    }

    public static String getAllMenus() {
        StringBuilder menus = new StringBuilder(USER_MANGER.getPowerKey());
        menus.append(",").append(TASK_MANGER.getPowerKey());
        menus.append(",").append(TASK_LOG_MANGER.getPowerKey());
        return menus.toString();
    }

    public static String getAllFunctions() {
        StringBuilder menus = new StringBuilder(USER_ADD.getPowerKey());
        menus.append(",").append(USER_UPDATE.getPowerKey());
        menus.append(",").append(USER_UPDATE_PWD.getPowerKey());
        menus.append(",").append(USER_UPDATE_POWER.getPowerKey());
        menus.append(",").append(USER_DELETE.getPowerKey());
        menus.append(",").append(TASK_ADD.getPowerKey());
        menus.append(",").append(TASK_UPDATE.getPowerKey());
        menus.append(",").append(TASK_CHANGE_STATUS.getPowerKey());
        menus.append(",").append(TASK_DELETE.getPowerKey());
        return menus.toString();
    }

    /**
     * 项目根路径前缀
     */
    public static String PROJECT_ROOT_PATH = "/job-admin";

    public static Set<String> getAllPower() {
        Set<String> powers = new HashSet<>(ScheduledUserPowerEnum.values().length);
        for (ScheduledUserPowerEnum powerEnum : ScheduledUserPowerEnum.values()) {
            if (powerEnum.getPowerUrls().contains(",")) {
                String[] split = powerEnum.getPowerUrls().split(",");
                if (split.length > 0) {
                    for (String url : split) {
                        powers.add(PROJECT_ROOT_PATH + url);
                    }
                }
            } else {
                powers.add(PROJECT_ROOT_PATH + powerEnum.getPowerUrls());
            }
        }
        return powers;
    }

    public static Map<String, Set<String>> getPowerGroupMap() {
        Map<String, Set<String>> powerGroupMap = new HashMap<>(ScheduledUserPowerEnum.values().length);
        Set<String> powerSet;
        for (ScheduledUserPowerEnum powerEnum : ScheduledUserPowerEnum.values()) {
            powerSet = new HashSet<>();
            if (powerEnum.getPowerUrls().contains(",")) {
                String[] split = powerEnum.getPowerUrls().split(",");
                if (split.length > 0) {
                    for (String url : split) {
                        powerSet.add(PROJECT_ROOT_PATH + url);
                    }
                }
            } else {
                powerSet.add(PROJECT_ROOT_PATH + powerEnum.getPowerUrls());
            }
            powerGroupMap.put(powerEnum.getPowerKey(), powerSet);
        }
        return powerGroupMap;
    }

    public static Set<String> getPowerByMenusAndFunctions(String menus, String functions) {
        Set<String> powerKeys = new HashSet<>();
        if (JobAdminStringUtils.isNotBlank(menus)) {
            Collections.addAll(powerKeys, menus.split(","));
        }
        if (JobAdminStringUtils.isNotBlank(functions)) {
            Collections.addAll(powerKeys, functions.split(","));
        }
        return getPowerByKeys(powerKeys);
    }

    public static Set<String> getPowerByKeys(Set<String> powerKeys) {
        Map<String, Set<String>> powerGroupMap = ScheduledUserPowerEnum.getPowerGroupMap();
        //默认只有基础权限
        Set<String> powerSet = new HashSet<>(powerGroupMap.get(DEFAULT.getPowerKey()));
        if (null == powerKeys || powerKeys.size() == 0) {
            return powerSet;
        }
        for (String powerKey : powerKeys) {
            if (!powerGroupMap.containsKey(powerKey)) {
                continue;
            }
            powerSet.addAll(powerGroupMap.get(powerKey));
        }
        return powerSet;
    }
}
