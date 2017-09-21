package data.repositories;

import data.dto.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Repository for work with Course entity
 */
@RepositoryRestResource
public interface CourseRepository extends CrudRepository<Course, Long> {
}
