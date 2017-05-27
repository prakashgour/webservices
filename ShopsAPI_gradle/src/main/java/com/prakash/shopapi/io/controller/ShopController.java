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
import com.prakash.shopapi.io.constants.ShopAPIConstants;
import com.prakash.shopapi.io.exception.NoDataFoundException;
import com.prakash.shopapi.io.exception.NullValueException;
import com.prakash.shopapi.io.model.Shop;
import com.prakash.shopapi.io.service.ShopService;
import com.prakash.shopapi.io.utils.GoogleApiUtils;
import com.prakash.shopapi.io.utils.Utility;

/**
 * This is the Rest controller class for API. It has all the resources
 * pertaining to each URI.
 *
 * @author Prakash Gour
 * @see {@link com.prakash.shopapi.io.service.ShopService}
 */
@RestController
@RequestMapping("/shopapi")
public class ShopController {

	private static final Logger log = LoggerFactory.getLogger(ShopController.class);

	@Autowired
	private ShopService shopService;

	/**
	 * Creating a new shop
	 * 
	 * @param shop
	 *            - shop details provided by user
	 * @param ucb
	 * @return ResponseEntity<?> - Response with the status and headers value
	 */

	@RequestMapping(value = "/shops", method = RequestMethod.POST)
	public ResponseEntity<?> createShop(@RequestBody Shop shop, UriComponentsBuilder ucb) {
		// Check if shop exist with the same name
		if (!Utility.isContainNullValue(shop)) {
			if (shopService.ifShopExist(shop)) {

				Shop shop1 = shopService.getShopByName(shop.getShopName());
				Shop shop2 = new Shop(shop1.getShopName(), shop1.getShopStreet(), shop1.getShopCity(),

						shop1.getShopDistrict(), shop1.getShopState(), shop1.getShopCountry(), shop1.getShopPincode());
				shop2.setLatitude(shop1.getLatitude());
				shop2.setLongitude(shop1.getLongitude());
				shop2.setShopId(shop1.getShopId());

				// Set the shopId to Shop passed by UI and update the shop
				// details.

				// Check if address changed w. r. t. existing shop
				if (GoogleApiUtils.isAddressUpdated(shop, shop1)) {
					LatLng latLng = GoogleApiUtils.getLatLng(GoogleApiUtils.getAddressString(shop));
					if (latLng == null) {
						log.info(ShopAPIConstants.INCORRECT_ADD, HttpStatus.BAD_REQUEST);
						return new ResponseEntity<>(ShopAPIConstants.INCORRECT_ADD, HttpStatus.BAD_REQUEST);
					}

					shop.setLatitude(latLng.lat);
					shop.setLongitude(latLng.lng);
					shop.setShopId(shop1.getShopId());
					shopService.updateShop(shop);

					return new ResponseEntity<>(ShopAPIConstants.SHOP_UPDATED + " Previous Lat:" + shop2.getLatitude()
							+ " long:" + shop.getLongitude() + "  Latest Lat:" + shop.getLatitude() + " long:"
							+ shop2.getLongitude(), HttpStatus.OK); // shop2
				} else {
					log.info(ShopAPIConstants.SHOP_EXIST_MSG, HttpStatus.CONFLICT);
					return new ResponseEntity<>(ShopAPIConstants.SHOP_EXIST_MSG +" Latitude:"+shop1.getLatitude() + " Longitude:"
							+ shop1.getLongitude() + " Shop ID:" + shop1.getShopId(), HttpStatus.CONFLICT);
				}

			} else {

				// Fetching the Longitude and latitude for newly shop to be
				// creating
				LatLng latLng = GoogleApiUtils.getLatLng(GoogleApiUtils.getAddressString(shop));

				if (latLng == null) {
					log.info(ShopAPIConstants.INCORRECT_ADD, HttpStatus.BAD_REQUEST);
					return new ResponseEntity<>(ShopAPIConstants.INCORRECT_ADD, HttpStatus.BAD_REQUEST);
				}

				shop.setLatitude(latLng.lat);
				shop.setLongitude(latLng.lng);

				// Persisting the shop details
				shopService.addShop(shop);

				log.info(ShopAPIConstants.SHOP_CREATED, HttpStatus.CREATED);
				return new ResponseEntity<>(ShopAPIConstants.SHOP_CREATED, HttpStatus.CREATED);
			}

		} else {

			throw new NullValueException(ShopAPIConstants.MANDATORY);
		}

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
	public ResponseEntity<Shop> searchNearestShop(@QueryParam("city") String city, @QueryParam("street") String street,
			@QueryParam("dist") String dist, @QueryParam("state") String state, @QueryParam("country") String country,
			@QueryParam("pin") String pin) {

		Shop nullValuecheck = new Shop("", street, city, dist, state, country, pin);

		if (!Utility.isContainNullValue(nullValuecheck)) {
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
				double distance = GoogleApiUtils.distance(srclatLng.lat, srclatLng.lng, shop2.getLatitude(),
						shop2.getLongitude(), "KM");
				shopDistance.put(Long.valueOf(shop2.getShopId()).intValue(), distance);
			}
			// Getting nearest shop
			Map<Integer, Double> map1 = Utility.sortByValue(shopDistance);
			System.out.println("Distances from source: " + map1);
			Integer requiredShopId = 0;
			Iterator<Integer> shopIds = map1.keySet().iterator();
			while (shopIds.hasNext()) {
				requiredShopId = shopIds.next();
				System.out.println(requiredShopId + " : " + map1.get(requiredShopId));
				break;
			}

			Shop shop3 = null;
			if (requiredShopId > 0) {
				for (Shop s : shops) {
					if (Long.valueOf(s.getShopId()).intValue() == requiredShopId) {
						shop3 = s;
						break;
					}
				}
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("content-type", "application/json");
			headers.add("Access-Control-Allow-Origin", "*");

			if (shop3 != null) {
				log.info(ShopAPIConstants.NEAREST_FOUND, HttpStatus.FOUND);
				return new ResponseEntity<Shop>(shop3, headers, HttpStatus.FOUND);
			} else {
				throw new NoDataFoundException(ShopAPIConstants.NEAREST_NOT_FOUND);
			}
		}

		else {
			throw new NullValueException(ShopAPIConstants.MANDATORY);
		}
	}

	/**
	 * Get All Shops
	 * 
	 * @return List<Shop> - List of shops
	 */
	@RequestMapping("/shops")
	public List<Shop> getAllShop() {
		log.info(ShopAPIConstants.RETURN_SHOPS);

		List<Shop> shopList = shopService.getAllShops();
		if (shopList == null || shopList.size() == 0) {
			throw new NoDataFoundException(ShopAPIConstants.EMPTY_STORE);
		}
		return shopList;
	}

	/**
	 * Search shop by latitude and longitude
	 * 
	 * @param longitude
	 *            - longitude provided by user
	 * @param latitude
	 *            - latitude provided by user
	 * @return ResponseEntity<?> - Response with the searched shop and headers
	 *         value
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchShop(@QueryParam("longitude") Double longitude,
			@QueryParam("latitude") Double latitude) {
		
		if (longitude == null || latitude == null) {
			throw new NullValueException(ShopAPIConstants.MANDATORY);
		} else {
			log.info("Request is GET /shops?longitude=" + longitude + "&latitude=" + latitude);
			List<Shop> shops = null;
			shops = shopService.searchShop(longitude, latitude);

			if (shops.size() == 0) {
				return new ResponseEntity<>(ShopAPIConstants.NO_SHOP_AVAIL, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<Shop>>(shops, HttpStatus.FOUND);
			}
		}

	}

	/**
	 * Update the shop details of given shopId
	 * 
	 * @param shop
	 *            - updated shop details
	 * @param shopId
	 *            - shop id
	 * @param ucb
	 *            - UriComponentsBuilder
	 * @return ResponseEntity<?> - Response with the status and headers value
	 */

	@RequestMapping(value = "/shops/{shopId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateShop(@RequestBody Shop shop, @PathVariable long shopId, UriComponentsBuilder ucb) {

		Shop oldShop = shopService.getShop(shopId);

		// if address changed
		if (GoogleApiUtils.isAddressUpdated(shop, oldShop)) {
			oldShop.setShopCity(shop.getShopCity());
			oldShop.setShopCountry(shop.getShopCountry());
			oldShop.setShopStreet(shop.getShopStreet());
			oldShop.setShopPincode(shop.getShopPincode());
			oldShop.setShopDistrict(shop.getShopDistrict());
			oldShop.setShopState(shop.getShopState());
			oldShop.setShopName(shop.getShopName());

			LatLng latLng = GoogleApiUtils.getLatLng(GoogleApiUtils.getAddressString(oldShop));

			if (latLng == null) {
				log.info(ShopAPIConstants.INCORRECT_ADD, HttpStatus.BAD_REQUEST);
				return new ResponseEntity<>(ShopAPIConstants.INCORRECT_ADD, HttpStatus.BAD_REQUEST);
			}

			oldShop.setLatitude(latLng.lat);
			oldShop.setLongitude(latLng.lng);
		} else {
			oldShop.setShopName(shop.getShopName());
		}

		shopService.updateShop(oldShop);

		return new ResponseEntity<>(ShopAPIConstants.SHOP_UPDATED, HttpStatus.OK);
	}

	/**
	 * Get Shop by shopId
	 * 
	 * @param shopId
	 *            - Id of shop which we want to get.
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
}
