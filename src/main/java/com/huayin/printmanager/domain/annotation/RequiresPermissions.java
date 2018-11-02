package com.huayin.printmanager.domain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 必须权限
 *   Logical.AND : 包含所有权限字符
 *   Logical.OR  : 包含一个全新字符
 * @author think
 *
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions
{
	 String[] value();
	 
	 Logical logical() default Logical.AND;
}

