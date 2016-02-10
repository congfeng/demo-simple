/**
 * 
 */
package com.cf.code.core.monitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.ui.Model;

import com.cf.code.common.Constant;
import com.cf.code.common.StringUtil;
import com.cf.code.common.WebUtil;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class ExecuteInterceptor4Web extends ExecuteInterceptor{

	public ExecuteInterceptor4Web() {
		super(ExecutionLog.Type.web);
	}

	@Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Model model = null;
        if(mi.getArguments() != null&&mi.getArguments().length > 0){
            for(Object o : mi.getArguments()){
            	if(o instanceof Model){
                	model = (Model)o;
                	break;
                }
            }
        }
        Object resp = super.invoke(mi);
        if(mi.getMethod().getReturnType() == Model.class&&model != null){
        	if(!model.containsAttribute(Constant.TransmitField.Status)){
        		model.addAttribute(Constant.TransmitField.Status, 1);
        	}
    	}
        return resp;
    }
	
	protected String getRequestMsg(MethodInvocation mi){
		StringBuilder requestMsg = new StringBuilder();
		if(mi.getArguments() != null&&mi.getArguments().length > 0){
        	requestMsg.append("[");
            for(Object o : mi.getArguments()){
                if(o == null){
                    requestMsg.append("").append(",");
                }else if(o instanceof HttpServletRequest){
                	HttpServletRequest httpRequest = (HttpServletRequest)o;
                    requestMsg.append("HttpServletRequest:[");
                    requestMsg.append("from:"+WebUtil.getRealClientIp(httpRequest)).append(",");
                    requestMsg.append("Host:"+httpRequest.getRemoteHost()).append(",");
                    requestMsg.append("ContentType:"+httpRequest.getContentType()).append(",");
                    requestMsg.append("Encoding:"+httpRequest.getCharacterEncoding()).append(",");
                    requestMsg.append("Param:"+WebUtil.getCurrentHttpRequestText()).append("],");
                }else if(o instanceof HttpServletResponse){
                    requestMsg.append("HttpServletResponse").append(",");
                }else{
                    requestMsg.append(StringUtil.toString(o)).append(",");
                }
            }
            requestMsg.append("]");
        }
    	return requestMsg.toString();
    }
}
