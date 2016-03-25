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
		int		pacSize;
		byte[]		pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever

		//initializes packet parameters
		pacSize = 1024;
		pacData = new byte[pacSize];

		try{
		    while(true){
			//initialises packet data to 0
			for(int n = 0; n < pacSize; n++){
			    pacData[n] = 0;
			}
			pac = new DatagramPacket(pacData,pacSize);
			//creates a new datagram packet with received data
			try{
			    recvSoc.setSoTimeout(30000);	//timeout = 30s
			    recvSoc.receive(pac);		//receives new packet
			    processMessage(new String(pac.getData()));	//extracts data from packet
			} catch (SocketTimeoutException e){
			    System.out.println("Socket Timedout");
			    //prints the status of any missing packets and which packets arrived
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
		//constructs a new packet in order to deconstruct it into data components
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
		//counts any incoming messages
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (totalMessages == msg.totalMessages){
			printSummary();
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		//initializes input data socket
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

		// TO-DO: Construct Server object and start it by calling run().
		//creates new instance of a UDPServer
		UDPServer server = new UDPServer(recvPort);
		//calls the operation on the server to receive data
		server.run();
	}

	public void printSummary(){

			if(receivedMessages == null || totalMessages <= 0){
				return;
			}
			
			String missingMessages = "";
			for(int i = 0; i <receivedMessages.length; i++){
				if(receivedMessages[i] == 0){
					missingMessages += i + ", ";
				}
			}	
		
			System.out.println("Number of messages received: " + totalMessages);
			if(totalMessages == receivedMessages.length){
				System.out.println("No missing messages");
			}else{
				System.out.println("Lost Messages: " + missingMessages);
			}
			receivedMessages = null;
			totalMessages = -1;



	}
}
