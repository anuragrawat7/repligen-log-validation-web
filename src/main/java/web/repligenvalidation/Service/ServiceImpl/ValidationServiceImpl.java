package web.repligenvalidation.Service.ServiceImpl;

import org.springframework.stereotype.Service;
import web.repligenvalidation.Entities.ExceptionEntity;
import web.repligenvalidation.Repository.ValidationRepo;
import web.repligenvalidation.Service.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {

    private ValidationRepo validationRepo;

    public ValidationServiceImpl(ValidationRepo validationRepo) {
        super();
        this.validationRepo = validationRepo;
    }

    @Override
    public void addExceptionLog(ExceptionEntity exceptionEntity) {
        validationRepo.save(exceptionEntity);
    }

}
