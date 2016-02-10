/**
 * 
 */
package com.cf.code.core.exception;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class BaseException extends Exception{

    private static final long serialVersionUID = 1L;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message,Throwable cause) {
        super(message, cause);
    }
    
}
