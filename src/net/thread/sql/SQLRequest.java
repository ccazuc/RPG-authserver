package net.thread.sql;

import java.sql.SQLException;
import java.util.ArrayList;

import jdo.JDOStatement;
import net.Server;

public class SQLRequest {
	
	protected JDOStatement statement;
	protected final ArrayList<SQLDatas> datasList;
	protected final String name;
	protected final boolean debugActive;
	
	public SQLRequest(String request, String name) {
		try {
			this.statement = Server.getJDO().prepare(request);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		this.datasList = new ArrayList<SQLDatas>();
		this.name = name;
		this.debugActive = true;
	}
	
	public final void execute() {
		this.statement.clear();
		try {
			gatherData();
			this.statement.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		this.datasList.remove(0);
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean debugActive() {
		return this.debugActive;
	}
	
	public void addDatas(SQLDatas datas) {
		this.datasList.add(datas);
	}
	
	public void gatherData() throws SQLException {}
}
