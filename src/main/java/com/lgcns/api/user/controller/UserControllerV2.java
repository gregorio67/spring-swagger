/**
 * @Project : 병행검증 솔루션
 * @Class : ProductController.java
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
package com.lgcns.api.user.controller;

import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.api.base.BaseController;
import com.lgcns.api.base.BaseMap;
import com.lgcns.api.helper.HttpHelper;
import com.lgcns.api.util.JsonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Api(value="User", tags= {"User"})
@SwaggerDefinition(tags = {@Tag(name = "User", description = "User REST API")})

//@RestController("/v2/user")
@RestController
public class UserControllerV2 extends BaseController{

	/** Leveraging method level **/ 
//	@ApiOperation(value = "User information with user id", response = BaseMap.class, tags = "user/userId")
//	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),@ApiResponse(code=404, message = "Not found user")})
	@RequestMapping(value="/v2/user/{userId}", method= {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseMap> userInfo(@PathVariable("userId") String userId) throws Exception {
		String url = "http://localhost:7070/userList2.do";

   
        //request entity is created with request body and headers
        BaseMap params = new BaseMap();
        params.put("username", userId);
                
        BaseMap result = HttpHelper.post(url, JsonUtil.toJson(params), ContentType.APPLICATION_JSON, BaseMap.class);
        if (result != null) {
        	return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);        	
        }
	}
}