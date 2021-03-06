package net.thread.sql;

import net.game.Player;

public class SQLDatas {

	private int iValue1;
	private int iValue2;
	private int iValue3;
	private int iValue4;
	private int iValue5;
	private int iValue6;
	private int iValue7;
	private long lValue1;
	private long lValue2;
	private byte bValue1;
	private byte bValue2;
	private String stringValue1;
	private String stringValue2;
	private String stringValue3;
	private Player player;
	
	public SQLDatas(int iValue1) {
		this.iValue1 = iValue1;
	}
	
	public SQLDatas(long lValue1) {
		this.lValue1 = lValue1;
	}
	
	public SQLDatas(Player player) {
		this.player = player;
	}
	
	public SQLDatas(int iValue1, int iValue2) {
		this.iValue1 = iValue1;
		this.iValue2 = iValue2;
	}
	
	public SQLDatas(int iValue1, long lValue1) {
		this.iValue1 = iValue1;
		this.lValue1 = lValue1;
	}
	
	public SQLDatas(int iValue1, byte bValue1) {
		this.iValue1 = iValue1;
		this.bValue1 = bValue1;
	}
	
	public SQLDatas(Player player, int iValue1) {
		this.player = player;
		this.iValue1 = iValue1;
	}
	
	public SQLDatas(Player player, String stringValue1, String stringValue2)
	{
		this.player = player;
		this.stringValue1 = stringValue1;
		this.stringValue2 = stringValue2;
	}
	
	public SQLDatas(int iValue1, String stringValue1) {
		this.iValue1 = iValue1;
		this.stringValue1 = stringValue1;
	}
	
	public SQLDatas(int iValue1, int iValue2, int iValue3) {
		this.iValue1 = iValue1;
		this.iValue2 = iValue2;
		this.iValue3 = iValue3;
	}
	
	public SQLDatas(int iValue1, int iValue2, long lValue1) {
		this.iValue1 = iValue1;
		this.iValue2 = iValue2;
		this.lValue1 = lValue1;
	}
	
	public SQLDatas(int iValue1, int iValue2, int iValue3, String text) {
		this.iValue1 = iValue1;
		this.iValue2 = iValue2;
		this.iValue3 = iValue3;
		this.stringValue1 = text;
	}
	
	public SQLDatas(int iValue1, byte bValue1, int iValue2, int iValue3, byte bValue2) {
		this.iValue1 = iValue1;
		this.iValue2 = iValue2;
		this.bValue1 = bValue1;
		this.iValue2 = iValue2;
		this.iValue3 = iValue3;
		this.bValue2 = bValue2;
	}
	
	public SQLDatas(long lValue1, long lValue2, String stringValue1, String stringValue2) {
		this.lValue1 = lValue1;
		this.lValue2 = lValue2;
		this.stringValue1 = stringValue1;
		this.stringValue2 = stringValue2;
	}
	
	public SQLDatas(int iValue1, long lValue1, long lValue2, String stringValue1, String stringValue2) {
		this.iValue1 = iValue1;
		this.lValue1 = lValue1;
		this.lValue2 = lValue2;
		this.stringValue1 = stringValue1;
		this.stringValue2 = stringValue2;
	}
	
	public SQLDatas(long lValue1, long lValue2, String stringValue1, String stringValue2, String stringValue3) {
		this.lValue1 = lValue1;
		this.lValue2 = lValue2;
		this.stringValue1 = stringValue1;
		this.stringValue2 = stringValue2;
		this.stringValue3 = stringValue3;
	}
	
	public int getIValue1() {
		return this.iValue1;
	}
	
	public int getIValue2() {
		return this.iValue2;
	}
	
	public int getIValue3() {
		return this.iValue3;
	}
	
	public int getIValue4() {
		return this.iValue4;
	}
	
	public int getIValue5() {
		return this.iValue5;
	}
	
	public int getIValue6() {
		return this.iValue6;
	}
	
	public int getIValue7() {
		return this.iValue7;
	}
	
	public long getLValue1() {
		return this.lValue1;
	}
	
	public long getLValue2() {
		return this.lValue2;
	}
	
	public byte getBValue1() {
		return this.bValue1;
	}
	
	public byte getBValue2() {
		return this.bValue2;
	}
	
	public String getStringValue1() {
		return this.stringValue1;
	}
	
	public String getStringValue2() {
		return this.stringValue2;
	}
	
	public String getStringValue3() {
		return this.stringValue3;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
