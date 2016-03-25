/*
 * Created on 07-Sep-2004
 * @author bandara
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;
	public static int recvPort;

	public static void main(String[] args) {

		InetAddress	serverAddr = null;
		recvPort = Integer.parseInt(args[1]);
		int 		countTo;
		String 		message;

		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			//Input server address in the console
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		//Input the receive port and amount of messages in the console
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);

		//Initialise the UDPClient
		UDPClient client = new UDPClient();
		//Run the send messages loop
		client.testLoop(serverAddr, recvPort, countTo);
	}

	public UDPClient() {

		try{
		sendSoc = new DatagramSocket();
		}
		catch(IOException e){
			System.out.println("IOException found");
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {

		for(int tries = 1; tries <= countTo; tries++)
		{
			String message = new String("" + countTo + ";" + tries);
			//Send messages
			send(message, serverAddr, recvPort);
			//Print the message to the screen
			System.out.println(message);
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {

		//Initialise the variables
		int				payloadSize = payload.length();
		byte[]			pktData = payload.getBytes();
		DatagramPacket	pkt;

		pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);

		try{
			//Send the message through the socket
			sendSoc.send(pkt);
		}
		catch(IOException e){
			System.out.println("Socket: " + e.getMessage());
		}
	}
}
