package net.command;

import net.Server;
import net.connection.ConnectionManager;
import net.game.Player;

public class CommandPlayerLoggedOnWorldServer extends Command {
	
	public CommandPlayerLoggedOnWorldServer(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public void read() {
		int id = this.connection.readInt();
		boolean isLoggedOnWorldServer = this.connection.readBoolean();
		Player player = Server.getPlayerList().get(id);
		if(player == null) {
			return;
		}
		player.setIsLoggedOnWorldServe(isLoggedOnWorldServer);
		if(isLoggedOnWorldServer) {
			player.setServer(this.server);
		}
		else {
			player.setServer(null);
		}
	}
}
