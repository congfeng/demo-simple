/**
 * 
 */
package com.cf.code.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cf.code.entity.Product;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface ProductDao {

	public Product find(@Param("id") Integer id);

	public void insert(Product product);
	
	public boolean delete(@Param("id") Integer id);
	
	public boolean update(@Param("id") Integer id,
			@Param("name") String name,
			@Param("sku") String sku,
			@Param("image") String image,
			@Param("richText") String richText);
	
	public void updateQrcode(@Param("id") Integer id,@Param("qrcode") String qrcode);
	
	
	public List<Product> query(@Param("productType") Integer productType,
			@Param("name") String name,@Param("sku") String sku,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd,
			@Param("start") Integer start,@Param("size") Integer size);

	public int queryCount(@Param("productType") Integer productType,
			@Param("name") String name,@Param("sku") String sku,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd);
	
}
