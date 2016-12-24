package net.game;

import java.nio.channels.SocketChannel;

import net.Server;
import net.connection.ConnectionManager;

public class Player {
	
	private ConnectionManager connectionManager;
	private int accountId;
	private int accountRank;
	private double authKey;
	private String accountName;
	private WorldServer server;
	private boolean isLoggedOnWorldServer;
	private final long loggedTimer;

	public Player(SocketChannel socket) {
		this.connectionManager = new ConnectionManager(this, socket);
		this.loggedTimer = System.currentTimeMillis();
	}
	
	public void close() {
		this.connectionManager.getConnection().close();
		Server.removeNonLoggedPlayer(this);
		Server.removeLoggedPlayer(this);
		System.out.println("Removed logged player");
	}
	
	public long getLoggedTimer() {
		return this.loggedTimer;
	}
	
	public WorldServer getServer() {
		return this.server;
	}
	
	public void setAccountName(String name) {
		this.accountName = name;
	}
	
	public String getAccountName() {
		return this.accountName;
	}
	
	public void setIsLoggedOnWorldServe(boolean we) {
		this.isLoggedOnWorldServer = we;
	}
	
	public boolean isLoggedOnWorldServer() {
		return this.isLoggedOnWorldServer;
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
	
	public void setAccountRank(int rank) {
		this.accountRank = rank;
	}
	
	public int getAccountRank() {
		return this.accountRank;
	}
	
	public ConnectionManager getConnectionManager() {
		return this.connectionManager;
	}
	
	public String getIpAdress() {
		return this.connectionManager.getIpAdress();
	}
}
