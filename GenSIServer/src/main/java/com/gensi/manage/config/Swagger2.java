package com.gensi.manage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger ui configuration
 * @author roykignw
 * @version 1.0.0
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gensi"))
                .paths(PathSelectors.any())
                .build();
    }
    
//    public UiConfiguration(
//            String validatorUrl,
//            String docExpansion,
//            String apiSorter,
//            String defaultModelRendering,
//            String[] supportedSubmitMethods,
//            boolean enableJsonEditor,
//            boolean showRequestHeaders) {
//      this.validatorUrl = validatorUrl;
//      this.docExpansion = docExpansion;
//      this.apiSorter = apiSorter;
//      this.defaultModelRendering = defaultModelRendering;
//      this.enableJsonEditor = enableJsonEditor;
//      this.showRequestHeaders = showRequestHeaders;
//      this.supportedSubmitMethods = supportedSubmitMethods;
//    }
    
//    public UiConfiguration(String validatorUrl) {
//        this(validatorUrl, "none", "alpha", "schema", Constants.DEFAULT_SUBMIT_METHODS, false, true);
//      }
//    @Bean
//    public UiConfiguration sysSwaggerUIConfig(){
//    	UiConfiguration sysUIConfig = new UiConfiguration(null,"none","alpha","schema",Constants.DEFAULT_SUBMIT_METHODS,true,true);
//    	return sysUIConfig;
//    }
    
    //可以通过注入两个Bean来修改swaggerui的一些默认配置
//    springfox.documentation.swagger.web.ApiResourceController
//    @Autowired(required = false)
//    private SecurityConfiguration securityConfiguration;
//    @Autowired(required = false)
//    private UiConfiguration uiConfiguration;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GenSI")
                .description("通用服务接入系统")
                .contact("roykingw")
                .version("1.0")
                .build();
    }

}
