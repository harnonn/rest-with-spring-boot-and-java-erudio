package ca.com.arnon.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.com.arnon.model.Person;
import ca.com.arnon.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	//private PersonServices personServices = new PersonServices();
	@Autowired
	private PersonServices personServices;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Person findById(@PathVariable(value = "id") String id) {
		return personServices.findById(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Person> findAll() {
		return personServices.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Person create(@RequestBody Person person) {
		return personServices.create(person);
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Person update(@RequestBody Person person) {
		return personServices.update(person);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable(value = "id") String id) {
		personServices.delete(id);
	}
	
}