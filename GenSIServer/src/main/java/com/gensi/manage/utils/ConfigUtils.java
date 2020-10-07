package com.gensi.manage.utils;

import com.gensi.manage.entity.GsmanageWithBLOBs;
import com.gensi.manage.service.SysManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置工具类
 *
 * @author shencheng
 */
@Component
public class ConfigUtils {
    /**
     * 接口服务管理集合
     */
    private static Map<String, GsmanageWithBLOBs> gsmanageMap = new HashMap<String, GsmanageWithBLOBs>();

    @Autowired
    private SysManageService sysManageService;

    private static SysManageService sysManageServiceStatic;

    /**
     * 初始化配置
     */
    @PostConstruct
    public void init() {
        sysManageServiceStatic = sysManageService;
        List<GsmanageWithBLOBs> gsmanages = sysManageService.queryAllSys();
        if (gsmanages != null && !gsmanages.isEmpty()) {
            for (GsmanageWithBLOBs gsmanage : gsmanages) {
            	gsmanageMap.put(gsmanage.getSysid(), gsmanage);
            }
        }
    }

    /**
     * 重新加载配置
     */
    public static void reLoadConfig() {
    	gsmanageMap.clear();
        List<GsmanageWithBLOBs> gsmanages = sysManageServiceStatic.queryAllSys();
        if (gsmanages != null && !gsmanages.isEmpty()) {
            for (GsmanageWithBLOBs gsmanage : gsmanages) {
            	gsmanageMap.put(gsmanage.getSysid(), gsmanage);
            }
        }
    }

    /**
     * 获取接口服务配置
     *
     * @param sysId 系统标识
     * @return
     */
    public static GsmanageWithBLOBs getFtmanage(String sysId) {
        return gsmanageMap.get(sysId);
    }

    /**
     * 检查系统标识是否有效
     *
     * @param sysId 系统标识
     * @return 有效返回true, 无效返回false
     */
    public static boolean checkSysId(String sysId) {
        return gsmanageMap.containsKey(sysId);
    }
}
