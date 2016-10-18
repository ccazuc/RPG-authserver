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
			Server.getRealm(id).getConnectionManager().getConnection().writeByte(PacketID.LOGIN_REALM);
			Server.getRealm(id).getConnectionManager().getConnection().writeByte(PacketID.LOGIN_NEW_KEY);
			Server.getRealm(id).getConnectionManager().getConnection().writeDouble(key);
			Server.getRealm(id).getConnectionManager().getConnection().writeInt(this.player.getAccountId());
			Server.getRealm(id).getConnectionManager().getConnection().send();
			this.connection.writeByte(PacketID.LOGIN);
			this.connection.writeByte(PacketID.LOGIN_REALM_ACCEPTED);
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
