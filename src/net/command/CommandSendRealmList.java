package net.command;

import net.Server;
import net.connection.ConnectionManager;
import net.connection.PacketID;
import net.game.Player;
import net.game.WorldServer;

public class CommandSendRealmList extends Command {
	
	public CommandSendRealmList(ConnectionManager connectionManager) {
		super(connectionManager);
	}
	
	@Override
	public void read() {
		sendRealmList(this.player);
	}

	public static void sendRealmList(Player player) {
		player.getConnectionManager().getConnection().writeShort(PacketID.SEND_REALM_LIST);
		player.getConnectionManager().getConnection().writeInt(Server.getRealmList().size());
		for(WorldServer server : Server.getRealmList().values()) {
			player.getConnectionManager().getConnection().writeInt(server.getRealmID());
			player.getConnectionManager().getConnection().writeString(server.getRealmName());
		}
		player.getConnectionManager().getConnection().send();
	}
}
