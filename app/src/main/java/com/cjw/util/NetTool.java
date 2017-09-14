package com.cjw.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetTool {
	
	
	public static  Socket connect(String dstName, int dstPort)
	{
		
		Socket socket=null;
		try {
			
			socket = new Socket(dstName, dstPort);
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
		
		
	}
	
	public static void rev(){
		
	}

}
