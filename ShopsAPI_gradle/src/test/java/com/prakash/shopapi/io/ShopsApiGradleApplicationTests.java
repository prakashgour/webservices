package com.prakash.shopapi.io;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prakash.shopapi.io.model.Shop;
import com.prakash.shopapi.io.repository.ShopRepository;
import com.prakash.shopapi.io.service.ShopServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopsApiGradleApplicationTests {
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Test
	public void contextLoads() {
		Shop shop= new Shop("MyShop", "datta mandir", "Pune", "Pune", "Maharastra" ,"india","411057");
		
		ShopServiceImpl shopServiceImpl=new ShopServiceImpl();
		shopServiceImpl.addShop(shop);
		
		Shop shopTest = shopRepository.findByShopName("MyShop");
		
		assertThat(shopTest.getShopName()).isEqualTo("MyShop");
		assertThat(shopTest.getShopCity()).isEqualTo("Pune");
		assertThat(shopTest.getShopCountry()).isEqualTo("India");
		assertThat(shopTest.getShopDistrict()).isEqualTo("Pune");
		
		
		
	}

}
