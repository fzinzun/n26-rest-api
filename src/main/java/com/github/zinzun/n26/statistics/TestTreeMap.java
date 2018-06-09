package com.github.zinzun.n26.statistics;

import java.util.TreeMap;

public class TestTreeMap {
	private static TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
	public static void main(String[] args) {
		treeMap.put(1, 1);
		treeMap.put(2, 2);
		//treeMap.put(3, 3);
		treeMap.put(4, 4);
		treeMap.put(5, 5);
		treeMap.put(6, 6);
		
		int key = 3;
		System.out.println(treeMap.headMap(key, true));
		System.out.println(treeMap.tailMap(key, true));
	}

}
