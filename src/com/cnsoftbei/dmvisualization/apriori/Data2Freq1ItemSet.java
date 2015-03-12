package com.cnsoftbei.dmvisualization.apriori;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.cnsoftbei.dmvisualization.data.Dataparameter;

public class Data2Freq1ItemSet {
	private Dataparameter dataparameter;
	private Map<String, Integer> itemsCount;// 每项的出现次数计数
	private int transactionSum;
	private int minSupCount;
	private Map<Set<String>, Set<String>> item1TransactionTable;
	// 1项 对应的事务集标识列表transaction
	private List<Set<String>> freqItemSetList;// 频繁项集

	
	
	public Data2Freq1ItemSet(Dataparameter dataparameter) {
		this.dataparameter = dataparameter;
		this.itemsCount = new HashMap<String, Integer>();
		this.item1TransactionTable = new HashMap<>();
		this.transactionSum = dataparameter.getDatabaseSum();
		this.minSupCount = (int) (transactionSum * dataparameter.getMinSup());
		this.freqItemSetList = new ArrayList<>();
		
		this.createFreq1ItemSet();
	}

	public void createFreq1ItemSet() {
		// 获取数据的映射关系集的Iterator
		Iterator<Map.Entry<String, Set<String>>> iterator = dataparameter
				.getDatabase().getData().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Set<String>> entry = iterator.next();
			String transaction = entry.getKey();// 事务
			Set<String> itemSet = entry.getValue();// 事务里的项集

			int count = 0;
			if (!itemSet.isEmpty()) {
				for (String item : itemSet) {
					// 为每项的出现次数计数
					if (!itemsCount.containsKey(item)) {// 不包含该项
						itemsCount.put(item, 1);
					} else {
						count = itemsCount.get(item);// 包含该项
						count++;
						itemsCount.put(item, count);
					}

					// 将项 出现的事务集 保存
					Set<String> itemsSet = new TreeSet<>();
					itemsSet.add(item);

					if (!item1TransactionTable.containsKey(itemsSet)) {
						Set<String> set = new HashSet<>();
						set.add(transaction);
						item1TransactionTable.put(itemsSet, set);
					} else {
						Set<String> set = new HashSet<>();
						set = item1TransactionTable.get(itemsSet);
						set.add(transaction);
						item1TransactionTable.put(itemsSet, set);
					}
				}
			}
		}
//		System.out.println("*******************************");
//		System.out.println(itemsCount);
//		System.out.println("------------------------------");
//		System.out.println(item1TransactionTable);
//		System.out.println("*******************************");

		/**
		 * 剔除一项集中 不频繁项
		 */
		// 过滤掉itemsCount和item1TransactionTable
		Iterator<Map.Entry<String, Integer>> it = itemsCount.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();
			if (entry.getValue() < this.minSupCount) {
				Set<String> itemsSet = new HashSet<>();
				itemsSet.add(entry.getKey());
				item1TransactionTable.remove(itemsSet);
				it.remove();
			}
		}

//		System.out.println("################################");
//		System.out.println(this.itemsCount);
//		System.out.println("------------------------------");
//		System.out.println(item1TransactionTable);
//		System.out.println("###############################");

		
		// 生成频繁项集FreqItemSetList
		Iterator<Map.Entry<String, Integer>> it2 = itemsCount.entrySet()
				.iterator();
		Set<String> set = null;
		while (it2.hasNext()) {
			Map.Entry<String, Integer> entry = it2.next();
			set = new TreeSet<>();
			set.add(entry.getKey());
			freqItemSetList.add(set);
		}
		
//		System.out.println(freqItemSetList);
	}

//	public static void main(String[] args) {
//		Database data = new Database();
//		data.getInstance();
//		System.out.println(data.getData());
//		Dataparameter dataparameter = new Dataparameter(data, 0.5, 0.5);
//		Data2Freq1ItemSet j = new Data2Freq1ItemSet(dataparameter);
//	}

	public Map<String, Integer> getItemsCount() {
		return itemsCount;
	}

	public int getTransactionSum() {
		return transactionSum;
	}

	public int getMinSupCount() {
		return minSupCount;
	}

	public Map<Set<String>, Set<String>> getItem1TransactionTable() {
		return item1TransactionTable;
	}

	public List<Set<String>> getFreqItemSetList() {
		return freqItemSetList;
	}
	
}
