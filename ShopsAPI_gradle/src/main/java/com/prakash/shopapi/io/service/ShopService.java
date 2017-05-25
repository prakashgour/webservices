package com.prakash.shopapi.io.service;

import java.util.List;

import com.prakash.shopapi.io.model.Shop;


/**
 * This interface <ShopService.java> provides the functions to manage the shop details.
 * 
 * @author Prakash Gour
 *
 */
public interface ShopService {
	public List<Shop> getAllShops();

	public Shop getShop(long shopId); 

	public void addShop(Shop shop);

	public void updateShop(Shop shop);

	public void deleteShop(long shopId);

	public Shop getShopByName(String shopName);

	public boolean ifShopExist(Shop shop);

	public List<Shop> searchShop(double longitude, double latitude);
}
