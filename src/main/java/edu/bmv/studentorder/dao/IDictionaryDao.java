package edu.bmv.studentorder.dao;

import edu.bmv.studentorder.domain.CountryArea;
import edu.bmv.studentorder.domain.PassportOffice;
import edu.bmv.studentorder.domain.RegisterOffice;
import edu.bmv.studentorder.domain.Street;
import edu.bmv.studentorder.exception.DaoException;

import java.util.List;

public interface IDictionaryDao {

    List<Street> findStreets(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffice(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException;
    List<CountryArea> findAreas(String areaId) throws DaoException;
}
