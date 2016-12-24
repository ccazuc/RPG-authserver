package net.connection;

import static net.connection.PacketID.*;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import net.command.Command;
import net.command.CommandLogin;
import net.command.CommandLoginRealm;
import net.command.CommandLogout;
import net.command.CommandPlayerLoggedOnWorldServer;
import net.command.CommandRegisterServer;
import net.command.CommandSendRealmList;
import net.game.Player;
import net.game.WorldServer;

public class ConnectionManager {
	
	private Player player;
	private WorldServer server;
	private Connection connection;
	private HashMap<Integer, Command> commandList = new HashMap<Integer, Command>();
	private short lastPacketReaded;
	
	public ConnectionManager(Player player, SocketChannel socket) {
		this.player = player;
		this.connection = new Connection(socket, player);
		this.commandList.put((int)REGISTER_WORLD_SERVER, new CommandRegisterServer(this));
		this.commandList.put((int)LOGIN, new CommandLogin(this));
		this.commandList.put((int)LOGOUT, new CommandLogout(this));
		this.commandList.put((int)LOGIN_REALM, new CommandLoginRealm(this));
		this.commandList.put((int)SEND_REALM_LIST, new CommandSendRealmList(this));
	}
	
	public ConnectionManager(WorldServer server, SocketChannel socket) {
		this.server = server;
		this.connection = new Connection(socket, server);
		this.commandList.put((int)REGISTER_WORLD_SERVER, new CommandRegisterServer(this));
		this.commandList.put((int)LOGIN, new CommandLogin(this));
		this.commandList.put((int)LOGOUT, new CommandLogout(this));
		this.commandList.put((int)LOGIN_REALM, new CommandLoginRealm(this));
		this.commandList.put((int)SEND_REALM_LIST, new CommandSendRealmList(this));
		this.commandList.put((int)PLAYER_LOGGED_WORLD_SERVER, new CommandPlayerLoggedOnWorldServer(this));
	}
	
	public void read() {
		try {
			if(this.connection.read() == 1) {
				readPacket();
			}
		} 
		catch (IOException e ) {
			System.out.println("IOException on read");
			if(this.player != null) {
				this.player.close();
			}
			if(this.server != null) {
				this.server.close();
			}
		}
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
	
	public WorldServer getWorldServer() {
		return this.server;
	}
	
	public HashMap<Integer, Command> getCommandList() {
		return this.commandList;
	}
	
	private void readPacket() {
		while(this.connection != null && this.connection.hasRemaining()) {
			int packetLength = this.connection.readInt();
			if(this.connection.rBufferRemaining()+4 < packetLength) {
				this.connection.rBufferSetPosition(this.connection.rBufferPosition()-4);
				return;
			}
			short packetId = this.connection.readShort();
			if(this.commandList.containsKey((int)packetId)) {
				this.lastPacketReaded = packetId;
				this.commandList.get((int)packetId).read();
			}
			else {
				if(this.player != null) {
					System.out.println("Unknown packet: "+(int)packetId+", last packet readed: "+this.lastPacketReaded+" for player "+this.player.getAccountId());
				}
				else if(this.server != null) {
					System.out.println("Unknown packet: "+(int)packetId+", last packet readed: "+this.lastPacketReaded+" for server "+this.server.getRealmID());
				}
			}
		}
	}
}
