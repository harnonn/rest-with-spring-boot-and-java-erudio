package ca.com.arnon.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ca.com.arnon.configs.TestConfigs;
import ca.com.arnon.integrationtests.controller.withyaml.mapper.YamlMapper;
import ca.com.arnon.integrationtests.testcontainers.AbstractIntegrationTest;
import ca.com.arnon.integrationtests.vo.AccountCredentialsVO;
import ca.com.arnon.integrationtests.vo.PersonVO;
import ca.com.arnon.integrationtests.vo.TokenVO;
import ca.com.arnon.integrationtests.vo.pagedModels.PagedModelPerson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest{
	
	private static RequestSpecification specification;
	private static YamlMapper objectMapper;
	private static PersonVO person;
	private static String CONTENT_TYPE = TestConfigs.CONTENT_TYPE_YML;
	
	private void mockPerson() {
		person.setFirstName("Richard");
		person.setLastName("Stallman");
		person.setAddress("New You City, NY - USA");
		person.setGender("Male");
		person.setEnabled(true);
	}
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YamlMapper();
//		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		person = new PersonVO();
		
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		var accessToken = given()
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.body(user, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class, objectMapper)
				.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer "+accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		var content = given()
				.spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.body(person, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, objectMapper);
		
		
		PersonVO persistedPerson = content;
		person = persistedPerson;
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman", persistedPerson.getLastName());
		assertEquals("New You City, NY - USA", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());
	}
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		person.setLastName("Stallman-2");
		
		var content = given()
				.spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.body(person, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.as(PersonVO.class, objectMapper);
		
		PersonVO persistedPerson = content;
		person = persistedPerson;
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertEquals(person.getId(),persistedPerson.getId());
		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman-2", persistedPerson.getLastName());
		assertEquals("New You City, NY - USA", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());
	}
	
	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		var content = given()
				.spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
				.statusCode(200)
				.extract()
				.as(PersonVO.class, objectMapper);
		
		PersonVO persistedPerson = content;
		person = persistedPerson;
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman-2", persistedPerson.getLastName());
		assertEquals("New You City, NY - USA", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());
	}

	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		var content = given()
				.spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", person.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.as(PersonVO.class, objectMapper);
		
		PersonVO persistedPerson = content;
		person = persistedPerson;
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman-2", persistedPerson.getLastName());
		assertEquals("New You City, NY - USA", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());
	}
	
	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given()
			.spec(specification)
			.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(CONTENT_TYPE)
			.accept(CONTENT_TYPE)
			.pathParam("id", person.getId())
			.when()
			.delete("{id}")
			.then()
			.statusCode(204);

	}
	
	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given()
				.spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.queryParams("page", 3, "limit", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, objectMapper);
		
		var people = content.getContent();
		
		PersonVO foundPerson1 = people.get(0);
		assertNotNull(foundPerson1.getId());
		assertNotNull(foundPerson1.getFirstName());
		assertNotNull(foundPerson1.getLastName());
		assertNotNull(foundPerson1.getAddress());
		assertNotNull(foundPerson1.getGender());
		assertEquals(837, foundPerson1.getId());
		assertEquals("Alexis", foundPerson1.getFirstName());
		assertEquals("Mullally", foundPerson1.getLastName());
		assertEquals("0098 Rigney Center", foundPerson1.getAddress());
		assertEquals("Male", foundPerson1.getGender());
		assertTrue(foundPerson1.getEnabled());
		
		PersonVO foundPerson6 = people.get(5);
		assertNotNull(foundPerson6.getId());
		assertNotNull(foundPerson6.getFirstName());
		assertNotNull(foundPerson6.getLastName());
		assertNotNull(foundPerson6.getAddress());
		assertNotNull(foundPerson6.getGender());
		assertEquals(713, foundPerson6.getId());
		assertEquals("Alla", foundPerson6.getFirstName());
		assertEquals("Astall", foundPerson6.getLastName());
		assertEquals("72525 Emmet Alley", foundPerson6.getAddress());
		assertEquals("Female", foundPerson6.getGender());
		assertFalse(foundPerson6.getEnabled());
	}
	
	@Test
	@Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		var content = given()
				.spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.pathParam("firstName", "rno")
				.queryParams("page", 0, "limit", 10, "direction", "asc")
				.when()
				.get("findPersonsByName/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, objectMapper);
		
		var people = content.getContent();
		
		PersonVO foundPerson1 = people.get(0);
		assertNotNull(foundPerson1.getId());
		assertNotNull(foundPerson1.getFirstName());
		assertNotNull(foundPerson1.getLastName());
		assertNotNull(foundPerson1.getAddress());
		assertNotNull(foundPerson1.getGender());
		assertEquals(1, foundPerson1.getId());
		assertEquals("Arnon", foundPerson1.getFirstName());
		assertEquals("Lehmann", foundPerson1.getLastName());
		assertEquals("Quebec", foundPerson1.getAddress());
		assertEquals("Male", foundPerson1.getGender());
		assertTrue(foundPerson1.getEnabled());
	}
	
	@Test
	@Order(8)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given()
			.spec(specificationWithoutToken)
			.contentType(CONTENT_TYPE)
			.accept(CONTENT_TYPE)
			.when()
			.get()
			.then()
			.statusCode(400);
	}
	
	@Test
	@Order(9)
	public void testHATEOS() throws JsonMappingException, JsonProcessingException {
		
		var contentRaw = given()
				.spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(CONTENT_TYPE)
				.accept(CONTENT_TYPE)
				.queryParams("page", 3, "limit", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		String content = contentRaw.replace("\n", "").replace("\r", "");
		assertTrue(content.contains("links:  - rel: \"self\"    href: \"http://localhost:8888/api/person/v1/837\""));
		assertTrue(content.contains("links:  - rel: \"self\"    href: \"http://localhost:8888/api/person/v1/198\""));
		assertTrue(content.contains("links:  - rel: \"self\"    href: \"http://localhost:8888/api/person/v1/796\""));
		assertTrue(content.contains("- rel: \"first\"  href: \"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=0&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("- rel: \"prev\"  href: \"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=2&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("- rel: \"self\"  href: \"http://localhost:8888/api/person/v1?page=3&limit=10&direction=asc\""));
		assertTrue(content.contains("- rel: \"next\"  href: \"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=4&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("- rel: \"last\"  href: \"http://localhost:8888/api/person/v1?limit=10&direction=asc&page=100&size=10&sort=firstName,asc\""));
//		assertTrue(content.contains("page:  size: 10  totalElements: 1007  totalPages: 101  number: 3"));
	}
}