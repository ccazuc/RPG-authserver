package net.game;

import java.nio.channels.SocketChannel;

import net.Server;
import net.connection.ConnectionManager;

public class Player {
	
	private ConnectionManager connectionManager;
	private int accountId;
	private double authKey;
	private WorldServer server;

	public Player(SocketChannel socket) {
		this.connectionManager = new ConnectionManager(this, socket);
	}
	
	public void close() {
		this.connectionManager.getConnection().close();
		Server.removeNonLoggedPlayer(this);
		Server.removeLoggedPlayer(this);
	}
	
	public WorldServer getServer() {
		return this.server;
	}
	
	public void setServer(WorldServer server) {
		this.server = server;
	}
	
	public void setAccountId(int id) {
		this.accountId = id;
	}
	
	public void setAuthKey(double key) {
		this.authKey = key;
	}
	
	public double getAuthKey() {
		return this.authKey;
	}
	
	public int getAccountId() {
		return this.accountId;
	}
	
	public ConnectionManager getConnectionManager() {
		return this.connectionManager;
	}
	
	public String getIpAdress() {
		return this.connectionManager.getIpAdress();
	}
}
