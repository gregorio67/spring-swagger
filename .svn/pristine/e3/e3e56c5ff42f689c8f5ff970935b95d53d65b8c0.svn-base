package com.lgcns.api.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.lgcns.api.exception.SysException;


public class PropertiesHelper extends PropertyPlaceholderConfigurer {
	
	/**LOGGER SET **/
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesHelper.class);
    private final static Pattern LTRIM = Pattern.compile("^\\s+");
	private final static Pattern RTRIM = Pattern.compile("\\s+$");


    /** System  properties has higher priority **/
    private int springSystemPropertiesMode = PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_FALLBACK;

	private static Map<String, String> propertiesMap;
	

    private PropertiesHelper() {
    	/** Do nothing **/
    }
    
    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }


    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);
        
        propertiesMap = new HashMap<String, String>();
        if (props == null) {
        	String errmsg = String.format("There is no properties for PropertiesHelper, please check your configuration");
        	throw new SysException(errmsg);
        }
        
        for (Object str : props.keySet()) {

        	String key = String.valueOf(str);            
        	/** Replace left and right space **/
            key =  LTRIM.matcher(key).replaceAll("");
            key = RTRIM.matcher(key).replaceAll("");
            
            String value = resolvePlaceholder(key, props, springSystemPropertiesMode);
        
            /** Replace left and right space **/
            value =  LTRIM.matcher(value).replaceAll("");
            value = RTRIM.matcher(value).replaceAll("");
            
            propertiesMap.put(key, value);
     
            if (LOGGER.isDebugEnabled()) {
            	LOGGER.debug("Load Properties :" +  key + ":" + value);
            }
        }
    }

    /**
     * This method return value with the name from properties map
     * @param name propertiy name
     * @return
     */
    public static String getString(String name)  {
        return propertiesMap.get(name) != null ? propertiesMap.get(name) : null;	
    }

    /**
     * Return int
     * @param name
     * @return
     */
    public static int getInt(String name) {
        return propertiesMap.get(name) != null ? Integer.parseInt(propertiesMap.get(name)) : null;	    	
    }

    /** Return long 
     * 
     * @param name
     * @return
     */
    public static long getLong(String name) {
        return propertiesMap.get(name) != null ? Long.parseLong(propertiesMap.get(name)) : null;	    	
    }

    /**
     * Return float
     * @param name
     * @return
     */
    public static float getFloat(String name) {
        return propertiesMap.get(name) != null ? Float.parseFloat(propertiesMap.get(name)) : null;	    	
    }
   
    /** Return boolean
     * 
     * @param name
     * @return
     */
    public static boolean getBoolean(String name) {
    	return  propertiesMap.get(name) !=null ? propertiesMap.get(name).equalsIgnoreCase("true") ? true : false : false;
    }
    
    /**
     * Return BigDecimal
     * @param name
     * @return
     */
    public static BigDecimal getDecimal(String name) {
    	  return propertiesMap.get(name) != null ? new BigDecimal(propertiesMap.get(name)) : null;	    	
    }
}
