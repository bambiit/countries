package com.assignment.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApplicationTestUtils {

    public static RequestSpecification getNewRequest() {
        return RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

}
