package com.prakash.shopapi.io.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.prakash.shopapi.io.constants.ShopAPIConstants;
import com.prakash.shopapi.io.model.Shop;

/**
 * This is the Utility class to handle the Google Map Api Request in order to
 * fulfill the user request.
 * 
 * @author Prakash Gour
 * @since Since MAY, 2017
 */
public class GoogleApiUtils {
	private static GeoApiContext geoApiContext = null;

	private static final Logger log = LoggerFactory.getLogger(GoogleApiUtils.class);

	public static LatLng getLatLng(String addrString) {
		GeoApiContext geoApiContext = GoogleApiUtils.getGeoApiContext();

		GeocodingResult geocodingResult = null;
		GeocodingApiRequest apiRequest = GeocodingApi.geocode(geoApiContext, addrString);
		try {
			GeocodingResult[] geo = apiRequest.await();
			System.out.println(geo.length);
			if (geo.length > 0) {
				geocodingResult = geo[0];
			}

		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());

		}

		if (geocodingResult != null) {
			return geocodingResult.geometry.location;
		} else {
			return null;
		}

	}

	public static String getAddressString(Shop shop) {
		StringBuilder sb = new StringBuilder(shop.getShopStreet());
		sb.append("+").append(shop.getShopCity()).append("+").append(shop.getShopDistrict()).append("+")
				.append(shop.getShopState()).append("+").append(shop.getShopDistrict()).append("+")
				.append(shop.getShopPincode());
		log.info("Address String: " + sb.toString());
		return sb.toString();
	}

	public static GeoApiContext getGeoApiContext() {
		log.info("Getting GeoApiContext");
		if (geoApiContext == null) {
			synchronized (GoogleApiUtils.class) {
				if (geoApiContext == null) {
					geoApiContext = new GeoApiContext(); /// ERR
					geoApiContext.setApiKey(ShopAPIConstants.API_KEY);
				}
			}
		}
		return geoApiContext;
	}

	// Get distance between two latlongs
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "KM") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	// This function converts decimal degrees to radians
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static boolean isAddressUpdated(Shop newShop, Shop oldShop) {
		boolean isAddressChanged = false;

		if (!newShop.getShopCity().equals(oldShop.getShopCity())) {
			isAddressChanged = true;
		} else if (!newShop.getShopStreet().equals(oldShop.getShopStreet())) {
			isAddressChanged = true;
		} else if (!newShop.getShopState().equals(oldShop.getShopState())) {
			isAddressChanged = true;
		} else if (!newShop.getShopDistrict().equals(oldShop.getShopDistrict())) {
			isAddressChanged = true;
		} else if (!newShop.getShopCountry().equals(oldShop.getShopCountry())) {
			isAddressChanged = true;
		} else if (!newShop.getShopPincode().equals(oldShop.getShopPincode())) {
			isAddressChanged = true;
		}

		return isAddressChanged;
	}
}
