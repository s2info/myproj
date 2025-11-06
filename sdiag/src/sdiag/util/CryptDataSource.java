package sdiag.util;

import org.apache.commons.dbcp.BasicDataSource;

import webdecorder.CipherBPMUtil;

public class CryptDataSource extends BasicDataSource {
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
	public void setUsername(String username){
		try {
			super.setUsername(CipherBPMUtil.Decrypt(username));
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
