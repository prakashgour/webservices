package com.prakash.shopapi.io;

import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.prakash.shopapi.io.model.Shop;
import com.prakash.shopapi.io.repository.ShopRepository;
import com.prakash.shopapi.io.service.ShopServiceImpl;

/**
 * This is the test class extending ShopsApiGradleApplicationTests to execute test cases
 * 
 * @author Prakash Gour
 */

public class ShopApiTest extends ShopsApiGradleApplicationTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ShopRepository shopRepository;

	private ShopServiceImpl shopServiceImpl;

	@SuppressWarnings("unused")
	private MockMvc mockMvc;
	private Shop shop;

	@Before
	public void setUp() {

		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		shopServiceImpl = new ShopServiceImpl();
		shopServiceImpl.setShopRepository(shopRepository);

		shop = new Shop("MyShop", "datta mandir", "Pune", "Pune", "Maharastra", "India", "411057");

		if (!shopServiceImpl.ifShopExist(shop)) {
			shopServiceImpl.addShop(shop);
		}

	}

	@Test
	public void testGetAllShops() throws Exception {

		Collection<Shop> list = shopServiceImpl.getAllShops();
		Assert.assertNotNull(list);

	}

	@Test
	public void testGetShopsById() throws Exception {

		Assert.assertEquals("411057", shopServiceImpl.getShopByName("MyShop").getShopPincode());

	}

	@Test
	public void testDeleteShops() throws Exception {

		shopServiceImpl.deleteShop(shopServiceImpl.getShopByName("MyShop").getShopId());
		Assert.assertNull(shopServiceImpl.getShopByName("MyShop"));

	}

	@After
	public void tearDown() {

	}

}
