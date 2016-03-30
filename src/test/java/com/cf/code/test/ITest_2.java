package com.cf.code.test;

public interface ITest_2 {
	
	int test(int x,int y);
	
	default void d_fn(){
		System.out.println("test_2");
	}
	
}
