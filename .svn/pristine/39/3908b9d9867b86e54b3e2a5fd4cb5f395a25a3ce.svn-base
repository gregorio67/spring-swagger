/**
 * @Project : 병행검증 솔루션
 * @Class : SwaggerConfig.java
 * @Description :
 * @Author : kimdoy
 * @Since : Mar 26, 2020
 * @Copyright LG CNS
 *------------------------------------------------------
 *      Dodification Information
 *------------------------------------------------------
 *  Date        Modifier       Reason
 *------------------------------------------------------
 *  Mar 26, 2020     kimdoy         Initial
 *------------------------------------------------------
 */
package com.lgcns.api.config;

import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.lgcns.api.helper.PropertiesHelper;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

	/** 
	 * Default swagger Docket
	 * @param swaggerProperties
	 * @return
	 */
	@Bean
	public Docket defaultDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.enable(PropertiesHelper.getBoolean("swagger.enabled"))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().pathMapping("/")
				.directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(PropertiesHelper.getBoolean("swagger.use.default.response.messages"))
				.enableUrlTemplating(PropertiesHelper.getBoolean("swagger.enable.url.templating"));
	}

//	@Bean
//	public Docket userApi(SwaggerProperties swaggerProperties) {
//		return makeDocket("User", "com.lgcns.api.user", swaggerProperties);
//	}
//	
//
//	@Bean
//	public Docket productApi(SwaggerProperties swaggerProperties) {
//		return makeDocket("Product", "com.lgcns.api.product", swaggerProperties);
//	}
//
//	private Docket makeDocket(String groupName, String packageName, SwaggerProperties swaggerProperties) {
//		return new Docket(DocumentationType.SWAGGER_2).groupName(groupName) /** Select a spec **/
//				.apiInfo(apiInfo(swaggerProperties)).enable(Boolean.valueOf(swaggerProperties.getEnabled())).select()
//				.apis(exactPackage(packageName)).paths(PathSelectors.any()).build().pathMapping("/")
//				.directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
//				.useDefaultResponseMessages(Boolean.valueOf(swaggerProperties.getUseDefaultResponseMessages()))
//				.enableUrlTemplating(Boolean.valueOf(swaggerProperties.getEnableUrlTemplating()));
//	}

	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
				.deepLinking(PropertiesHelper.getBoolean("swagger.deeplinking"))
				.displayOperationId(PropertiesHelper.getBoolean("swagger.display.operation.id"))
				.defaultModelsExpandDepth(PropertiesHelper.getInt("swagger.default.models.expand.depth"))
				.defaultModelExpandDepth(PropertiesHelper.getInt("swagger.default.model.expand.depth"))
				.defaultModelRendering(ModelRendering.EXAMPLE)
				.displayRequestDuration(PropertiesHelper.getBoolean("swagger.display.request.duration"))
				.docExpansion(DocExpansion.NONE)
				.filter(PropertiesHelper.getBoolean("swagger.filter"))
				.maxDisplayedTags(PropertiesHelper.getInt("swagger.max.displayed.tags"))
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(PropertiesHelper.getBoolean("swagger.show.extensions"))
				.tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
				.validatorUrl(null)
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(PropertiesHelper.getString("swagger.title"))
				.description(PropertiesHelper.getString("swagger.description"))
				.version(PropertiesHelper.getString("api.version"))
				.build();
	}

//	private static Predicate<RequestHandler> exactPackage(final String pkg) {
//		return input -> input.declaringClass().getPackage().getName().contains(pkg);
//	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// registry.addRedirectViewController("/v2/api-docs",
		// "/api/v2/api-docs").setKeepQueryParams(true);
		// registry.addRedirectViewController("/swagger-resources/configuration/ui",
		// "/api/resources/configuration/ui");
		// registry.addRedirectViewController("/swagger-resources/configuration/security",
		// "/api/resources/configuration/security");
		// registry.addRedirectViewController("/swagger-resources", "/api/resources");
		registry.addRedirectViewController("/api", "/swagger-ui.html");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");

	}

	
/**
	 
@Bean
public Docket api1() {

// here tags is optional, it just adds a description in the UI
// by default description is class name, so if you use same tag using 
// `@Api` on different classes it will pick one of the class name as 
// description, so better define your own description for them
return new Docket(DocumentationType.SWAGGER_2)
    .tags(new Tag("users", "users related"), 
          new Tag("products", "products related"))
    .select()
    .apis(RequestHandlerSelectors.basePackage("com.github"))
    .build();
}

**/
/**
 * @Bean
public Docket api1() {

return new Docket(DocumentationType.SWAGGER_2)
    .groupName("users")
    .select()
    .paths(PathSelectors.ant("/api/users/**"))
    .build();
}

@Bean
public Docket api2() {

return new Docket(DocumentationType.SWAGGER_2)
    .groupName("products")
    .select()
    .paths(PathSelectors.ant("/api/products/**"))
    .build();
}
 */
	
}
