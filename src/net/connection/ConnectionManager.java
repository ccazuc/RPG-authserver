package net.connection;

import static net.connection.PacketID.*;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import net.command.Command;
import net.command.CommandLogin;
import net.game.Player;

public class ConnectionManager {
	
	private Player player;
	private Connection connection;
	private HashMap<Integer, Command> commandList = new HashMap<Integer, Command>();
	private static Connection worldServerConnection;
	private static SocketChannel worldServerSocket;
	private byte lastPacketReaded;
	
	public ConnectionManager(Player player, SocketChannel socket) {
		this.player = player;
		this.connection = new Connection(socket, player);
		this.commandList.put((int)LOGIN, new CommandLogin(this));
	}
	
	public void read() {
		try {
			if(this.connection.read() == 1) {
				readPacket();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			this.player.close();
		}
	}
	
	public static void setWorldServerSocket(SocketChannel socket) {
		worldServerSocket = socket;
		worldServerConnection = new Connection(worldServerSocket);
	}
	
	public static Connection worldServerConnection() {
		return worldServerConnection;
	}
	
	public String getIpAdress() {
		return this.connection.getIpAdress();
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public HashMap<Integer, Command> getCommandList() {
		return this.commandList;
	}
	
	private void readPacket() {
		while(this.connection != null && this.connection.hasRemaining()) {
			byte packetId = this.connection.readByte();
			if(this.commandList.containsKey((int)packetId)) {
				this.lastPacketReaded = packetId;
				this.commandList.get((int)packetId).read();
			}
			else {
				System.out.println("Unknown packet: "+(int)packetId+", last packet readed: "+this.lastPacketReaded+" for player "+this.player.getAccountId());
			}
		}
	}
}
