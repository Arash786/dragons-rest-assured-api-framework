package tek.api.sqa.tests;

	import java.sql.ResultSet;
	import java.sql.SQLException;

	import org.testng.Assert;
	import org.testng.annotations.Test;

	import io.restassured.RestAssured;
	import io.restassured.response.Response;
	import io.restassured.specification.RequestSpecification;
	import tek.api.sqa.base.DatabaseConfig;
	import tek.api.utility.EndPoints;

	public class GetAccountWithDB extends DatabaseConfig{

		@Test
		public void getAccountTestWithDBQuery() throws SQLException {
			String query = "select id from primary_person order by id desc limit 1";
			ResultSet resultSet = runQuery(query);
			long queryResultId = 0; 
			while(resultSet.next()) {
				queryResultId = resultSet.getLong("id");
			}
			String validToken = getValidToken(); 
			RequestSpecification request = RestAssured.given();
			request.header("Authorization", "Bearer " + validToken);
			request.queryParam("primaryPersonId", queryResultId);
			
			Response response = request.when().get(EndPoints.GET_ACCOUNT.getValue());
			Assert.assertEquals(response.getStatusCode(), 200);
			int actualPrimaryPersonId = response.jsonPath().get("primaryPerson.id");
			Assert.assertEquals(actualPrimaryPersonId, queryResultId);
		}
	}


