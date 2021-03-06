/*
 * Created on 07-Sep-2004
 * @author bandara
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.RMISecurityManager;
import java.rmi.registry.*;
import java.net.InetAddress;
import common.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

/**
 * @author bandara
 *
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private int receivedcount = 0;
	
	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {
		
		//initialise array of received messages and also counter for messages
		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages == -1){
			totalMessages = 0;
			totalMessages = msg.totalMessages;
			receivedcount = 0;
			receivedMessages = new int[totalMessages];
			for(int i = 0; i < totalMessages; i++){
				receivedMessages[i] = 0;
			}
		}
		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		receivedcount++;
		//marks a message as received by placing a 1 in the array for the corresponding messageNum
		System.out.println("Message " + msg.messageNum + " received");
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == (totalMessages-1)) {	//if last message
			if(receivedcount == totalMessages){	//if all messages expected are received
				System.out.println("No messages Missed");
			}else{
			//else counts the array entries = 0 i.e the message numbers that were lost
				System.out.println("The following messages are missed: ");
			
				for(int i = 0; i < totalMessages; i++) {
					if(receivedMessages[i] == 0) {
						System.out.print(i + ", ");
					}
				}
			}
			totalMessages = -1; //resets variable to allow for re-initialization of buffer, etc
		}
	}


	public static void main(String[] args) {

		RMIServer rmis = null;
		try{
		// TO-DO: Initialise Security Manager
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new RMISecurityManager());
		}
		// TO-DO: Instantiate the server class
		
			rmis = new RMIServer();
		}catch(RemoteException e){
			System.out.println("Remote Exception: " + e);
		}
		// TO-DO: Bind to RMI registry
		String urlName = new String("RMIServer");
		rebindServer(urlName, rmis);
	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		try{
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
			Registry r;
			try{

				r = LocateRegistry.createRegistry(1099);
			}catch(RemoteException e){
				r = LocateRegistry.getRegistry();
			}
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
			r.rebind(serverURL, server);
		}catch(RemoteException e){
			System.out.println("Remote Exception: " + e);
		}
	}
}
