package com.cf.code.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.ErrorCode;
import org.h2.tools.Server;

public class H2Test {

	public static void main(String[] args) {
		try {
			Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8088" ).start();
		} catch (SQLException e) {
			if(e.getErrorCode() == ErrorCode.EXCEPTION_OPENING_PORT_2){
				System.out.println("h2 server tcp 端口已占用，可能已经启动");
			}else{
				
			}
		}
		try {
			Server.createWebServer("-webAllowOthers","-webPort","8082").start();
		} catch (SQLException e) {
			
		}
//		testQuery();
		System.out.println("d");
//		server.stop();
	}

	private static void testQuery() throws Exception{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:8088/~/mydb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;MODE=MYSQL;AUTO_RECONNECT=TRUE;PAGE_SIZE=512;", "root", "root");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id,name FROM product order by create_time desc");   
		while(rs.next()) {   
			System.out.println(
					rs.getInt("id")+","+
					rs.getString("name")+",---------"
					);
		}
		conn.close();
	}
	
}
