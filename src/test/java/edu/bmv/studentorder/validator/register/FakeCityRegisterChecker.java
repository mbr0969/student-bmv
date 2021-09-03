package edu.bmv.studentorder.validator.register;

import edu.bmv.studentorder.domain.Adult;
import edu.bmv.studentorder.domain.Child;
import edu.bmv.studentorder.domain.register.CityRegisterResponse;
import edu.bmv.studentorder.domain.Person;
import edu.bmv.studentorder.exception.CityRegisterException;
import edu.bmv.studentorder.exception.TransportException;

public class FakeCityRegisterChecker  implements ICityRegisterChecker {

    public static final String GOOD_1 = "1000";
    public static final String GOOD_2 = "2000";
    public static final String BAD_1 = "1001";
    public static final String BAD_2 = "2001";
    public static final String ERROR_1 = "1002";
    public static final String ERROR_2 = "2002";
    public static final String ERROR_T_1 = "1003";
    public static final String ERROR_T_2 = "2003";

    public CityRegisterResponse checkPerson(Person Person) throws CityRegisterException, TransportException {
        CityRegisterResponse res = new CityRegisterResponse();
        if(Person instanceof Adult){

            Adult t = (Adult) Person;
            var ps = t.getPassportSeria();


            if(t.getPassportSeria().equals(GOOD_1) || t.getPassportSeria().equals(GOOD_2)){
                res.setRegistered(true);
                res.setTemporary(false);
            }

            if(t.getPassportSeria().equals(BAD_1) || t.getPassportSeria().equals(BAD_2)){
                res.setRegistered(false);
            }

            if(t.getPassportSeria().equals(ERROR_1) || t.getPassportSeria().equals(ERROR_2)){
                CityRegisterException ex = new CityRegisterException("1", "GRN ERROR " + t.getPassportSeria());
                throw ex;
            }

            if(t.getPassportSeria().equals(ERROR_T_1) || t.getPassportSeria().equals(ERROR_T_2)){
                TransportException ex = new TransportException( "Transport ERROR " + t.getPassportSeria());
                throw ex;
            }
        }

        if(Person instanceof Child){
            res.setRegistered(true);
            res.setTemporary(true);
        }

        System.out.println(res);
        return res;
    }
}
