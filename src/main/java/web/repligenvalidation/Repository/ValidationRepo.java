package web.repligenvalidation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.repligenvalidation.Entities.ExceptionEntity;

public interface ValidationRepo extends JpaRepository<ExceptionEntity, Integer> {

}
