package net.connection;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import net.game.Player;
import net.game.WorldServer;

public class Connection {

	private Buffer wBuffer;
	private Buffer rBuffer;
	private SocketChannel socket;
	private int startPacketPosition;
	
	public Connection(SocketChannel socket, Player player) {
		this.socket = socket;
		this.wBuffer = new Buffer(socket, player);
		this.rBuffer = new Buffer(socket, player);
	}
	
	public Connection(SocketChannel socket, WorldServer server) {
		this.socket = socket;
		this.wBuffer = new Buffer(socket, server);
		this.rBuffer = new Buffer(socket, server);
	}

	public final void close() {
		try {
			if (this.socket != null)
				this.socket.close();
			this.socket = null;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SocketChannel getSocket() {
		return this.socket;
	}
	
	public final String getIpAdress() {
		return this.socket.socket().getInetAddress().getHostAddress();
	}
	
	public final void clearRBuffer() {
		this.rBuffer.clear();
	}
	
	public final void startPacket() {
		if(this.wBuffer.position() >= 3*this.wBuffer.capacity()/4) {
			send();
		}
		this.startPacketPosition = this.wBuffer.position();
		writeInt(0);
	}
	
	public final void endPacket() {
		int position = this.wBuffer.position();
		this.wBuffer.setPosition(this.startPacketPosition);
		this.wBuffer.writeInt(position-this.startPacketPosition);
		this.wBuffer.setPosition(position);
	}
	
	public final byte read() throws IOException {
		return this.rBuffer.read();
	}
	
	public final void send() {
		try {
			this.wBuffer.send();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final int rBufferRemaining() {
		return this.rBuffer.remaining();
	}
	
	public final int rBufferPosition() {
		return this.rBuffer.position();
	}
	
	public final void rBufferSetPosition(int position) {
		this.rBuffer.setPosition(position);
	}
	
	public final boolean hasRemaining() {
		return this.rBuffer.hasRemaining();
	}
	
	public final void writeBoolean(final boolean b) {
		this.wBuffer.writeBoolean(b);
	}
	
	public final boolean readBoolean() {
		return this.rBuffer.readBoolean();
	}
	
	public final void writeByte(final byte b) {
		this.wBuffer.writeByte(b);
	}
	
	public final byte readByte() {
		return this.rBuffer.readByte();
	}
	
	public final void writeShort(final short s) {
		this.wBuffer.writeShort(s);
	}
	
	public final short readShort() {
		return this.rBuffer.readShort();
	}
	
	public final void writeInt(final int i) {
		this.wBuffer.writeInt(i);
	}
	
	public final int readInt() {
		return this.rBuffer.readInt();
	}
	
	public final void writeLong(final long l) {
		this.wBuffer.writeLong(l);
	}
	
	public final long readLong() {
		return this.rBuffer.readLong();
	}
	
	public final void writeFloat(final float f) {
		this.wBuffer.writeFloat(f);
	}
	
	public final float readFloat() {
		return this.rBuffer.readFloat();
	}
	
	public final void writeDouble(final double d) {
		this.wBuffer.writeDouble(d);
	}
	
	public final double readDouble() {
		return this.rBuffer.readDouble();
	}
	
	public final void writeChar(final char c) {
		this.wBuffer.writeChar(c);
	}
	
	public final char readChar() {
		return this.rBuffer.readChar();
	}
	
	public final void writeString(final String s) {
		this.wBuffer.writeString(s);
	}
	
	public final String readString() {
		return this.rBuffer.readString();
}
	
}
