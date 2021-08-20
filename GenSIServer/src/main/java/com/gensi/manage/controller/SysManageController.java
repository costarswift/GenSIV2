package com.gensi.manage.controller;

import com.gensi.manage.config.GsLogConfig;
import com.gensi.manage.entity.GsmanageWithBLOBs;
import com.gensi.manage.service.GsrequestService;
import com.gensi.manage.service.SysManageService;
import com.gensi.manage.utils.ConfigUtils;
import com.gensi.manage.utils.GsConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sysManage")
@ApiIgnore
public class SysManageController {
    @Resource
    private SysManageService sysManageService;

    @Resource
    private GsrequestService gsrequestService;

    @Autowired
    private GsLogConfig gsLogConfig;

    private Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping(value = "/newSys", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object newSys(@RequestBody GsmanageWithBLOBs gsmanage) throws Exception {
        logger.info("SysManageAction.newSys: gsmanage=> " + gsmanage);
        Map<String, Object> res = new HashMap<String, Object>();
        int opRes = sysManageService.createSys(gsmanage);
        if (opRes > 0) {
            res.put("code", 1);
            res.put("desc", "外围系统创建成功");
        } else {
            res.put("code", 0);
            res.put("desc", "外围系统创建失败。");
        }
        ConfigUtils.reLoadConfig();
        return res;
    }

    @RequestMapping(value = "/deleteSys", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object deleteSys(String sysid) throws Exception {
        logger.info("SysManageAction.deleteSys: sysid=> " + sysid);
        int opRes = sysManageService.deleteSysById(sysid);
        return opRes;
    }

    @RequestMapping(value = "/querySys", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object querySys() throws Exception {
        logger.info("SysManageAction.querySys: ");
        List<GsmanageWithBLOBs> res = sysManageService.queryAllSys();
        return res;
    }

    @RequestMapping(value = "/passKey", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object passKey(String passKey) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        if (passKey.equals(GsConstants.PASSKEY)) {
            res.put("result", "0");
        } else {
            res.put("result", "1");
        }
        return res;
    }

    @RequestMapping(value = "/getHistory", produces = "application/json;charset=UTF-8", method = {RequestMethod
            .POST})
    public Object getHistory(HttpServletRequest request) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("transId", request.getParameter("transId"));
        paras.put("serviceCode", request.getParameter("serviceCode"));
        res.put("code", "1");
        res.put("desc", "获取数据成功！");
        res.put("data", gsrequestService.getAll(paras));
        return res;
    }

    /**
     * 获取指定目录下transId文件列表
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTransIdLogFiles", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object getTransIdLogFiles(HttpServletRequest request) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            //transId命名的日志文件存放的根目录
            String transIdLogFilesRootPath = gsLogConfig.getTransLogDir();
            String transId = request.getParameter("transId");
            //存储返回结果
            List<Map<String, String>> dataList = new ArrayList<>();
            List<File> files = (List<File>) FileUtils.listFiles(new File(transIdLogFilesRootPath), new String[]{GsConstants.LOG_SUFFIX}, false);
            if (files != null && !files.isEmpty()) {
                //按文件最后修改时间倒序排序
                Collections.sort(files, (file1,file2) ->{
                	return (int) (file2.lastModified() - file1.lastModified());
                });
                //如果transId不为空，只展现该transId的文件。 否则，返回最近的100个日志文件
                Predicate<File> logFileFilter = (f)->{
                	return f.getName().contains(transId.trim());
                };
                if(StringUtils.isNotEmpty(transId)) {
                	files = files.stream().filter(logFileFilter).collect(Collectors.toList());
                }else if(files.size()>GsConstants.LOG_FILE_MAX_SIZE) {
                	files = files.subList(0, GsConstants.LOG_FILE_MAX_SIZE);
                }
                for (File file : files) {
                    Map<String, String> data = new HashMap<>();
                    data.put("transIdLogFileName", file.getName());
                    data.put("transIdLogFilePath", file.getAbsolutePath());
                    dataList.add(data);
                }
            }
            res.put("code", "1");
            res.put("desc", "获取数据成功！");
            res.put("data", dataList);
            res.put("logPath", transIdLogFilesRootPath);
        } catch (Exception ex) {
            res.put("code", "0");
            res.put("desc", "获取数据失败！");
            logger.error(ex.getMessage(), ex);
        }
        return res;
    }

    /**
     * 获取指定transId文件的内容
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTransIdLogFileContent", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object getTransIdLogFileContent(HttpServletRequest request) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            //transId命名的日志文件存放的根目录
            String transIdLogFilePath = request.getParameter("transIdLogFilePath");
            String transIdLogFileName = request.getParameter("transIdLogFileName");
            String content = FileUtils.readFileToString(new File(transIdLogFilePath), "utf-8");
            Map<String, String> data = new HashMap<>();
            data.put("transIdLogFilePath", transIdLogFilePath);
            data.put("transIdLogFileName", transIdLogFileName);
            data.put("transIdLogFileContent", content);
            res.put("code", "1");
            res.put("desc", "获取数据成功！");
            res.put("data", data);
        } catch (Exception ex) {
            res.put("code", "0");
            res.put("desc", "获取数据失败！");
            logger.error(ex.getMessage(), ex);
        }
        return res;
    }
}
