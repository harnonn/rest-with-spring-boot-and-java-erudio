package ca.com.arnon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.com.arnon.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
	
	@Query("SELECT u FROM User u where u.userName =:userName")
	User findByUserName(@Param("userName") String userName);
	
}
