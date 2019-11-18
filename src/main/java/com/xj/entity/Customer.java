/**   
 * Copyright © 广州禾信仪器股份有限公司. All rights reserved.
 * 
 * @Title: Customer.java 
 * @Package: com.xj.entity
 * @Description: TODO
 * @author: sky-1012262558@qq.com
 * @date: 2019-11-04 15:53:19
 * @Modify Description : 
 * @Modify Person : 
 * @version: V1.0   
 */

package com.xj.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.persistence.*;
import java.math.BigDecimal;
import lombok.Data;

/**
* 描述：商户表模型
* @author sky
* @date 2019-11-04 15:53:19
*/
@Data
@TableName("customer")
public class Customer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
    *自增主键
    */
    @TableField("id")
    @Column(name = "id",length = 10,nullable = false)
    private Integer id;

    
    /**
    *名称
    */
    @TableField("name")
    @Column(name = "name",length = 50,nullable = false)
    private String old_name;

    
    /**
    *经度
    */
    @TableField("lon")
    @Column(name = "lon",length = 9,nullable = false)
    private double lon;

    
    /**
    *纬度
    */
    @TableField("lat")
    @Column(name = "lat",length = 8,nullable = false)
    private double lat;

    
    
}