package com.cnsoftbei.dmvisualization.test;


import com.cnsoftbei.dmvisualization.apriori.Japriori;
import com.cnsoftbei.dmvisualization.data.Database;
import com.cnsoftbei.dmvisualization.data.Dataparameter;

public class Work {

	public static void main(String[] args) {
		Database data = new Database();
		data.getInstance();
		Dataparameter dataparameter = new Dataparameter(data, 0.5, 0.5);

		Japriori japriori = new Japriori(dataparameter);
		japriori.J_apriori();

		System.out.println(japriori.getFreqItemSetList());
	}

}
