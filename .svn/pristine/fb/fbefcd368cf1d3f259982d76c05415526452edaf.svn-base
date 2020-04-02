/**
 * @Project : 병행검증 솔루션
 * @Class : DocketFacory.java
 * @Description :
 * @Author : kimdoy
 * @Since : Mar 27, 2020
 * @Copyright LG CNS
 *------------------------------------------------------
 *      Dodification Information
 *------------------------------------------------------
 *  Date        Modifier       Reason
 *------------------------------------------------------
 *  Mar 27, 2020     kimdoy         Initial
 *------------------------------------------------------
 */   
package com.lgcns.api.swagger;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Predicate;
import com.lgcns.api.helper.PropertiesHelper;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class DocketFacory implements FactoryBean<Docket>, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocketFacory.class);

	private String groupName;
	private String basePackage;
	private String pathMapping;
	private String title;
	private String description;
	private String version;
	
	@Override
	public Docket getObject() throws Exception {
		LOGGER.warn("Swagger Group is created :: [{}]::[{}]", groupName, basePackage);
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName(groupName) /** Select a spec **/
				.apiInfo(apiInfo())
				.enable(PropertiesHelper.getBoolean("swagger.enabled"))
				.select()
//				.apis(exactPackage(basePackage))
				.apis(RequestHandlerSelectors.basePackage(basePackage))		
//				.paths(PathSelectors.ant("/api/products/**"))
				.paths(PathSelectors.any())
				.build()
				.pathMapping(pathMapping)
				.directModelSubstitute(LocalDate.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(PropertiesHelper.getBoolean("swagger.use.default.response.messages"))
				.enableUrlTemplating(PropertiesHelper.getBoolean("swagger.enable.url.templating"));	
	}

	@Override
	public Class<?> getObjectType() {
		return Docket.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (pathMapping == null) {
			this.pathMapping = "/";
		}
		if (groupName == null) {
			String errmsg = "Group name should be set";
			throw new RuntimeException(errmsg);
		}
		
		if (basePackage == null) {
			String errmsg = "Base Package name should be set";
			throw new RuntimeException(errmsg);
		}
		
		
		if (title == null) {
			String errmsg = "Title should be set";
			throw new RuntimeException(errmsg);
		}
		
		
		if (description == null) {
			String errmsg = "Description should be set";
			throw new RuntimeException(errmsg);
		}
		
		
		if (version == null) {
			String errmsg = "Version name should be set";
			throw new RuntimeException(errmsg);
		}
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(title)
						.description(description)
						.version(version)
						.build();
	}

	@SuppressWarnings("deprecation")
	private static Predicate<RequestHandler> exactPackage(final String pkg) {
		return input -> input.declaringClass().getPackage().getName().contains(pkg);
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setPathMapping(String pathMapping) {
		this.pathMapping = pathMapping;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}

/** 
http://www.mkjava.com/tutorial/spring-and-swagger/

package com.listfeeds.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfiguration {

    private static String AUTHENTICATION_KEY_NAME = "authorization";
    private static String AUTHENTICATION_KEY_VALUE = "@fromSwagger@";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getListFeedsMetaData())
                .securitySchemes(new ArrayList<ApiKey>() {
                    {
                        apiKey();
                    }
                }).securityContexts(new ArrayList<SecurityContext>() {
                    {
                        securityContext();
                    }
                });
    }

    private ApiInfo getListFeedsMetaData() {

        return new ApiInfoBuilder()
                .title("ListFeeds")
                .description("ListFeeds API for NLP")
                .version("1.0.0")
                .build();
    }

  
    private ApiKey apiKey() {
        return new ApiKey("swaggerApiKey", AUTHENTICATION_KEY_NAME, "header");
    }

 
    private SecurityContext securityContext() {


        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        List<SecurityReference> securityReferences = new ArrayList<SecurityReference>();
        securityReferences.add(new SecurityReference("swaggerApiKey", authorizationScopes));


        return SecurityContext.builder()
                .securityReferences(securityReferences)
                .forPaths(PathSelectors.regex("/anyPath.*"))
                .build();
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(
            "test-app-client-id",
            "test-app-client-secret",
            "test-app-realm",
            "test-app",
            AUTHENTICATION_KEY_VALUE,
            ApiKeyVehicle.HEADER, 
            AUTHENTICATION_KEY_NAME, 
            "," scope separator);
      }
} 
**/