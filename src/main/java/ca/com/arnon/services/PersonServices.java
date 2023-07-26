package ca.com.arnon.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import ca.com.arnon.model.Person;

@Service
public class PersonServices {
	
	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	public PersonServices() {
		
	}
	
	public Person findById(String id) {
		logger.info("Finding one person!");

		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Leandro");
		person.setLastName("Costa");
		person.setAddress("Uberlandia - MG - Brasil");
		person.setGender("Male");
		
		return person;
	}
	
	public List<Person> findAll() {
		logger.info("Finding all the persons!");
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < 8; i++) {
			Person person = mockPerson(i);
			persons.add(person);
		}
		return persons;
	}

	public Person create(Person person) {
		logger.info("Creating one person!");
		
		return person;
	}
	
	public Person update(Person person) {
		logger.info("Updating one person!");
		
		return person;
	}
	
	public void delete(String id) {
		logger.info("Deleting one person!");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Person mockPerson(int i) {
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Name " + i);
		person.setLastName("Lastname " + i);
		person.setAddress("Address: " + i);
		person.setGender("Gender" + i);
		return person;
	}

}
