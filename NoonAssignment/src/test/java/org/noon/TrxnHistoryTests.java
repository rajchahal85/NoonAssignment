package org.noon;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class TrxnHistoryTests extends BaseClass {
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://interview.noonpay.biz";
    }

    @Test(priority = 1)
    public void trxnHistorySuccess()
    {
        RequestSpecification request = given().
                                            contentType(ContentType.JSON).
                                            queryParam("token", token);

        Response response = request.when().
                                        get("user/txn/history").
                                    then().
                                        statusCode(200).
                                        extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        Object trxnObject = jsonPath.getJsonObject("data.txn");

        Assert.assertTrue(trxnObject instanceof ArrayList);
    }

    @Test(priority = 2)
    public void trxnHistoryError()
    {
        RequestSpecification request = given().
                                            contentType(ContentType.JSON).
                                            queryParam("token", "wrongtoken");

        Response response = request.when().
                                        get("user/txn/history").
                                    then().
                                        statusCode(200).
                                        extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        String errorMessage = jsonPath.getJsonObject("errorMessage");

        Assert.assertEquals(errorMessage, "Invalid Token.");
    }

    @Test(priority = 3)
    public void trxnHistoryErrorStatus404Test()
    {
        RequestSpecification request = given().
                log().all().
                contentType(ContentType.JSON).
                queryParam("token", token);

        Response response = request.when().
                get("user/txn/historyy").
                then().
                statusCode(404).log().all().
                extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        String errorMessage = jsonPath.getJsonObject("message");

        Assert.assertEquals(errorMessage, "No message available");
    }
}
