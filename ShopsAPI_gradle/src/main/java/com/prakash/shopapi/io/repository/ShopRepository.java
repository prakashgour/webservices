package com.prakash.shopapi.io.repository;

import org.springframework.data.repository.CrudRepository;

import com.prakash.shopapi.io.model.Shop;

/**
 * This is the CRUD repository to manage the shop details in database.
 * 
 * @author Prakash Gour
 * @see {@link org.springframework.data.repository.CrudRepository}}
 */
public interface ShopRepository extends CrudRepository<Shop, Long> {
	public Shop findByShopName(String shopName);
}
