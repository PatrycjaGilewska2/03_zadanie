package pl.jstk.repository;


import java.util.List;

import pl.jstk.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query("select user from UserEntity user where upper(user.userName) like concat(upper(:name), '%')")
	List<UserEntity> findUsersByName(@Param("name") String name);

}
