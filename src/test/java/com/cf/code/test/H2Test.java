package com.cf.code.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.cf.code.common.DateUtil;

public class H2Test {

	public static void main(String[] args) throws Exception {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:E:/workspace/jisheng_workspace/_resources/h2/mydb", "root", "root");
		Statement stmt = conn.createStatement();
//		stmt.executeUpdate("DROP TABLE myuser");
//		StringBuilder create_sql = new StringBuilder();
//		create_sql.append("CREATE TABLE myuser ( ");
//		create_sql.append(" id int(11) PRIMARY KEY AUTO_INCREMENT,");
//		create_sql.append(" create_time TIMESTAMP NOT NULL,");
//		create_sql.append(" name varchar(60) NOT NULL,");
//		create_sql.append(" password varchar(60) DEFAULT NULL");
//		create_sql.append(");");
//		stmt.executeUpdate(create_sql.toString());
//		stmt.executeUpdate("CREATE INDEX myuser_index ON myuser(create_time,name);");
		stmt.executeUpdate("INSERT INTO myuser(create_time,name,password) VALUES(now(),'丛峰1','123');");
		stmt.executeUpdate("INSERT INTO myuser(create_time,name,password) VALUES(now(),'丛峰2',null);");
		ResultSet rs = stmt.executeQuery("SELECT id,name,password,create_time,FORMATDATETIME(create_time,'yyyy-MM-dd hh:mm:ss') AS show_time FROM myuser order by create_time desc");   
		while(rs.next()) {   
			System.out.println(
					rs.getInt("id")+","+
					rs.getString("password")+",---------"+
					rs.getString("show_time")+",---------"+
					DateUtil.format(rs.getTimestamp("create_time"), "yyyy-MM-dd HH:mm:ss")
					);
		}
		conn.close();
	}

}
