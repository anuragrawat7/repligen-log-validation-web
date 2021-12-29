package web.repligenvalidation.Service;

import web.repligenvalidation.Entities.ExceptionEntity;

import java.io.IOException;

public interface ValidationService {

    void addExceptionLog(ExceptionEntity exceptionEntity) throws IOException;

}

