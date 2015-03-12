package com.cnsoftbei.dmvisualization.apriori;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.cnsoftbei.dmvisualization.data.Dataparameter;

public class Japriori {
	private Data2Freq1ItemSet data2Freq1ItemSet;
	private Map<String, Integer> itemsCount;// 每项的出现次数计数
	private Map<Set<String>, Set<String>> itemsTransactionTable;// 项集对应的事务集标识列表transaction
	private List<Set<String>> freqItemSetList;// 项频繁项集

	public Japriori(Dataparameter dataparameter) {
		data2Freq1ItemSet = new Data2Freq1ItemSet(dataparameter);
		itemsCount = data2Freq1ItemSet.getItemsCount();
		itemsTransactionTable = data2Freq1ItemSet.getItem1TransactionTable();
		freqItemSetList = new ArrayList<>();

		//System.out.println(itemsCount);
		//System.out.println("----------------");
		//System.out.println(itemsTransactionTable);
		//System.out.println();
	}

	public void J_apriori() {
		//System.out.println("-------------------1-----------------------");
		List<Set<String>> currentList_L = new ArrayList<>();
		currentList_L = data2Freq1ItemSet.getFreqItemSetList();
		//System.out.println(currentList_L);
		//System.out.println();
		for (int k = 2; !currentList_L.isEmpty(); k++) {
//			System.out.println("-------------------" + k
//					+ "-----------------------");
			currentList_L = ZS_apriori_gen(currentList_L, k);
			// System.out.println(currentList_L);
			// System.out.println();
			if (null == currentList_L) {
				break;
			}
		}
//		System.out.println("---freqItemSetList---" + this.freqItemSetList);
//		System.out.println("---itemsTransactionTable---"
//				+ this.itemsTransactionTable);
	}

	private List<Set<String>> ZS_apriori_gen(List<Set<String>> preItemSetList,
			int k) {
		Map<Set<String>, Set<String>> tempItemsTransactionTable = null;// 临时项集对应的事务集标识列表
		Map<String, Integer> tempItemsCount = null;// 临时项目计数表
		List<String> needRemoveItem = null;// 需要移除的项
		List<Set<String>> k_ItemSet = null;
		Set<String> transactionSameSet = null;// 项集的事务集的交集
		Set<Integer> itemSetIndex = null;
		Set<Integer> needRemoveItemIndex = null;

		int size = preItemSetList.size();
		if (size > k) {// k-1项频繁集能生成k项频繁集，则k-1项频繁集的大小必大于k
			tempItemsTransactionTable = new HashMap<>();
			tempItemsCount = new HashMap<>();
			needRemoveItem = new ArrayList<>();
			k_ItemSet = new ArrayList<>();
			transactionSameSet = new HashSet<>();
			itemSetIndex = new HashSet();
			needRemoveItemIndex = new HashSet();

			// 不能构成Lk项的项目 由itemsCount算出
//			System.out.println("不能构成L" + k
//					+ "项的项目 由itemsCount算出. itemsCount为-- " + this.itemsCount);
			needRemoveItem = getNeedRemoveItem(this.itemsCount, k);
//			System.out.println("不能构成L" + k + "项的项目" + needRemoveItem);

			// 第一次剔除preItemSet中包含不能构成Lk项的项目的项集
			// 并把剔除项加入到freqItemSetList
			preItemSetList = cutPreItemSet(preItemSetList, needRemoveItem);
			// System.out.println("----cutPreItemSet()完毕----");
			// System.out.println();

			size = preItemSetList.size();
			for (int i = 0; i < size - 1; i++) {
				itemSetIndex.add(i);
				for (int j = i + 1; j < size; j++) {
					String[] strA = preItemSetList.get(i)
							.toArray(new String[0]);
					String[] strB = preItemSetList.get(j)
							.toArray(new String[0]);
					if (isCanLink(strA, strB)) { // 判断两个k-1项集是否符合连接成k项集的条件　
						TreeSet<String> set = new TreeSet<String>();
						for (String str : strA) {
							set.add(str);
						}
						set.add((String) strB[strB.length - 1]); // 连接成k项集

						// set项的事务集是否有交集
						transactionSameSet = hasSameTransactionSet(
								this.itemsTransactionTable.get(preItemSetList
										.get(i)),
								this.itemsTransactionTable.get(preItemSetList
										.get(j)));

						this.itemsTransactionTable.put(set, transactionSameSet);
						// 交集是否符合条件
//						System.out.println();
//						System.out.println("@@@@@@@@@@@@@@@@@");
//						System.out.println(transactionSameSet.size());
//						System.out.println();
//						System.out.println();
						
						if (transactionSameSet.size() >= data2Freq1ItemSet
								.getMinSupCount()) {
							needRemoveItemIndex.add(i);
							needRemoveItemIndex.add(j);
							k_ItemSet.add(set);
//							System.out.println(k_ItemSet);

							// 放入临时表
							recordTtemsCount(tempItemsCount, set);// 记录Lk中个item出现次数
							// System.out.println(set + "  SameSet---"
							// + transactionSameSet);
							// System.out.println("k_ItemSet  " + k_ItemSet);
							// System.out.println("tempItemsCount  "
							// + tempItemsCount);
							// System.out.println("tempItemsTransactionTable  "
							// + tempItemsTransactionTable);
							// System.out.println();
						}
					}
				}
			}
//			System.out.println("********k_ItemSet*************");
//			System.out.println(k_ItemSet);
			// if (2 == k) {
			// freq2ItemSetList = k_ItemSet;
			// }
			// this.freqItemSetList = k_ItemSet;
			if (k > 2) {
				itemSetIndex.add(size - 1);
				itemSetIndex.removeAll(needRemoveItemIndex);
				// System.out.println("itemSetIndex  " + itemSetIndex);
				for (Integer i : itemSetIndex) {
					add2FreqItemSetList(preItemSetList.get(i), k_ItemSet, k);
				}
			}
			this.itemsCount = tempItemsCount;
			// this.itemsTransactionTable = tempItemsTransactionTable;
		}
		
		if (null == k_ItemSet) {
			freqItemSetList.addAll(preItemSetList);
		}
		// System.out.println();
		// System.out.println("****&&&&&&^^^^%%%%%$$$$######@@@@@@");
		// System.out.println(itemsCount);
		// System.out.println(itemsTransactionTable);

		// System.out.println();

		return k_ItemSet;
	}

	private List<String> getNeedRemoveItem(Map<String, Integer> itemsCount,
			int k) {
		List<String> needRemoveItem = new ArrayList<>();
		Iterator<Map.Entry<String, Integer>> iteratorItemsCount = itemsCount
				.entrySet().iterator();
		while (iteratorItemsCount.hasNext()) {
			Map.Entry<String, Integer> entry = iteratorItemsCount.next();
			if (entry.getValue() < k - 1) {// 每个项在Lk-1中要至少出现k-1次
				needRemoveItem.add(entry.getKey());// 添加不能构成Lk项的项目
			}
		}
		return needRemoveItem;
	}

	private List<Set<String>> cutPreItemSet(List<Set<String>> itemsSetList,
			List<String> needRemoveItems) {
		// System.out.println();//
		// System.out.println("-----cutPreItemSet-----");
		// System.out.println(itemsSetList);
		// System.out.println(needRemoveItems);

		if (needRemoveItems.isEmpty()) {
			return itemsSetList;
		}

		Iterator<Set<String>> it = itemsSetList.iterator();
		while (it.hasNext()) {
			Set<String> itemsSet = it.next();
			if (isHasSame(itemsSet, needRemoveItems)) {
				freqItemSetList.add(itemsSet);
				it.remove();
				// System.out.println("@@@@@@@@@@@@@@@@@@@@@-" + itemsSet);
			}
		}
		// System.out.println("freqItemSetList   " + freqItemSetList);
		return itemsSetList;
	}

	private boolean isCanLink(String[] strA, String[] strB) {
		boolean flag = true;
		if (strA.length == strB.length) {
			if (strA[strA.length - 1].equals(strB[strB.length - 1])) {
				flag = false;
				return flag;
			}
			for (int i = 0; i < strA.length - 1; i++) {
				if (!strA[i].equals(strB[i])) {
					flag = false;
					return flag;
				}
			}
		} else {
			flag = false;
		}
		return flag;
	}

	private boolean isHasSame(Collection<String> A, Collection<String> B) {
		Set<String> sameSet = new HashSet<>();
		sameSet.addAll(A);
		sameSet.retainAll(B);
		if (sameSet.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private void recordTtemsCount(Map<String, Integer> itemsCount,
			Set<String> set) {
		int count;
		for (String item : set) {
			if (!itemsCount.containsKey(item)) {
				itemsCount.put(item, 1);
			} else {
				count = itemsCount.get(item);
				count++;
				itemsCount.put(item, count);
			}
		}
	}

	private Set<String> hasSameTransactionSet(Set<String> A, Set<String> B) {
		Set<String> sameSet = new HashSet<String>();
		sameSet.addAll(A);
		sameSet.retainAll(B);
		return sameSet;
	}

	private void add2FreqItemSetList(Set<String> itemsSetList,
			List<Set<String>> ItemSet, int k) {
		// System.out.println("--add2FreqItemSetList--");
		// System.out.println(itemsSetList);
		// System.out.println(ItemSet);
		// System.out.println("---------------------------");
		boolean flag = true;
		Set<String> setItem = null;
		for (Set<String> set : ItemSet) {
			setItem = hasSameTransactionSet(itemsSetList, set);
			if (setItem.size() >= k - 1) {
				flag = false;
				break;
			}
		}
		if (flag) {
			this.freqItemSetList.add(itemsSetList);
		}
		// System.out.println("freqItemSetList----" + freqItemSetList);
	}

	public Data2Freq1ItemSet getData2Freq1ItemSet() {
		return data2Freq1ItemSet;
	}

	public Map<String, Integer> getItemsCount() {
		return itemsCount;
	}

	public Map<Set<String>, Set<String>> getItemsTransactionTable() {
		return itemsTransactionTable;
	}

	public List<Set<String>> getFreqItemSetList() {
		return freqItemSetList;
	}

	public int getTransactionSum() {
		return data2Freq1ItemSet.getTransactionSum();
	}

	public int getMinSupCount() {
		return data2Freq1ItemSet.getMinSupCount();
	}
}
