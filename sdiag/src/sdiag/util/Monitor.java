package sdiag.util;

import java.net.DatagramSocket;

public class Monitor {
	private static DatagramSocket isRun;
	public static boolean monitoring(int portnum){
		try{
			isRun = new DatagramSocket(portnum);
			return true;
		}catch(Exception e){
			
			return false;
		}
	}
	
	public static void close(){
		if(isRun != null){
			isRun.close();
		}
	}
}