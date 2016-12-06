package net.command;

import net.connection.Connection;
import net.connection.ConnectionManager;
import net.game.Player;
import net.game.WorldServer;

public class Command {
	
	protected Connection connection;
	protected Player player;
	protected WorldServer server;
	
	public Command(final ConnectionManager connectionManager) {
		this.connection = connectionManager.getConnection();
		this.player = connectionManager.getPlayer();
		this.server = connectionManager.getWorldServer();
	}
	
	public void read() {}
	public void write() {}
}
