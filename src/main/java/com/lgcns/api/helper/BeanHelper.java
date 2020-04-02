package com.lgcns.api.helper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class BeanHelper{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanHelper.class);

	/** Application context **/
	private static WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();

	private BeanHelper() {
		/** Do nothing **/
	}
	
	public static WebApplicationContext appContext() throws Exception {
		/** Check application context exists **/
		if (context == null) {
			throw new RuntimeException("Fail to get application context from ContextLoader");
		}
		
//		AbstractRefreshableWebApplicationContext refreshContext = (AbstractRefreshableWebApplicationContext) context;
//		ConfigurableListableBeanFactory  beanFactory = refreshContext.getBeanFactory();
//		beanFactory.destroyBean("dataSource", BeanHelper.getBean("dataSource"));
//		beanFactory.registerSingleton("dataSource", "");
		//		DefaultSingletonBeanRegistry  beanFactory = (DefaultSingletonBeanRegistry)refreshContext.getBeanFactory();
//		DefaultSingletonBeanRegistry registry = beanFactory.get
		return context;
	}

	/**
	 * Return servlet context
	 * @return
	 * @throws Exception
	 */
	public ServletContext servletContext() throws Exception {
		return appContext().getServletContext();
	}

	/**
	 * Return spring bean
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) throws Exception {
		
		/** Search bean from context **/
		T bean = (T) appContext().getBean(beanName);
		if (bean == null) {
			String errmsg = String.format("Can't find bean from context :: [%s]", beanName);
			throw new RuntimeException(errmsg);
		}
		
		return bean;
	}

	/**
	 * Return spring bean with class
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T  getBean(Class<T> clazz)  throws Exception {
		
		String[] beanNames = appContext().getBeanNamesForType(clazz);
		
		if (beanNames == null) {
			String errmsg = String.format("Fail to get bean with [%s]", clazz.getName());
			throw new RuntimeException(errmsg);
		}
		
		return getBean(beanNames[0]);
	}	
	
	/**
	 * Extract bean names with class
	 * @param clazz
	 * @return
	 */
	public static List<String> getBeanName(Class<?> clazz)  throws Exception {
			
		String[] beanNames = appContext().getBeanNamesForType(clazz);
		
		List<String> retBeanNames = new ArrayList<String>();
		
		for (String beanName : beanNames) {
			retBeanNames.add(beanName.replaceAll("&", ""));
		}
		
		return retBeanNames;
	}
	
	
	/**
	 * Return active spring profiler 
	 * @return
	 * @throws Exception
	 */
	public static String[] getActiveProfile() throws Exception {
		return appContext().getEnvironment().getActiveProfiles();
	}
	


	/**
	 * Register bean
	 * @param bean
	 * @param beanName
	 * @throws Exception
	 */
	public static void registerBean(final String beanName, final Class<?> bean) throws Exception {
		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
		try {
			Object obj = beanFactory.getBean(beanName);
			if (obj != null) {
				LOGGER.warn("{} bean is already registered", beanName);
			}
		}
		catch(Exception ex) {
			beanFactory.registerSingleton(beanName, bean.newInstance());			
			LOGGER.info("{} bean is registered", beanName);			
		}
		/**
		BeanDefinitionRegistry registry = ((BeanDefinitionRegistry )beanFactory);
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(bean);
		beanDefinition.setLazyInit(false);
		beanDefinition.setAbstract(false);
		beanDefinition.setAutowireCandidate(true);
		beanDefinition.setScope("session");
		registry.registerBeanDefinition(beanName, beanDefinition);
		**/
	}
	
	/**
	 * Publish application event
	 * @param event
	 * @throws Exception
	 */
	public static void publishEvent(ApplicationEvent event) throws Exception {
		context.publishEvent(event);
	}
	
	/**
	 * Support resource type
	 * @author exbnsd800
	 *
	 */
	public enum ResourceType {
		CLASSPATH,
		FILE
	}
}
