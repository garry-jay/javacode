package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行的公共功能字段的填充
 */

@Target(ElementType.METHOD)//指定注解只能加到方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操作类型 UPDATE INSERT
    OperationType value();
}
