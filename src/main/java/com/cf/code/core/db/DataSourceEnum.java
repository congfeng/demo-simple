package com.cf.code.core.db;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 * @see 未来可能根据区域、手机型号等多维度做分类、分库、分表处理
 *
 */
public enum DataSourceEnum {

	CategoryOne("CategoryOne","默认分类"),
	CategoryTwo("CategoryTwo","第二分类")
    ;
    
    private DataSourceEnum(String code, String name){
        this.code = code;
        this.name = name;
    };
    
    private String code;
    private String name;
    
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}

}
