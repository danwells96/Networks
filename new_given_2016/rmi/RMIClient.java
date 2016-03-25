/*
 * Created on 07-Sep-2004
 * @author bandara
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

import common.MessageInfo;

/**
 * @author bandara
 *
 */
public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		try{
			if(System.getSecurityManager() == null){
				System.setSecurityManager(new SecurityManager());
			}
		// TO-DO: Bind to RMIServer
			try{
				
				iRMIServer = (RMIServerI) Naming.lookup(urlServer);
			}catch(NotBoundException e){
				System.out.println("Not Bound Exception: " + e);
			}
			catch(MalformedURLException e2){
				System.out.println("MalformedURLException: " + e2);
			}
		// TO-DO: Attempt to send messages the specified number of times
			for(int i = 0; i < numMessages; i++){
				MessageInfo msg = new MessageInfo(numMessages, i);
				iRMIServer.receiveMessage(msg);
			}
		}catch(RemoteException e){
			System.out.println("Remote Exception: " + e);
		}
	}
}
