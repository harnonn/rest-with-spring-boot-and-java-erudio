package ca.com.arnon.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.com.arnon.exeptions.ResourceNotFoundException;
import ca.com.arnon.model.Person;
import ca.com.arnon.repositories.PersonRepository;

@Service
public class PersonServices {
	
	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public PersonServices() {
		
	}
	
	public Person findById(Long id) {
		logger.info("Finding one person!");
		return repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
	}
	
	public List<Person> findAll() {
		logger.info("Finding all the persons!");
		return repository.findAll();
	}

	public Person create(Person person) {
		logger.info("Creating one person!");
		return repository.save(person);
	}
	
	public Person update(Person person) {
		logger.info("Updating one person!");
		
		Person entity  = repository.findById(person.getId()).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		return repository.save(entity);
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		Person entity  = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		repository.delete(entity);
	}
}