package sdiag.util;

import org.apache.commons.dbcp.BasicDataSource;

import webdecorder.CipherBPMUtil;

public class CryptDataSourceSdiag extends BasicDataSource{
	@Override
	public synchronized void setUrl(String url){
		try {
			super.setUrl(CipherBPMUtil.Decrypt(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void setUsername(String unameanal){
		try {
			super.setUsername(CipherBPMUtil.Decrypt(unameanal));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void setPassword(String password){
		try {
			super.setPassword(CipherBPMUtil.Decrypt(password));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
