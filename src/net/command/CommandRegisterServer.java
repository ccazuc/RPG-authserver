package net.command;

import net.Server;
import net.connection.ConnectionManager;
import net.game.Player;
import net.game.WorldServer;

public class CommandRegisterServer extends Command {
	
	public CommandRegisterServer(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public void read() {
		String realmName = this.connection.readString();
		int realmID = this.connection.readInt();
		int port = this.connection.readInt();
		System.out.println("[New realm registered] "+this.player.getIpAdress()+" "+realmName+" "+realmID);
		if(this.player.getIpAdress().equals("/127.0.0.1")) {
			WorldServer server = new WorldServer(this.player.getConnectionManager().getConnection().getSocket(), realmID);
			server.setPort(port);
			server.setRealmName(realmName);
			Server.addNewRealm(server);
			updatePlayerRealmList();
		}
		else { //someone's trying to register his own server
			Server.kickPlayer(this.player.getAccountId());
		}
	}
	
	public static void updatePlayerRealmList() {
		for(Player player : Server.getPlayerList().values()) {
			if(!player.isLoggedOnWorldServer()) {
				CommandSendRealmList.sendRealmList(player);
			}
		}
	}
}
