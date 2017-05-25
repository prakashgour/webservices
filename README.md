# Webservices

#ShopsAPI

Please read below instructions to use API.

##Deploy the ShopsAPI.war in the Application server of host and then use the localhost and 8090 in the below given URI and test the all the operation in postman.

##To create a shop follow below steps.
URI: http://localhost:8090/shopapi/shops
Method: POST
Headers: 
1. content-type=application\json
Body:
Select raw type and use below json format(example).

{
  "shopName": "Agrawal Sweets",
  "shopStreet": "MG Road",
  "shopCity": "Indore",
  "shopDistrict": "Indore",
  "shopState": "Madhya Pradesh",
  "shopCountry": "India",
  "shopPincode": "452001"
  }
#Test custom exception 
If you do not provide all the details (i.e. remove any pair from above example) REST will respond custom exception.

##To update the address of a shop follow below steps. 

URI: http://localhost:8090/shopapi/shops
Method: POST
Headers: 
1. content-type=application\json
Body:
Select raw type and use below json format and provide updated data (example).

{
  "shopName": "Agrawal Sweets",
  "shopStreet": "RNT Marg",
  "shopCity": "Indore",
  "shopDistrict": "Indore",
  "shopState": "Madhya Pradesh",
  "shopCountry": "India",
  "shopPincode": "452001"
  }

##To update the shop address with shop Id (When there is no change in address REST respond you with shop ID and Lat Long)
If Shop name already exist it will return shop ID , then pass the shop ID in uri and new address in json as below,

URI: http://localhost:8090/shopapi/shops/<shopId>
Method: PUT
Headers: 
1. content-type=application\json
Body:
Select raw type and use below json format (example id=2).

URI: http://localhost:8090/shopapi/shops/2
{
  "shopName": "Agrawal Sweets",
  "shopStreet": "Pune ",
  "shopCity": "Pune",
  "shopDistrict": "Pune",
  "shopState": "Maharastra",
  "shopCountry": "India",
  "shopPincode": "411057"
  }


## Find nearest shop from current location of user.
Method :GET
URI : http://localhost:8090/shopapi/searchNearest?street=<>&city=<>&country=<>&state=<>&dist=<>&pin=<>

##Get all shops from DB.
Method:GET
URI: http://localhost:8090/shopapi/shops

#URIs for other services.


##Get Shop by Shop Id
GET
http://localhost:8090/shopapi/shops/{shopId}


##Search shop at given longitude and latitude
Method :GET
http://localhost:8090/shopapi/search?longitude=<longitude >&latitude=<latitude>


