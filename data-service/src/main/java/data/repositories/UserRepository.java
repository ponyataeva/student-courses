package data.repositories;

import data.dto.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Add class description
 */
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
}
