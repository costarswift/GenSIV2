package com.gensi.manage.config;

import com.gensi.manage.utils.GsConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gs.log")
public class GsLogConfig {
	
	private Logger fLogger = Logger.getLogger(this.getClass());

	private String transLogDir;
	
//	private String LOG_SUFFIX=".gsTransLog";
	
//	private FileAppender fa = null;
	
	public String getTransLogDir() {
		return transLogDir;
	}

	public void setTransLogDir(String transLogDir) {
		this.transLogDir = transLogDir;
	}
	
	public void addTransLogAppender(Logger logger,String transId){
		String targetFileName=  this.getTransLogDir()+"/"+transId+"."+GsConstants.LOG_SUFFIX;
		fLogger.info("追加trans日志到"+targetFileName);
		//log4j Appender 可以重复进行add或者remove，因此不需要判断是否已经有fa
		if(null != logger && StringUtils.isNotEmpty(transId)){
			logger.removeAppender("fa");
//			FileAppender fa = (FileAppender)logger.getAppender("fa");
//			if(null == fa){
//				FileAppender fa = new FileAppender();
//				PatternLayout layout = new PatternLayout();  
//		        String conversionPattern = "%d %-5p %c [%L] - %m%n";  
//		        layout.setConversionPattern(conversionPattern); 
//		        fa.setLayout(layout);//输出形式
//		        fa.setName("fa");//Appender别名
		        //fa.setAppend(true);是否追加文件,默认为true
//			}
			FileAppender fa = GsConstants.getTransLogAppender();
			fa.setFile(targetFileName);
			fa.activateOptions();//将配置生效
			logger.removeAllAppenders();
			logger.setAdditivity(true);
			
			logger.addAppender(fa);
		}
	}
	
	public void removeTransLogAppender(Logger logger){
		//log4j Appender 可以重复进行add或者remove，因此不需要判断是否已经有fa
		logger.removeAppender("fa");
	}
	
}
