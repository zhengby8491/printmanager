package com.huayin.printmanager.domain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;

/**
 * 系统日志拦截
 * @author think
 *
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented 
public @interface SystemControllerLog {
	SystemLogType SystemLogType() default SystemLogType.DEFAULT;
	String BillTypeText() default "系统";
	Operation Operation() default Operation.DEFAULT;
	OperationResult OperationResult() default OperationResult.SUCCESS;
	String Description()  default "";    
}
