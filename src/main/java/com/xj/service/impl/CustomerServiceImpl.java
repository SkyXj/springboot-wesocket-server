/**   
 * Copyright © 广州禾信仪器股份有限公司. All rights reserved.
 * 
 * @Title: CustomerServiceImpl.java 
 * @Package: com.xj.service.impl
 * @Description: TODO
 * @author: sky-1012262558@qq.com
 * @date: 2019-11-04 15:53:19
 * @Modify Description : 
 * @Modify Person : 
 * @version: V1.0   
 */


package com.xj.service.impl;


import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.xj.entity.Customer;
import com.xj.mapper.CustomerMapper;
import com.xj.service.CustomerService;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
* 描述：商户表 服务实现层
* @author sky
* @date 2019-11-04 15:53:19
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService{

}