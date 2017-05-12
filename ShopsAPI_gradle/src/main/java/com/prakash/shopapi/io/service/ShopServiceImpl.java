package com.prakash.shopapi.io.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prakash.shopapi.io.model.Shop;
import com.prakash.shopapi.io.repository.ShopRepository;

/**
 * @author Prakash Gour
 * Since MAY, 2017
 */
@Service
public class ShopServiceImpl implements ShopService {
	private static final Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);

	@Autowired
	private ShopRepository shopRepository;

	public ShopRepository getShopRepository() {
		return shopRepository;
	}

	public void setShopRepository(ShopRepository shopRepository) {
		this.shopRepository = shopRepository;
	}

	public List<Shop> getAllShops() {
		List<Shop> shops = new ArrayList<>();
		shopRepository.findAll().forEach(shops::add);
		return shops;
	}

	public Shop getShop(long shopId) {
		return shopRepository.findOne(shopId);
	}
 
	public void addShop(Shop shop) {
		shopRepository.save(shop);
	}

	public void updateShop(Shop shop) {
		shopRepository.save(shop);
	}

	public void deleteShop(long shopId) {
		shopRepository.delete(shopId);
	}

	public Shop getShopByName(String shopName) {
		return shopRepository.findByShopName(shopName);
	}

	public boolean ifShopExist(Shop shop) {
		return getShopByName(shop.getShopName()) != null;
	}

	public List<Shop> searchShop(double longitude, double latitude) {
		List<Shop> shops = new ArrayList<>();
		log.info("Latitude: " + latitude + " Longitude: " + longitude);
		for (Shop shop : getAllShops()) {
			if ((shop.getLatitude() == latitude) && (shop.getLongitude() == longitude)) {
				shops.add(shop);
			}
		}
		
		return shops;
	}
}
