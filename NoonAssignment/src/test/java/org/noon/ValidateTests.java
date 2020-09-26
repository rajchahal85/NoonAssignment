package org.noon;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ValidateTests  extends BaseClass {
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://interview.noonpay.biz";
    }

    @Test(priority = 2)
    public void validateError()
    {
        RequestSpecification request = given().
                                            contentType(ContentType.JSON).
                                            body("{\n" + "    \"email\": \"test@gmail.com\",\n" + "    \"password\": \"wrong\"\n" + "}");

        Response response = request.when().
                                        post("/user/validate").
                                    then().
                                        statusCode(200).
                                        extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        boolean success =  jsonPath.get("success");
        String errorMessage = jsonPath.get("errorMessage");

        Assert.assertEquals(success, false);
        Assert.assertEquals(errorMessage, "Invalid Credentials.");
    }

    @Test(priority = 1)
    public void validateSuccess()
    {
        RequestSpecification request = given().
                contentType(ContentType.JSON).
                body("{\n" + "    \"email\": \"test@gmail.com\",\n" + "    \"password\": \"Qwerty123\"\n" + "}");

        Response response = request.when().
                                        post("/user/validate").
                                    then().
                                        statusCode(200).
                                        extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        boolean success =  jsonPath.get("success");
        String errorMessage = jsonPath.get("errorMessage");

        token = jsonPath.get("data.token");

        Assert.assertEquals(success, true);
        Assert.assertEquals(errorMessage, null);
    }
}
