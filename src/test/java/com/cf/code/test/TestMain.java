/**
 * 
 */
package com.cf.code.test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.cf.code.common.DateUtil;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class TestMain {

	static int i = 100;
	
	public static void main(String[] args) {
//		Predicate<String> xx = (d) -> d.length() > 5;
//		Function<String,String> d1 = (p) -> p+"-1";
//		Function<String,String> d2 = (p) -> p+"-2";
//		Function<String,String> dx = d1.compose(d2);
//		System.out.println(dx.apply("congfeng"));
//		Supplier<Integer> s = Test::testd;
//		System.out.println(s.get());
//		Consumer<String> c = (p) -> System.out.println(p+"x");
//		c.accept("y");
//		Optional<String> op = Optional.of("abcd");
//		op.isPresent();
//		op.get();
//		op.orElse("other");
//		op.ifPresent(System.out::print);
//		Map<Integer, String> map = new HashMap<>();
//		map.putIfAbsent(5, "b");
//		map.putIfAbsent(5,(x,y)->x+","+y);
//		map.computeIfPresent(5,(x,y)->null);
//		System.out.println(map.get(5));
//		System.out.println(DateUtil.toParse("2016-03-22 10:57:00").getTime()/1000);
		String d = "%e6%9c%aa%e5%88%86%e9%a1%9e";
		System.out.println(URLDecoder.decode(d));
	}
	
	
}
