package com.prakash.shopapi.io.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "shop_api")
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long shopId;
	@NotNull
	@Column(name = "shop_name")
	private String shopName;
	@NotNull
	private String shopStreet;
	private String shopCity;
	private String shopDistrict;
	@NotNull
	private String shopState;

	@NotNull
	private String shopCountry;
	@NotNull
	private String shopPincode;
	private double latitude;
	private double longitude;

	public Shop(String shopName, String shopStreet, String shopCity, String shopDistrict, String shopState,
			String shopCountry, String shopPincode) {
		super();
		this.shopName = shopName;
		this.shopStreet = shopStreet;
		this.shopCity = shopCity;
		this.shopDistrict = shopDistrict;
		this.shopState = shopState;
		this.shopCountry = shopCountry;
		this.shopPincode = shopPincode;
	}

	public Shop() {

	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopStreet() {
		return shopStreet;
	}

	public void setShopStreet(String shopStreet) {
		this.shopStreet = shopStreet;
	}

	public String getShopCity() {
		return shopCity;
	}

	public void setShopCity(String shopCity) {
		this.shopCity = shopCity;
	}

	public String getShopDistrict() {
		return shopDistrict;
	}

	public void setShopDistrict(String shopDistrict) {
		this.shopDistrict = shopDistrict;
	}

	public String getShopState() {
		return shopState;
	}

	public void setShopState(String shopState) {
		this.shopState = shopState;
	}

	public String getShopCountry() {
		return shopCountry;
	}

	public void setShopCountry(String shopCountry) {
		this.shopCountry = shopCountry;
	}

	public String getShopPincode() {
		return shopPincode;
	}

	public void setShopPincode(String shopPincode) {
		this.shopPincode = shopPincode;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Shop [shopId=" + shopId + ", shopName=" + shopName + ", shopStreet=" + shopStreet + ", shopCity="
				+ shopCity + ", shopDistrict=" + shopDistrict + ", shopState=" + shopState + ", shopCountry="
				+ shopCountry + ", shopPincode=" + shopPincode + "]";
	}
}
