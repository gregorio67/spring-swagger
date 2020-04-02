package com.lgcns.api.base;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class BaseController {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
//	@Resource(name = "defaultDateFormat")
//	protected SimpleDateFormat defaultDateFormat;
	
	@Resource(name="restTemplate")
	protected RestTemplate restTemplate;
	
}
