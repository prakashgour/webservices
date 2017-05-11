package com.prakash.shopapi.io.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.maps.model.LatLng;
import com.prakash.shopapi.io.model.Shop;
import com.prakash.shopapi.io.service.ShopService;
import com.prakash.shopapi.io.utils.MapUtil;
import com.prakash.shopapi.io.utils.GoogleApiUtils; 

/**
 * 
 * @author Prakash Gour
 * @since  01-05-2017
 * @version
 *
 */
@RestController
@RequestMapping("/shopapi")
public class ShopController {
	private static final Logger log = LoggerFactory.getLogger(ShopController.class);

	@Autowired
	private ShopService shopService;

	/**
	 * Get All Shops
	 * 
	 * @return List<Shop> - List of shops
	 */
	@RequestMapping("/shops")
	public List<Shop> getAll() {
		log.info("Request is GET /shops");
		return shopService.getAllShops();
	}

	/**
	 * Get Shop by shopId
	 * 
	 * @param shopId - Id of shop which we want to get. 
	 * @return Shop - Shop
	 */
	@RequestMapping(value = "/shops/{shopId}", method = RequestMethod.GET)
	public ResponseEntity<Shop> getShopById(@PathVariable long shopId) {
		Shop shop = shopService.getShop(shopId);
		log.info("Request is GET /shops/{" + shopId + "}");
		if (shop != null) {
			return new ResponseEntity<>(shop, HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(shop, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Creating a new shop
	 * 
	 * @param shop - shop details provided by user
	 * @param ucb
	 * @return ResponseEntity<?> - Response with the status and headers value
	 */
	@RequestMapping(value = "/shops", method = RequestMethod.POST)
	public ResponseEntity<?> createShop(@RequestBody Shop shop, UriComponentsBuilder ucb) {
		log.info("Request is POST /shops");
		// Check if shop exist with the same name, return the message with
		// exiting shop id.
		if (shopService.ifShopExist(shop)) {
			Shop shop1 = shopService.getShopByName(shop.getShopName());
			return new ResponseEntity<>(
					"Shop with name " + shop.getShopName() + " already exist. Shop Id is " + shop1.getShopId(),
					HttpStatus.CONFLICT);
		} else {
			// Fetching the Longitude and latitude for newly shop to be creating
			LatLng latLng = GoogleApiUtils.getLatLng(GoogleApiUtils.getAddressString(shop));
			
			if(latLng == null) {
				return new ResponseEntity<>("Please enter correct address ", HttpStatus.BAD_REQUEST);
			}
			
			shop.setLatitude(latLng.lat);
			shop.setLongitude(latLng.lng);

			// Persisting the shop details
			shopService.addShop(shop);

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-type", "application/json");
			headers.setLocation(ucb.path("shopapi/shops/{id}").buildAndExpand(shop.getShopId()).toUri());
			return new ResponseEntity<>(headers, HttpStatus.CREATED);
		}

	}

	/**
	 * Search shop by latitude and longitude
	 * 
	 * @param longitude - longitude provided by user
	 * @param latitude - latitude provided by user
	 * @return ResponseEntity<?> - Response with the searched shop and headers value
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchShop(@QueryParam("longitude") double longitude,
			@QueryParam("latitude") double latitude) {
		log.info("Request is GET /shops?longitude=" + longitude + "&latitude=" + latitude);
		List<Shop> shops = null;
		shops = shopService.searchShop(longitude, latitude);

		if (shops.size() == 0) {
			return new ResponseEntity<>("No shop availabe at the latitude: " + latitude + " longitude: " + longitude,
					HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<Shop>>(shops, HttpStatus.FOUND);
		}
	}

	/** 
	 * Update the shop details of given shopId
	 * 
	 * @param shop - updated shop details
	 * @param shopId - shop id
	 * @param ucb - UriComponentsBuilder
	 * @return ResponseEntity<?> - Response with the status and headers value
	 */
	@RequestMapping(value = "/shops/{shopId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateShop(@RequestBody Shop shop, @PathVariable long shopId, UriComponentsBuilder ucb) {
		log.info("Request is PUT /shops/{" + shopId + "}");
		Shop oldShop = shopService.getShop(shopId);
		
		// if address changed
		if(GoogleApiUtils.isAddressUpdated(shop, oldShop)) {
			oldShop.setShopCity(shop.getShopCity());
			oldShop.setShopCountry(shop.getShopCountry());
			oldShop.setShopStreet(shop.getShopStreet());
			oldShop.setShopPincode(shop.getShopPincode());
			oldShop.setShopDistrict(shop.getShopDistrict());
			oldShop.setShopState(shop.getShopState());
			oldShop.setShopName(shop.getShopName());
			
			LatLng latLng = GoogleApiUtils.getLatLng(GoogleApiUtils.getAddressString(oldShop));
			
			if(latLng == null) {
				return new ResponseEntity<>("Please enter correct address ", HttpStatus.BAD_REQUEST);
			}
			
			oldShop.setLatitude(latLng.lat);
			oldShop.setLongitude(latLng.lng);
		} else {
			oldShop.setShopName(shop.getShopName());
		}
		
		
		shopService.updateShop(oldShop);

		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.setLocation(ucb.path("shopapi/shops/{id}").buildAndExpand(shop.getShopId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/shops/{shopId}", method = RequestMethod.DELETE)
	public String deleteShop(@PathVariable long shopId) {
		log.info("Request is DELETE /shops/{" + shopId + "}");
		shopService.deleteShop(shopId);
		return "Shop with id " + shopId + " has been deleted successfully.";
	}
	
	/**
	 * Search nearest shop by address .
	 * 
	 * @param city
	 * @param street
	 * @param dist
	 * @param state
	 * @param country
	 * @param pin
	 * @return
	 */
	@RequestMapping(value = "/searchNearest", method = RequestMethod.GET)
	public ResponseEntity<Shop> searchNearestShop(@QueryParam("city") String city, @QueryParam("street") String street, @QueryParam("dist") String dist,
			@QueryParam("state") String state, @QueryParam("country") String country, @QueryParam("pin") String pin) {
		log.info("Request is GET /shops?City=" + city + "&country=" + country + "&state=" + state + "&dist=" + dist
				+ "&pin=" + pin);

		Shop shop = new Shop();
		shop.setShopStreet(street);
		shop.setShopCity(city);
		shop.setShopState(state);
		shop.setShopCountry(country);
		shop.setShopDistrict(dist);
		shop.setShopPincode(pin);

		LatLng srclatLng = GoogleApiUtils.getLatLng(GoogleApiUtils.getAddressString(shop));
		log.info("src latitude =" + srclatLng.lat + " src longitude =" + srclatLng.lng);

		List<Shop> shops = null;
		shops = shopService.getAllShops();
		Map<Integer, Double> shopDistance = new HashMap<>();
		for (Shop shop2 : shops) {
			double distance = GoogleApiUtils.distance(srclatLng.lat, srclatLng.lng, shop2.getLatitude(),shop2.getLongitude(), "KM");
			shopDistance.put(Long.valueOf(shop2.getShopId()).intValue() , distance);
		}

		Map<Integer, Double>	map1 =MapUtil.sortByValue(shopDistance);
		System.out.println("Distances from source: " + map1);
		Integer requiredShopId = 0;
		Iterator<Integer> shopIds = map1.keySet().iterator();
		while(shopIds.hasNext()) {
			requiredShopId = shopIds.next();
			System.out.println(requiredShopId + " : " + map1.get(requiredShopId));
			break;
		}

		Shop shop3 = null;
		if(requiredShopId > 0) {
			for(Shop s : shops) {
				if(Long.valueOf(s.getShopId()).intValue() == requiredShopId) {
					shop3 = s;
					break;
				}
			}
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("Access-Control-Allow-Origin", "*");
		
		return new ResponseEntity<Shop>(shop3, headers, HttpStatus.OK);

	}
	
}
