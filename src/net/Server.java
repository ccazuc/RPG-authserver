package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
import net.sql.MyRunnable;
import net.sql.SQLRequest;

public class Server {
	private final static int PORT = 5725;
	private static JDO jdo;
	private static ServerSocketChannel serverSocketChannel;
	private static SocketChannel clientSocket;
	private static Map<Integer, Player> playerList = Collections.synchronizedMap(new HashMap<Integer, Player>());
	private static List<Player> nonLoggedPlayer = Collections.synchronizedList(new ArrayList<Player>());
	private static HashMap<Integer, WorldServer> realmList = new HashMap<Integer, WorldServer>();
	private static ArrayList<Integer> playerWaitingForKick = new ArrayList<Integer>();
	private static Thread sqlRequest;
	private static MyRunnable runnable;
	
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		long time = System.currentTimeMillis();
		System.out.println("AUTH SERVER");
		jdo = new MariaDB("127.0.0.1", 3306, "rpg", "root", "mideas");
		nonLoggedPlayer = Collections.synchronizedList(nonLoggedPlayer);
		final InetSocketAddress iNetSocketAdress = new InetSocketAddress(PORT);
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(iNetSocketAdress);
		runnable = new MyRunnable();
		sqlRequest = new Thread(runnable);
		sqlRequest.start();
		System.out.println("Init took "+(System.currentTimeMillis()-time)+" ms.");
		while(true) {
			if((clientSocket = serverSocketChannel.accept()) != null) {
				clientSocket.configureBlocking(false);
				nonLoggedPlayer.add(new Player(clientSocket));
			}
			time = System.currentTimeMillis();
			readRealm();
			readPlayer();
			kickPlayers();
			if((System.currentTimeMillis()-time)/1000d >= 0.05) {
				System.out.println("Loop too long: "+(System.currentTimeMillis()-time)/1000d);
			}
		}
	}
	
	public static void addNewRequest(SQLRequest request) {
		runnable.addRequest(request);
	}
	
	private static void kickPlayers() {
		int i = 0;
		while(i < playerWaitingForKick.size()) {
			playerList.remove(playerWaitingForKick.get(i));
			i++;
		}
		playerWaitingForKick.clear();
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
	
	public static void removeNonLoggedPlayer(Player player) {
		if(player != null) {
			synchronized(nonLoggedPlayer) {
				nonLoggedPlayer.remove(player);
			}
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
