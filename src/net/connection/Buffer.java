package net.connection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import net.game.Player;
import net.game.WorldServer;

public class Buffer {

	private ByteBuffer buffer;	
	private boolean written;
	private SocketChannel socket;
	private Player player;
	private WorldServer server;
	
	public Buffer(SocketChannel socket, Player player) {
		this.buffer = ByteBuffer.allocateDirect(16000);
		this.socket = socket;
		this.player = player;
	}
	
	public Buffer(SocketChannel socket, WorldServer server) {
		this.buffer = ByteBuffer.allocateDirect(16000);
		this.socket = socket;
		this.server = server;
	}
	
	protected final void send() throws IOException {
		if(this.socket.isOpen()) {
			if(this.written) {
				send(this.buffer);
				this.written = false;
			}
		}
		else {
			throw new ClosedChannelException();
		}
	}

	private final void send(final ByteBuffer buffer) throws IOException {
		buffer.flip();
		while(buffer.hasRemaining()) {
			this.socket.write(buffer);
		}
		buffer.clear();
	}

	protected final byte read() throws IOException {
		return read(this.buffer);
	}
	
	private final byte read(final ByteBuffer buffer) throws IOException {
		buffer.clear();
		if(this.socket.read(buffer) >= 1) {
			buffer.flip();
			return 1;
		}
		return 2;
	}
	
	protected final boolean hasRemaining() {
		return this.buffer.hasRemaining();
	}

	protected final void writeString(final String s) {
		writeShort((short)s.length());
		int i = -1;
		while(++i < s.length()) {
			writeChar(s.charAt(i));
		}
		this.written = true;
	}
	
	protected final String readString() {
		final short length = readShort();
		final char[] chars = new char[length];
		int i = -1;
		while(++i < length) {
			chars[i] = readChar();
		}
		return new String(chars);
	}
	
	protected final void clear() {
		this.buffer.clear();
	}
	
	protected final void writeBoolean(final boolean b) {
		this.buffer.put((byte)(b?1:0));
		this.written = true;
	}
	
	protected final boolean readBoolean() {
		try {
			return this.buffer.get() == 1;
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return false;
		}
	}
	
	protected final void writeByte(final byte b) {
		this.buffer.put(b);
		this.written = true;
	}
	
	protected final byte readByte() {
		try {
			return this.buffer.get();
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return 0;
		}
	}
	
	protected final void writeShort(final short s) {
		this.buffer.putShort(s);
		this.written = true;
	}
	
	protected final short readShort() {
		try {
			return this.buffer.getShort();
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return 0;
		}
	}
	
	protected final void writeInt(final int i) {
		this.buffer.putInt(i);
		this.written = true;
	}
	
	protected final int readInt() {
		try {
			return this.buffer.getInt();
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return 0;
		}
	}
	
	protected final void writeLong(final long l) {
		this.buffer.putLong(l);
		this.written = true;
	}
	
	protected final long readLong() {
		try {
			return this.buffer.getLong();
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return 0;
		}
	}
	
	protected final void writeFloat(final float f) {
		this.buffer.putFloat(f);
		this.written = true;
	}
	
	protected final float readFloat() {
		try {
			return this.buffer.getFloat();
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return 0;
		}
	}
	
	protected final void writeDouble(final double d) {
		this.buffer.putDouble(d);
		this.written = true;
	}
	
	protected final double readDouble() {
		try {
			return this.buffer.getDouble();
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return 0;
		}
	}
	
	protected final void writeChar(final char c) {
		this.buffer.putChar((char)(Character.MAX_VALUE-c));
		this.written = true;
	}
	
	protected final char readChar() {
		try {
			return (char)(Character.MAX_VALUE-this.buffer.getChar());
		}
		catch(BufferUnderflowException e) {
			e.printStackTrace();
			this.player.close();
			return 0;
		}
	}
}
