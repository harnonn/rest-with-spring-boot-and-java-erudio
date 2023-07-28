package ca.com.arnon.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.com.arnon.data.vo.PersonVO;
import ca.com.arnon.exeptions.ResourceNotFoundException;
import ca.com.arnon.mapper.DozerMapper;
import ca.com.arnon.model.Person;
import ca.com.arnon.repositories.PersonRepository;

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public PersonServices() {
		
	}
	
	public PersonVO findById(Long id) {
		logger.info("Finding one person!");
		var entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		return DozerMapper.parseObject(entity, PersonVO.class);
	}
	
	public List<PersonVO> findAll() {
		logger.info("Finding all the persons!");
		return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
	}

	public PersonVO create(PersonVO personVo) {
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(personVo, Person.class);
		repository.save(entity);
		return DozerMapper.parseObject(entity, PersonVO.class);
	}
	
	public PersonVO update(PersonVO person) {
		logger.info("Updating one person!");
		Person entity  = repository.findById(person.getId()).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		repository.save(entity);
		return DozerMapper.parseObject(entity, PersonVO.class);
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		Person entity  = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		repository.delete(entity);
	}
}