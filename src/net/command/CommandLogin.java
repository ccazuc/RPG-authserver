package net.command;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import jdo.JDOStatement;
import net.Server;
import net.connection.ConnectionManager;
import net.connection.PacketID;
import net.thread.sql.SQLRequest;
import net.utils.Hash;

public class CommandLogin extends Command {
	
	static JDOStatement selectBan;
	private static JDOStatement removeBan;
	private static SQLRequest loginRequest = new SQLRequest("SELECT name, password, salt, id, rank FROM account WHERE name = ?") {
		@Override
		public void gatherData() {
			try {
				this.statement.clear();
				this.statement.putString(this.userName);
				this.statement.execute();
				if(this.statement.fetch()) {
					String goodUsername = this.statement.getString().toLowerCase();
					String goodPassword = this.statement.getString();
					String salt = this.statement.getString();
					this.password = Hash.hash(this.password, salt);
					if(!(goodPassword.equals(this.password) && goodUsername.equals(this.userName.toLowerCase()))) {
						this.player.getConnectionManager().getConnection().startPacket();
						this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
						this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN_WRONG);
						this.player.getConnectionManager().getConnection().endPacket();
						this.player.getConnectionManager().getConnection().send();
						this.player.close();
						return;
					}
					int id = this.statement.getInt();
					int rank = this.statement.getInt();
					if(selectBan == null) {
						selectBan = Server.getJDO().prepare("SELECT unban_date FROM account_banned WHERE account_id = ?");
					}
					selectBan.clear();
					selectBan.putInt(id);
					selectBan.execute();
					if(selectBan.fetch()) {
						long unbanDate = selectBan.getLong();
						if(unbanDate > System.currentTimeMillis()) {
							this.player.getConnectionManager().getConnection().startPacket();
							this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
							this.player.getConnectionManager().getConnection().writeShort(PacketID.ACCOUNT_BANNED_TEMP);
							this.player.getConnectionManager().getConnection().endPacket();
							this.player.getConnectionManager().getConnection().send();
							this.player.close();
							return;
						}
						if(unbanDate == -1) {
							this.player.getConnectionManager().getConnection().startPacket();
							this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
							this.player.getConnectionManager().getConnection().writeShort(PacketID.ACCOUNT_BANNED_PERM);
							this.player.getConnectionManager().getConnection().endPacket();
							this.player.getConnectionManager().getConnection().send();
							this.player.close();
							return;
						}
						if(unbanDate <= System.currentTimeMillis()) {
							removeBan(id);
						}
					}
					if(Server.getPlayerList().containsKey(id)) {
						this.player.getConnectionManager().getConnection().startPacket();
						this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
						this.player.getConnectionManager().getConnection().writeShort(PacketID.ALREADY_LOGGED);
						this.player.getConnectionManager().getConnection().endPacket();
						this.player.getConnectionManager().getConnection().send();
						this.player.close();
						return;
					}
					this.player.getConnectionManager().getConnection().startPacket();
					this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
					this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN_ACCEPT);
					this.player.getConnectionManager().getConnection().writeInt(id);
					this.player.getConnectionManager().getConnection().writeString(goodUsername.toUpperCase());
					//this.player.getConnectionManager().getConnection().writeInt(rank);
					this.player.getConnectionManager().getConnection().endPacket();
					this.player.getConnectionManager().getConnection().send();
					this.player.setAccountId(id);
					this.player.setAccountRank(rank);
					this.player.setAccountName(this.userName);
					CommandSendRealmList.sendRealmList(this.player);
					Server.removeNonLoggedPlayer(this.player);
					Server.addLoggedPlayer(this.player);
					System.out.println("LOGIN:LOGIN_ACCEPT");
					return;
				}
				this.player.getConnectionManager().getConnection().startPacket();
				this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
				this.player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN_WRONG);
				this.player.getConnectionManager().getConnection().endPacket();
				this.player.getConnectionManager().getConnection().send();
				this.player.close();
				return;
			}
			catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	};
	
	public CommandLogin(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public void read() {
		loginRequest.setPlayer(this.player);
		loginRequest.setUserName(this.connection.readString().toLowerCase());
		loginRequest.setPassword(this.connection.readString());
		Server.addNewRequest(loginRequest);
	}
	
	static void removeBan(int accountId) throws SQLException {
		if(removeBan == null) {
			removeBan = Server.getJDO().prepare("DELETE FROM account_banned WHERE id = ?");
		}
		removeBan.clear();
		removeBan.putInt(accountId);
		removeBan.execute();
	}
}
