package com.cf.code.test;

@FunctionalInterface
public interface ITest_1 {
	
	int xxxx(int x,int y);
	
//	int xxxxxxx(int x,int y);
	
	
	default void d_fn(){
		System.out.println("test_1");
	}
	
	default void d_fdn(){
		System.out.println("test_1");
	}
}
