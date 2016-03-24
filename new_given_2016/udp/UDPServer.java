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
import common.*;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;
	private static UDPServer server;
	private static boolean initialised = false;
	private static int received_count = 0; //Counts the amount of received messages

	private void run() {
		int 	pacSize;
		byte[]	pacData;
		DatagramPacket 	pac;
		int 	counter = 0;

		try {
			//Initialise the variables
			pacSize = 1000;
			pacData = new byte[pacSize];
			pac = new DatagramPacket(pacData, pacSize);
			//Get the IP address and print it to the screen
			InetAddress addr = InetAddress.getLocalHost();
			String ipAddress = addr.getHostAddress();
			System.out.println("Local Host = " + addr);
			System.out.println("Host = " + ipAddress);
			//Set timeout to 30 seconds
			recvSoc.setSoTimeout(30000);

			//Receive messages
			while(true)
			{
				try{
					recvSoc.receive(pac);
					String data = new String(pac.getData(), 0, pac.getLength());
					server.processMessage(data);
				}
				catch (SocketTimeoutException e){
					System.out.println("Timeout of 30 seconds reached!");
					System.out.println("The following messages were not received: ");

					for(int i = 0; i < totalMessages; i++)
					{
						if(receivedMessages[i] == 0)
							System.out.println(i+1);
					}

					System.out.println("Total amount of messages received = " + received_count);
					System.out.println("Amount of unreceived messages = " + (totalMessages - received_count));

					initialised = false;
					received_count = 0;
					recvSoc.close();
				}
			}
		}
		catch (SocketException e1) {
			System.out.println("Socket closed " + e1);
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	public void processMessage(String data) {

		try{
			MessageInfo msg;
			int messageNum;
			msg = new MessageInfo(data);
			totalMessages = msg.totalMessages;
			messageNum = msg.messageNum;
			
			//If the receivedMessages array hasn't been initialised, initialise it
			if(initialised == false)
			{
				receivedMessages = new int[totalMessages];
				initialised = true;
				for(int counter = 0; counter < totalMessages; counter++)
					receivedMessages[counter] = 0;
			}

			receivedMessages[messageNum - 1] = 1; //1 if received
			
			if(receivedMessages[messageNum - 1] == 1)
				received_count++;

			System.out.println("Message " + messageNum + " was received");

			//After the last message is received print the outcomes to the screen
			if(messageNum == totalMessages)
			{
				System.out.println("The following messages were not received: ");
				for(int i = 0; i < totalMessages; i++)
				{
					if(receivedMessages[i] == 0)
						System.out.println(i+1);
				}

				System.out.println("Total amount of messages received = " + received_count);
				System.out.println("Amount of unreceived messages = " + (totalMessages - received_count));

				initialised = false;
				received_count = 0;
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UDPServer(int rp) {

		try{
			recvSoc = new DatagramSocket(rp);
		}
		catch (SocketException e) {
			System.out.println("Socket Exception " + e);
		}

		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {

		int	recvPort;

		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		server = new UDPServer(recvPort);
		server.run();
	}
}
