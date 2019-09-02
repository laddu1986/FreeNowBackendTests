package com.freenow.api.utils;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

/*
 * This is class contains all the restAssured related getter/setter methods
 * @author Kushal Bhalaik
 */
public class RestTemplate {
	private static RestTemplate apiUtilsInstance = null;
	private RequestSpecification httpRequest;

	private RestTemplate() {
		httpRequest = RestAssured.given()
				.config(RestAssured.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON)));
	}

	public static RestTemplate getInstance() {

		if (apiUtilsInstance == null)
			apiUtilsInstance = new RestTemplate();

		return apiUtilsInstance;
	}

	public void resetRestAssured() {
		apiUtilsInstance = null;
	}

	// Sets Base URI
	public void setBaseURI(String baseURI) {
		httpRequest.baseUri(baseURI);
	}

	// Sets base path
	public void setBasePath(String basePathTerm) {
		httpRequest.basePath(basePathTerm);
	}

	// Reset Base URI (after test)
	public void resetBaseURI() {
		httpRequest.basePath("");
	}

	// Reset base path
	public void resetBasePath() {
		httpRequest.basePath("");
	}

	// Sets relaxedHTTPSValidation
	public void relaxedHTTPSValidation() {
		httpRequest.relaxedHTTPSValidation();
	}

	// Sets ContentType
	public void setContentType(ContentType Type) {
		httpRequest.contentType(Type);
	}

	// Sets RequestBody using String
	public void setRequestBody(String body) {
		httpRequest.body(body);
	}

	// Sets RequestBody using JsonPath
	public void setRequestBody(JsonPath requestBodyArray) {
		httpRequest.body(requestBodyArray);
	}

	// Sets query params
	public void setRequestQueryParams(Map<String, String> params) {
		httpRequest.queryParams(params);
	}

	// Sets query params
	public void setRequestPathParams(String param, String paramValue) {
		httpRequest.pathParams(param, paramValue);
	}

	// resets query params
	public void resetRequestQueryParams() {
		httpRequest.queryParams(new HashMap<String, String>());
	}

	// Sets RequestHeader
	public void setRequestHeader(Map<String, String> headers) {
		httpRequest.headers(headers);
	}

	// Gets PostRequestResponse
	public Response getPOSTResponse() {
		return httpRequest.post();
	}

	// Returns POST response by given path
	public Response getPOSTResponsebyPath(String path) {
		return httpRequest.post(path);
	}

	// Returns response by given path
	public Response getResponsebyPath(String path) {
		return httpRequest.get(path);
	}

	// Returns response
	public Response getResponse() {
		return httpRequest.get();
	}

	// Returns JsonPath object
	public JsonPath getJsonPath(Response res) {
		String json = res.asString();
		return new JsonPath(json);
	}
}