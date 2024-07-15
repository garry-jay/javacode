package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工登录返回的数据格式")//描述这个实体类是干嘛的
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("主键值")//描述我们的属性有什么用
    private Long id;

    @ApiModelProperty("用户名")//描述我们的属性有什么用
    private String userName;

    @ApiModelProperty("姓名")//描述我们的属性有什么用
    private String name;

    @ApiModelProperty("jwt令牌")//描述我们的属性有什么用
    private String token;

}
