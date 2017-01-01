package net.command;

import net.Server;
import net.connection.ConnectionManager;

public class CommandPlayerLoggedOut extends Command {

	public CommandPlayerLoggedOut(ConnectionManager connectionManager) {
		super(connectionManager);
	}
	
	public void read() {
		int id = this.connection.readInt();
		Server.kickPlayer(id);
	}
}
