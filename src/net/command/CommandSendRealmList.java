package net.command;

import net.Server;
import net.connection.ConnectionManager;
import net.game.Player;

public class CommandSendRealmList extends Command {
	
	public CommandSendRealmList(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	public static void sendRealmList(Player player) {
		player.getConnectionManager().getConnection().writeInt(Server.getRealmList().size());
	}
}
