package net.command;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import jdo.JDOStatement;
import net.Server;
import net.config.ConfigMgr;
import net.connection.ConnectionManager;
import net.connection.PacketID;
import net.game.Player;
import net.thread.sql.SQLDatas;
import net.thread.sql.SQLRequest;
import net.utils.Hash;

public class CommandLogin extends Command {
	
	static JDOStatement selectBan;
	private static JDOStatement removeBan;
	private static SQLRequest loginRequest = new SQLRequest("SELECT name, password, salt, id, rank FROM account WHERE name = ?", "Player account login") {
		@Override
		public void gatherData() {
			try {
				Player player = this.datasList.get(0).getPlayer();
				String userName = this.datasList.get(0).getStringValue1();
				String password = this.datasList.get(0).getStringValue2();
				this.statement.clear();
				this.statement.putString(userName);
				this.statement.execute();
				if(this.statement.fetch()) {
					String goodUsername = this.statement.getString().toLowerCase();
					String goodPassword = this.statement.getString();
					String salt = this.statement.getString();
					password = Hash.hash(password, salt);
					if(!(goodPassword.equals(password) && goodUsername.equals(userName.toLowerCase()))) {
						player.getConnectionManager().getConnection().startPacket();
						player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
						player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN_WRONG);
						player.getConnectionManager().getConnection().endPacket();
						player.getConnectionManager().getConnection().send();
						player.close();
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
							player.getConnectionManager().getConnection().startPacket();
							player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
							player.getConnectionManager().getConnection().writeShort(PacketID.ACCOUNT_BANNED_TEMP);
							player.getConnectionManager().getConnection().endPacket();
							player.getConnectionManager().getConnection().send();
							player.close();
							return;
						}
						if(unbanDate == -1) {
							player.getConnectionManager().getConnection().startPacket();
							player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
							player.getConnectionManager().getConnection().writeShort(PacketID.ACCOUNT_BANNED_PERM);
							player.getConnectionManager().getConnection().endPacket();
							player.getConnectionManager().getConnection().send();
							player.close();
							return;
						}
						if(unbanDate <= System.currentTimeMillis()) {
							removeBan(id);
						}
					}
					if(!ConfigMgr.ALLOW_MULTIPLE_LOG && Server.getPlayerList().containsKey(id)) {
						player.getConnectionManager().getConnection().startPacket();
						player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
						player.getConnectionManager().getConnection().writeShort(PacketID.ALREADY_LOGGED);
						player.getConnectionManager().getConnection().endPacket();
						player.getConnectionManager().getConnection().send();
						player.close();
						return;
					}
					player.getConnectionManager().getConnection().startPacket();
					player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
					player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN_ACCEPT);
					player.getConnectionManager().getConnection().writeInt(id);
					player.getConnectionManager().getConnection().writeString(goodUsername.toUpperCase());
					//player.getConnectionManager().getConnection().writeInt(rank);
					player.getConnectionManager().getConnection().endPacket();
					player.getConnectionManager().getConnection().send();
					player.setAccountId(id);
					player.setAccountRank(rank);
					player.setAccountName(userName);
					CommandSendRealmList.sendRealmList(player);
					Server.removeNonLoggedPlayer(player);
					Server.addLoggedPlayer(player);
					System.out.println("LOGIN:LOGIN_ACCEPT");
					System.out.println("Number online after accept " + Server.getPlayerList().size());
					return;
				}
				player.getConnectionManager().getConnection().startPacket();
				player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN);
				player.getConnectionManager().getConnection().writeShort(PacketID.LOGIN_WRONG);
				player.getConnectionManager().getConnection().endPacket();
				player.getConnectionManager().getConnection().send();
				player.close();
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
		loginRequest.addDatas(new SQLDatas(this.player, this.connection.readString(), this.connection.readString()));
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
