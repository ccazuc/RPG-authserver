package net.command;

import net.Server;
import net.connection.ConnectionManager;
import net.connection.PacketID;

public class CommandLoginRealm extends Command {

	public CommandLoginRealm(ConnectionManager connectionManager) {
		super(connectionManager);
	}
	
	@Override
	public void read() {
		double key = Math.random();
		int id = this.connection.readInt();
		if(Server.getRealmList().containsKey(id)) {
			Server.getRealm(id).getConnectionManager().getConnection().writeShort(PacketID.LOGIN_REALM);
			Server.getRealm(id).getConnectionManager().getConnection().writeShort(PacketID.LOGIN_NEW_KEY);
			Server.getRealm(id).getConnectionManager().getConnection().writeDouble(key);
			Server.getRealm(id).getConnectionManager().getConnection().writeInt(this.player.getAccountId());
			Server.getRealm(id).getConnectionManager().getConnection().writeInt(this.player.getAccountRank());
			Server.getRealm(id).getConnectionManager().getConnection().writeString(this.player.getAccountName());
			Server.getRealm(id).getConnectionManager().getConnection().send();
			this.connection.writeShort(PacketID.LOGIN);
			this.connection.writeShort(PacketID.LOGIN_REALM_ACCEPTED);
			this.connection.writeDouble(key);
			this.connection.writeInt(Server.getRealm(id).getPort());
			this.connection.send();
			System.out.println("LOGINREALM:LOGIN_NEW_KEY");
		}
		else {
			//u haxor
		}
	}
}
