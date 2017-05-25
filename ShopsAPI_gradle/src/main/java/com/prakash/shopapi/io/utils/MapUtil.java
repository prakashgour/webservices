package com.prakash.shopapi.io.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.prakash.shopapi.io.model.Shop;

/**
 * This class <code> Utility.java</code> provides the generic method to sort the
 * Map by value
 * 
 * @author Prakash Gour
 */
public class Utility {
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static boolean isContainNullValue(Shop shop) {
		if(shop.getShopStreet()==null||shop.getShopCity()==null||shop.getShopCountry()==null||shop.getShopDistrict()==null||shop.getShopDistrict()==null||shop.getShopName()==null
				||shop.getShopPincode()==null){
			return true;
		}
		return false;
		
	}
}
