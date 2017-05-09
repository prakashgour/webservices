package com.prakash.shopapi.io.repository;

import org.springframework.data.repository.CrudRepository;

import com.prakash.shopapi.io.model.Shop;

public interface ShopRepository extends CrudRepository<Shop, Long> {
	public Shop findByShopName(String shopName);
}
