/**
* Copyright © 广州禾信仪器股份有限公司. All rights reserved.
*
* @Title: CustomerController.java
* @Package: com.xj.web.api
* @Description: TODO
* @author: sky-1012262558@qq.com
* @date: 2019-11-04 15:53:19
* @Modify Description :
* @Modify Person :
* @version: V1.0
*/

package com.xj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.xj.entity.Customer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.xj.service.CustomerService;

@Api(value="商户表",tags={"商户表"})
@RestController
@RequestMapping("/customer")
public class CustomerController{
	
	@Autowired
	CustomerService customerService; 
	
}