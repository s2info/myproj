package sdiag.getdata.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import sdiag.com.service.SdiagProperties;
import webdecorder.CipherBPMUtil;

public class DBConnectionAnal {
	private static DBConnectionAnal getAnalConnect;
	private static String proPath="";
	private DBConnectionAnal(){
		try{
			proPath = this.getClass().getResource("/sdiag.context/props/globals.properties").getPath();
			Class.forName(SdiagProperties.getProperty("Globals.DriverClassName1", proPath));
		}catch(ClassNotFoundException e){
			
		}
	}
	
	public static DBConnectionAnal getConnect(){
		if (getAnalConnect == null) 
			getAnalConnect = new DBConnectionAnal();
		return getAnalConnect;
	}
	
	public Connection getConnAnal() throws Exception{
		return DriverManager.getConnection(CipherBPMUtil.Decrypt(SdiagProperties.getProperty("Globals.Url2", proPath)), CipherBPMUtil.Decrypt(SdiagProperties.getProperty("Globals.UN2", proPath)), CipherBPMUtil.Decrypt(SdiagProperties.getProperty("Globals.PW2", proPath)));
	}
	
	public Statement createStatement(Connection conn) throws Exception{
		return conn.createStatement();
	}
}
