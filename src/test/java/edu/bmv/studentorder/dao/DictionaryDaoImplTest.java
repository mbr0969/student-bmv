package edu.bmv.studentorder.dao;

import edu.bmv.studentorder.domain.CountryArea;
import edu.bmv.studentorder.domain.PassportOffice;
import edu.bmv.studentorder.domain.RegisterOffice;
import edu.bmv.studentorder.domain.Street;
import edu.bmv.studentorder.exception.DaoException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DictionaryDaoImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryDaoImplTest.class);

    @BeforeClass
    public static void startUp() throws Exception {
        DBInit.startUp();
    }

    @Test
    public void testStreet() throws DaoException {
        LocalDateTime dt = LocalDateTime.now();
        LOGGER.info("Test street {} ", dt);
        List<Street> streets  = new DictionaryDaoImpl().findStreets("про");
        assertEquals(2, streets.size());
    }

    @Test
    public void testPassportOffice() throws DaoException{
        List<PassportOffice> po = new DictionaryDaoImpl().findPassportOffice("010020000000");
        assertEquals(2, po.size());
    }

    @Test
    public void testRegisterOffice() throws DaoException {
        List<RegisterOffice> ro = new DictionaryDaoImpl().findRegisterOffice("010010000000");
        assertEquals(2, ro.size());
    }

    @Test
    public void testCountryArea() throws DaoException{
        List<CountryArea> ca1 = new DictionaryDaoImpl().findAreas("");
        List<CountryArea> ca2 = new DictionaryDaoImpl().findAreas("020000000000");
        List<CountryArea> ca3 = new DictionaryDaoImpl().findAreas("020010000000");
        List<CountryArea> ca4 = new DictionaryDaoImpl().findAreas("020010010000");

        assertEquals(2, ca1.size());
        assertEquals(2, ca2.size());
        assertEquals(2, ca3.size());
        assertEquals(2, ca4.size());
    }

}