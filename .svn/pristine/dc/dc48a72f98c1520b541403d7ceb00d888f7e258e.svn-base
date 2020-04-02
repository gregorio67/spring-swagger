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
package com.lgcns.api.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Api(value="Product", tags= {"Product"})
@SwaggerDefinition(tags = {@Tag(name = "Product", description = "Product REST API")})

//@RestController("/v2/product")
@RestController
public class ProductController {

	private List<Product> products = Arrays.asList(
			new Product("1", "G Tech", "G Tech Hard drive", 230.45, 25),
			new Product("2", "WD SSD", "WD HDD", 150, 15),
			new Product("3", "Samsung SSD", "Samsung Solid State Drive", 100, 12),
			new Product("5", "Sandisk Pen Drive", "Sandisk Pen Drive", 12, 200));

	@ApiOperation(value = "Product information with product id", response = Product.class, tags = "getProduct/code")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),@ApiResponse(code =404, message = "404 error")})
	@RequestMapping(value="/v2/product/{code}", method= {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public Product getProduct(@PathVariable("code") String code) {
		return products.stream().filter(p -> p.getCode().equalsIgnoreCase(code)).collect(Collectors.toList()).get(0); 
	}

	@ApiOperation(value = "All Product information", response = Product.class, tags = "products")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),@ApiResponse(code =404, message = "404 error")})
	@RequestMapping(value="/v2/products", method= {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProducts() {
		return products;
	}
}
