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
				for(int i = 0; i < pacSize; i++){
					pacData[i] = 0;
				}
				pac = new DatagramPacket(pacData, pacSize);
				try{
					recvSoc.setSoTimeout(30000);
					recvSoc.receive(pac);
					String msgin = new String(pac.getData(), 0, pac.getlength);
					this->processMessage(msgin);
				}catch(SocketTimeoutException e){
					System.out.println("Socket Timed Out: " + e);
					recvSoc.close();
				}
			}
		}catch(SocketException e){
			System.out.println("Socket Exception: " + e);
		}catch(IOException e){
			System.out.println("IOException: " + e);
		}
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data.toString());
		}catch(Exception e){
			System.out.println("Exception: " + e);
		}
		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages == -1){
			receivedMessages = new int[msg.totalMessages];
		}
		// TO-DO: Log receipt of the message
		totalMessages++;
		receivedMessages[msg.messageNum] = 1;
		System.out.println("Message " + msg.messageNum + " received");
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == totalMessages) {
			System.out.print("The following messages are missed: ");
	
			for(int i = 0; i < totalMessages; i++) {
				if(receivedMessages[i] == 0) {
					System.out.print(i + ", ");
				}
			}
			totalMessages = -1;
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try{
			recvSoc = new DatagramSocket(rp);
		}catch(SocketException e){
			System.out.println("Socket Exception: " + e);
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

		recvPort = Integer.parseInt(args[0]);

		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

}
