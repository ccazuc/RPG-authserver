package net.game;

import java.nio.channels.SocketChannel;

import net.Server;
import net.connection.ConnectionManager;

public class WorldServer {

	private ConnectionManager connectionManager;
	private String realmName = "";
	private int realmId;
	private int port;

	public WorldServer(SocketChannel socket, int realmId) {
		this.connectionManager = new ConnectionManager(this, socket);
		this.realmId = realmId;
	}
	
	public ConnectionManager getConnectionManager() {
		return this.connectionManager;
	}
	
	public void close() {
		this.connectionManager.getConnection().close();
		Server.removeServer(this.realmId);;
	}
	
	public int getRealmID() {
		return this.realmId;
	}
	
	public void setRealmId(int id) {
		this.realmId = id;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getRealmName() {
		return this.realmName;
	}
	
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}
}
