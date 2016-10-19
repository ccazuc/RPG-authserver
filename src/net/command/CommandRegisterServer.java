package net.command;

import net.Server;
import net.connection.ConnectionManager;
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
		System.out.println(this.player.getIpAdress()+" "+realmName+" "+realmID);
		if(this.player.getIpAdress().equals("/127.0.0.1")) {
			Server.addNewRealm(new WorldServer(this.player.getConnectionManager().getConnection().getSocket(), realmID));
			Server.getRealm(realmID).setPort(port);
			Server.getRealm(realmID).setRealmName(realmName);
			System.out.println("new realm registered");
		}
		else { //someone's trying to register is own server
			Server.kickPlayer(this.player.getAccountId());
		}
	}
}
