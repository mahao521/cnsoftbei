package com.cnsoftbei.dmvisualization.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Database {
	private static Map<String, Set<String>> data = null; // 事务数据库

	public Map<String, Set<String>> getData() {
		return this.data;
	}

	public void getInstance(HashMap<String, Set<String>> dataSet) {
		if (null == data) {
			data = new HashMap<String, Set<String>>();
		}
		this.data = dataSet;
	}

	public void getInstance() {
		if (null == data) {
			data = new HashMap<String, Set<String>>();
		}
		Set<String> set1 = new TreeSet<String>();
		set1.add("纸巾");
		set1.add("香皂");
		set1.add("杯子");
		data.put("1", set1);

		Set<String> set2 = new TreeSet<String>();
		set2.add("啤酒");
		set2.add("纸巾");
		set2.add("楔子");
		data.put("2", set2);

		Set<String> set3 = new TreeSet<String>();
		set3.add("楔子");
		set3.add("可乐");
		set3.add("啤酒");
		data.put("3", set3);
		Set<String> set4 = new TreeSet<String>();
		set4.add("可乐");
		set4.add("啤酒");
		set4.add("纸巾");
		set4.add("杯子");
		set4.add("牙膏");
		data.put("4", set4);
		Set<String> set5 = new TreeSet<String>();
		set5.add("香皂");
		set5.add("啤酒");
		set5.add("楔子");
		set5.add("可乐");
		set5.add("纸巾");
		data.put("5", set5);
		System.out.println(data);
	}
}
