package ca.com.arnon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.com.arnon.model.Person;

public interface PersonRepository extends JpaRepository<Person,Long>{
}
