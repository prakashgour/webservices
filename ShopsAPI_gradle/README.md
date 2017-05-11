ShopsAPI

Please read below instructions to use API.

##Deploy the ShopsAPI.war in the Application server of host and then use the localhost and 8090 in the below given URI’s and test the all the operation in postman.

##To create the shop use below format  
URI: http://localhost:8090/shopapi/shops
Method: POST
Headers: 
1. content-type=application\json
Body:
Select raw type and use below json format.

{
  "shopName": "XYZ Cloths Store",
  "shopStreet": "Harda 2",
  "shopCity": "Harda",
  "shopDistrict": "Harda",
  "shopState": "Madhya Pradesh",
  "shopCountry": "India",
  "shopPincode": "461335"
  }

##To update the shop 
To update the shop’s address we should know shop Id first
If Shop name already exist it will return shop ID , then pass the shop ID in uri and new address in json as below,
URI: http://localhost:8090/shopapi/shops/<shopId>
Method: PUT
Headers: 
1. content-type=application\json
Body:
Select raw type and use below json format.

{
  "shopName": " XYZ Cloths Store",
  "shopStreet": "Pune ",
  "shopCity": "Pune",
  "shopDistrict": "Pune",
  "shopState": "Maharastra",
  "shopCountry": "India",
  "shopPincode": "411057"
  }

## URI table for all services
Operation:
Method:
URI:

Add new Shop
POST
http://localhost:8090/shopapi/shops

2 Get all shops
GET
http://localhost:8090/shopapi/shops

3 Get Shop by Shop Id
GET
http://localhost:8090/shopapi/shops/{shopId}

4 Search Shop by Shop Name
GET
http://localhost:8090/shopapi/shops?shopName=<shop_name>

5 Update the shop details
PUT
http://localhost:8090/shopapi/shops/{shopId}

6 Delete Shop
DELETE
http://localhost:8090/shopapi/shops/{shopId}

7 Search shop at given longitude and latitude
GET
http://localhost:8090/shopapi/search?longitude=<longitude >&latitude=<latitude>

8 Find nearest shop to given address
GET
http://localhost:8090/shopapi/searchNearest?street=<>&city=<>&country=<>&state=<>&dist=<>&pin=<>
