package ca.com.arnon.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import ca.com.arnon.controller.PersonController;
import ca.com.arnon.data.vo.PersonVO;
import ca.com.arnon.exeptions.RequiredObjectIsNullException;
import ca.com.arnon.exeptions.ResourceNotFoundException;
import ca.com.arnon.mapper.DozerMapper;
import ca.com.arnon.model.Person;
import ca.com.arnon.repositories.PersonRepository;
import jakarta.transaction.Transactional;

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
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public List<PersonVO> findAll() {
		logger.info("Finding all the persons!");
		var personsVo = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		personsVo.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return personsVo;
	}

	public PersonVO create(PersonVO personVo) {
		if (personVo == null) throw new RequiredObjectIsNullException();
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(personVo, Person.class);
		repository.save(entity);
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		if (person == null) throw new RequiredObjectIsNullException();
		logger.info("Updating one person!");
		Person entity  = repository.findById(person.getKey()).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		repository.save(entity);
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	@Transactional
	public PersonVO disablePerson(Long id) {
		logger.info("Disabling one person!");
		repository.disablePerson(id);
		var entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		Person entity  = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No records fond for this id"));
		repository.delete(entity);
	}
}