package edu.bmv.studentorder.validator.register;

import edu.bmv.studentorder.domain.register.CityRegisterResponse;
import edu.bmv.studentorder.domain.Person;
import edu.bmv.studentorder.exception.CityRegisterException;
import edu.bmv.studentorder.exception.TransportException;

public interface ICityRegisterChecker {
    public CityRegisterResponse checkPerson(Person Person) throws CityRegisterException, TransportException;
}
