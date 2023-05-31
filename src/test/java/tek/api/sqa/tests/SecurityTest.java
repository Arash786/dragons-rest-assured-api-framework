package tek.api.sqa.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import tek.api.sqa.base.APITestConfig;
import tek.api.utility.EndPoints;

public class SecurityTest extends APITestConfig {
	
	@Test
	public void testGenerateTokenValidUser() {
		//First step to set Base URL done at BeforeMethod Annotation
		//2) prepare Request.
		//Create Request Body.
		//Option 1) Creating a Map.
		//Option 2) Create and Encapsulated Object.
		// Option 3) String as JSON Object. (Not Recommended)
		
		Map<String, String> tokenRequestBody = new HashMap<>();
		tokenRequestBody.put("username", "supervisor");
		tokenRequestBody.put("password", "tek_supervisor");
		//Given Prepare Request.
		RequestSpecification request = RestAssured.given().body(tokenRequestBody);
		//Set Content Type.
		request.contentType(ContentType.JSON);
		//When sending request to end-point
		Response response = request.when().post (EndPoints.TOKEN_GENERATION.getValue());
		//Optional to print response in console.
		response.prettyPrint();
		Assert.assertEquals(response.getStatusCode(), 200);
		//Assert token is not null.
		String generatedToken = response.jsonPath().get("token");
		Assert.assertNotNull(generatedToken);
		
	}
		
	//Create Test to generate token with Invalid Username.
	//Assert errorMessage "User not found"
		
		
	@Test(dataProvider = "invalidTokenData")
	
	public void tokenWithInvalidDataTest(String username, String password, String expectedErrorMessage) {
	
	Map<String, String> requestBody = new HashMap<>();
	requestBody.put("username", "WrongUserName");
	requestBody.put("password", "tek_supervisor");
	//Given Prepare Request.
	RequestSpecification request = RestAssured.given().body(requestBody);
	//Set Content Type.
	request.contentType(ContentType.JSON);
	//When sending request to end-point
	Response response = request.when().post (EndPoints.TOKEN_GENERATION.getValue());
	//Optional to print response in console.
	response.prettyPrint();
	Assert.assertEquals(response.getStatusCode(), 400);
	String errorMessage = response.jsonPath().get("errorMessage");
	Assert.assertEquals(errorMessage,"User not found");
	
	
	
	}
		@DataProvider(name = "invalidTokenData")
		private Object[][] invalidTokenData(){
			Object[][] data = {
					{"WrongUser" , "tek_supervisor" , "User not found"},
					{"supervisor", "WrongPassword" , "Password Not Matched"}
					
			};
			
			return data;
			
			}
		}

