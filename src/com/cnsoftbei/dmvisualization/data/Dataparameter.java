package com.cnsoftbei.dmvisualization.data;


//数据参数
public class Dataparameter {
	private Database database;
	private int databaseSum;//事务数
	private double minSup;// 最小支持度
	private double minConf;// 最小置信度

	
	public Dataparameter(Database database, double minSup, double minConf) {
		this.database = database;
		this.minSup = minSup;
		this.minConf = minConf;
		this.databaseSum = database.getData().size() ;
	}

	
	
	public int getDatabaseSum() {
		return databaseSum;
	}

	public Database getDatabase() {
		return database;
	}

	public double getMinSup() {
		return minSup;
	}

	public double getMinConf() {
		return minConf;
	}

	
}
