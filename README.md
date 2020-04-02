# spring-swagger
This is example for spring swagger with restful API.
This project was build with spring MVC and @EnableSwagger2 annotation.
And also add resources for swagger in configuration file


## 1. Configuration for swagger
The source below is for configruation default docket and adding resource for swagger.
The spring swagger support to add many dockets for grouping restful API.
The configuration source only set default docket for app restful API.

<pre>
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
  
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
	}  

}
</pre>

## 2. Docket for swagger
A docket is categorized with base package.
In this sample there are two dockets, one is for users, and the other is product.
To create new docket, refer to below source.
<pre>
mport java.time.LocalDate;

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
				.apis(RequestHandlerSelectors.basePackage(basePackage))		
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
</pre>

The factory create docket bean with grouop name, package name, title and version.

Let's setting to docket with xml configuration using factory.
<pre>
	<bean id="userDocket" class="com.lgcns.api.swagger.DocketFacory">
		<property name="groupName" value="User" />
		<property name="basePackage" value="com.lgcns.api.user" />
		<property name="pathMapping" value="/" />
		<property name="title" value="User API" />
		<property name="description" value="User API" />
		<property name="version" value="1.0" />		
	</bean>

	<bean id="productDocket" class="com.lgcns.api.swagger.DocketFacory">
		<property name="groupName" value="Product" />
		<property name="basePackage" value="com.lgcns.api.product" />
		<property name="pathMapping" value="/" />
		<property name="title" value="Product API" />
		<property name="description" value="Product API" />
		<property name="version" value="1.0" />		
	</bean>
</pre>

This xml file create two docket, one is for user, the other is for product.




