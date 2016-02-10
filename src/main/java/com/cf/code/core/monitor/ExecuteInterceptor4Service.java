/**
 * 
 */
package com.cf.code.core.monitor;

import org.aopalliance.intercept.MethodInvocation;

import com.cf.code.common.StringUtil;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class ExecuteInterceptor4Service extends ExecuteInterceptor{

	public ExecuteInterceptor4Service() {
		super(ExecutionLog.Type.service);
	}
	
	protected String getRequestMsg(MethodInvocation mi){
		StringBuilder requestMsg = new StringBuilder();
        if(mi.getArguments() != null&&mi.getArguments().length > 0){
        	requestMsg.append("[");
            for(Object o : mi.getArguments()){
                if(o == null){
                    requestMsg.append("").append(",");
                }else{
                    requestMsg.append(StringUtil.toString(o)).append(",");
                }
            }
            requestMsg.append("]");
        }
		return requestMsg.toString();
	}
	
}
