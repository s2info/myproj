package sdiag.getdata.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import sdiag.com.service.SdiagProperties;
import webdecorder.CipherBPMUtil;

public class DBConnection {
	private static DBConnection getconnect;
	private static String proPath="";
	private DBConnection(){
		try{
			proPath = this.getClass().getResource("/sdiag.context/props/globals.properties").getPath();
			Class.forName(SdiagProperties.getProperty("Globals.DriverClassName1", proPath));
		}catch(ClassNotFoundException e){
			
		}
	}
	
	public static DBConnection getConnect(){
		if (getconnect == null) 
			getconnect = new DBConnection();
		return getconnect;
	}
	
	public Connection getConn() throws Exception{
		return DriverManager.getConnection(CipherBPMUtil.Decrypt(SdiagProperties.getProperty("Globals.Url1", proPath)), CipherBPMUtil.Decrypt(SdiagProperties.getProperty("Globals.UN1", proPath)), CipherBPMUtil.Decrypt(SdiagProperties.getProperty("Globals.PW1", proPath)));
	}
	
	public Statement createStatement(Connection conn) throws Exception{
		return conn.createStatement();
	}
}
