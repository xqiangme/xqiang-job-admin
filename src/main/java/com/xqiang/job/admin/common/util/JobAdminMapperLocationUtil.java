package com.xqiang.job.admin.common.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mengqiang
 */
public class JobAdminMapperLocationUtil {

    private List<Resource> resourceList;

    public static JobAdminMapperLocationUtil init() {
        return init(10);
    }

    public static JobAdminMapperLocationUtil init(int size) {
        JobAdminMapperLocationUtil mapperLocationsUtil = new JobAdminMapperLocationUtil();
        mapperLocationsUtil.setResourceList(new ArrayList<>(size));
        return mapperLocationsUtil;
    }

    public JobAdminMapperLocationUtil addMapperLocations(String mapperLocations) throws IOException {
        if (null == resourceList) {
            resourceList = new ArrayList<>(10);
        }
        resourceList.addAll(Arrays.asList(new PathMatchingResourcePatternResolver().getResources(mapperLocations)));
        return this;
    }

    public Resource[] build() {
        if (null == resourceList) {
            return new Resource[0];
        }
        Resource[] resources = new Resource[resourceList.size()];
        resourceList.toArray(resources);
        return resources;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }
}