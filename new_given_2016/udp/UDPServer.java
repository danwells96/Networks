/*
 * Created on 07-Sep-2004
 * @author bandara
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.InetAddress;
import java.util.Arrays;

import common.MessageInfo;

/**
 * @author bandara
 *
 */
public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever

		
		pacSize = 1024;
		pacData = new byte[pacSize];

		try{
		    while(true){
			
			for(int n = 0; n < pacSize; n++)
			    pacData[n] = 0;
			pac = new DatagramPacket(pacData,pacSize);
			try{
			    recvSoc.setSoTimeout(30000);
			    recvSoc.receive(pac);
			    processMessage(new String(pac.getData()));
			} catch (SocketTimeoutException e){
			    // Print summary information
			    //System.out.println("Number of messages received = " + totalMessages);
			    //totalMessages = -1;
				printSummary();
			}
		    
		    }
		} catch(SocketException e){
		    System.out.println("Socket exception: " + e.getMessage());
		} catch(IOException e){
		    System.out.println("IO exception: " + e.getMessage());
		}

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object

		try{
		    msg = new MessageInfo(data.trim());
		} catch(Exception e){
		    e.printStackTrace();
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages == null){
		    totalMessages = 0;
		    // Create an array of 'totalMessages' zeros
		    receivedMessages = new int[msg.totalMessages];
		}

		// TO-DO: Log receipt of the message
		totalMessages++;
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (totalMessages == msg.totalMessages){
		    // Print summary information
		    //System.out.println("Number of messages received = " + totalMessages);
		    //totalMessages = -1;		
			printSummary();
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
	        try{
		    recvSoc = new DatagramSocket(rp);
		} catch (SocketException e){
		    System.out.println("Socket: " + e.getMessage());
		}
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		//InetAddress addr = InetAddress.getLocalHost(); 
		//String ipAddress = addr.getHostAddress(); 
		//System.out.println("Local Host = " + addr); 
		//System.out.println("Host = " + ipAddress);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

	public void printSummary(){

			if(receivedMessage == null || totalMessage <= 0)
				return;
			
			String missingMessages = "";
			for(int i = 0; i <receivedMessages.length; i++)
				if(receivedMessages[i] == 0)
					missingMessages += i + ", ";
				
			System.out.println("******* SUMMARY *******");
			Systen.out.println("Number of messages received: " + totalMessages);
			if(totalMessages == receivedMessages.length)
				System.out.println("No missing messages!");
			else
				System.out.println("Lost Messages: " + missingMessages);
			receivedMessages = null;
			totalMessages = -1;



	}
}
