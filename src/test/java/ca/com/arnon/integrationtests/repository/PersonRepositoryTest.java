package ca.com.arnon.integrationtests.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ca.com.arnon.integrationtests.testcontainers.AbstractIntegrationTest;
import ca.com.arnon.model.Person;
import ca.com.arnon.repositories.PersonRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{

	@Autowired
	public PersonRepository repository;
	private static Person person;
	
	@BeforeAll
	public static void setUp() {
		person = new Person();
	}
	
	@Test
	@Order(0)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "firstName"));
		person = repository.findPersonsByName("rno", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertEquals(1, person.getId());
		assertEquals("Arnon", person.getFirstName());
		assertEquals("Lehmann", person.getLastName());
		assertEquals("Quebec", person.getAddress());
		assertEquals("Male", person.getGender());
		assertTrue(person.getEnabled());
	}
	
	@Test
	@Order(1)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
		
		repository.disablePerson(person.getId());
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "firstName"));
		person = repository.findPersonsByName("rno", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertEquals(1, person.getId());
		assertEquals("Arnon", person.getFirstName());
		assertEquals("Lehmann", person.getLastName());
		assertEquals("Quebec", person.getAddress());
		assertEquals("Male", person.getGender());
		assertFalse(person.getEnabled());
	}
	
}
