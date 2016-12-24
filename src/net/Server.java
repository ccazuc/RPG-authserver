package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdo.JDO;
import jdo.wrapper.MariaDB;
import net.game.Player;
import net.game.WorldServer;
import net.thread.socket.SocketRunnable;
import net.thread.sql.SQLRunnable;
import net.thread.sql.SQLRequest;

public class Server {
	private static JDO jdo;
	private static ServerSocketChannel serverSocketChannel;
	//private static SocketChannel clientSocket;
	private static Map<Integer, Player> playerList = Collections.synchronizedMap(new HashMap<Integer, Player>());
	private static List<Player> nonLoggedPlayer = Collections.synchronizedList(new ArrayList<Player>());
	private static HashMap<Integer, WorldServer> realmList = new HashMap<Integer, WorldServer>();
	private static ArrayList<Integer> playerWaitingForKick = new ArrayList<Integer>();
	private static Thread sqlRequest;
	private static SQLRunnable SQLRunnable;
	private static Thread socketThread;
	private static SocketRunnable socketRunnable;

	private static long LOOP_TICK_TIMER;
	private final static int TIMEOUT_TIMER = 10000;
	private final static int PORT = 5725;
	private final static int LOOP_TIMER = 25;
	
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, InterruptedException {
		float delta;
		System.out.println("AUTH SERVER");
		LOOP_TICK_TIMER = System.currentTimeMillis();
		jdo = new MariaDB("127.0.0.1", 3306, "rpg", "root", "mideas");
		nonLoggedPlayer = Collections.synchronizedList(nonLoggedPlayer);
		final InetSocketAddress iNetSocketAdress = new InetSocketAddress(PORT);
		serverSocketChannel = ServerSocketChannel.open();
		//serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(iNetSocketAdress, 100);
		SQLRunnable = new SQLRunnable();
		sqlRequest = new Thread(SQLRunnable);
		sqlRequest.start();
		socketRunnable = new SocketRunnable(serverSocketChannel);
		socketThread = new Thread(socketRunnable);
		socketThread.start();
		System.out.println("Init took "+(System.currentTimeMillis()-LOOP_TICK_TIMER)+" ms.");
		while(true) {
			LOOP_TICK_TIMER = System.currentTimeMillis();
			kickPlayers();
			readRealm();
			readPlayer();
			delta = System.currentTimeMillis()-LOOP_TICK_TIMER;
			if(delta < LOOP_TIMER) {
				Thread.sleep(LOOP_TIMER-(long)delta);
			}
			if(delta > 2) {
				System.out.println("Loop too long: "+delta);
			}
		}
	}
	
	public static void addNewRequest(SQLRequest request) {
		SQLRunnable.addRequest(request);
	}
	
	private static void kickPlayers() {
		while(playerWaitingForKick.size() > 0) {
			playerList.remove(playerWaitingForKick.get(0));
			playerWaitingForKick.remove(0);
		}
	}
	
	private static void readRealm() {
		for(WorldServer server : realmList.values()) {
			server.getConnectionManager().read();
		}
	}
	
	private static void readPlayer() {
		int i = 0;
		synchronized(nonLoggedPlayer) {
			while(i < nonLoggedPlayer.size()) {
				if(LOOP_TICK_TIMER-nonLoggedPlayer.get(i).getLoggedTimer() > TIMEOUT_TIMER) {
					nonLoggedPlayer.remove(i);
					continue;
				}
				nonLoggedPlayer.get(i).getConnectionManager().read();
				i++;
			}
		}
		synchronized(playerList) {
			for(Player player : playerList.values()) {
				player.getConnectionManager().read();
			}
		}
	}
	
	public static void addNewRealm(WorldServer server) {
		realmList.put(server.getRealmID(), server);
	}
	
	public static WorldServer getRealm(int id) {
		return realmList.get(id);
	}
	
	public static Map<Integer, Player> getPlayerList() {
		synchronized(playerList) {
			return playerList;
		}
	}
	
	public static JDO getJDO() {
		return jdo;
	}
	
	public static Player getNonLoggedPlayer(int id) {
		int i = 0;
		synchronized(nonLoggedPlayer) {
			while(i < nonLoggedPlayer.size()) {
				if(nonLoggedPlayer.get(i).getAccountId() == id) {
					return nonLoggedPlayer.get(i);
				}
				i++;
			}
		}
		return null;
	}
	
	public static void removeServer(int id) {
		realmList.remove(id);
	}
	
	public static void removeNonLoggedPlayer(Player player) {
		if(player != null) {
			synchronized(nonLoggedPlayer) {
				nonLoggedPlayer.remove(player);
			}
		}
	}
	
	public static void addNonLoggedPlayer(Player player) {
		synchronized(nonLoggedPlayer) {
			nonLoggedPlayer.add(player);
		}
	}
	
	public static void addLoggedPlayer(Player player) {
		if(player != null) {
			synchronized(playerList) {
				playerList.put(player.getAccountId(), player);
			}
		}
	}
	
	public static void removeLoggedPlayer(Player player) {
		if(player != null) {
			playerWaitingForKick.add(player.getAccountId());
		}
	}
	
	public static void kickPlayer(int id) {
		playerWaitingForKick.add(id);
	}
	
	public static HashMap<Integer, WorldServer> getRealmList() {
		return realmList;
	}
}
