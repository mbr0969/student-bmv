package edu.bmv.studentorder.validator;

import edu.bmv.studentorder.domain.Child;
import edu.bmv.studentorder.domain.Person;
import edu.bmv.studentorder.domain.register.AnswerCityRegister;
import edu.bmv.studentorder.domain.register.AnswerCityRegisterItem;
import edu.bmv.studentorder.domain.register.CityRegisterResponse;
import edu.bmv.studentorder.domain.StudentOrder;
import edu.bmv.studentorder.exception.CityRegisterException;
import edu.bmv.studentorder.exception.TransportException;
import edu.bmv.studentorder.validator.register.ICityRegisterChecker;
import edu.bmv.studentorder.validator.register.RealCityRegisterChecker;

public class CityRegisterValidator {
    public static final  String IN_CODE = "NO GRN";


    private ICityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new RealCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so)  {
        AnswerCityRegister ans = new AnswerCityRegister();
        ans.addItems(checkPerson(so.getHusband()));
        ans.addItems( checkPerson(so.getWife()));
         for (Child child : so.getChildren()){
              ans.addItems(checkPerson(child));
         }
         return ans;
    }

    private AnswerCityRegisterItem  checkPerson(Person person){

        AnswerCityRegisterItem.CityStatus status;
        AnswerCityRegisterItem.CityError error = null;

        try{
            CityRegisterResponse tmp = personChecker.checkPerson(person);
            status = tmp.isRegistered() ?  AnswerCityRegisterItem.CityStatus.YES : AnswerCityRegisterItem.CityStatus.NO;
        }catch (CityRegisterException ex){
            error = new AnswerCityRegisterItem.CityError(ex.getCode(), ex.getMessage());
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            ex.printStackTrace(System.out);
        }catch (TransportException ex){
            ex.printStackTrace(System.out);
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());
            status = AnswerCityRegisterItem.CityStatus.ERROR;
        }catch (Exception ex){
            ex.printStackTrace(System.out);
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());
            status = AnswerCityRegisterItem.CityStatus.ERROR;
        }

        AnswerCityRegisterItem ans = new AnswerCityRegisterItem(person, status, error);

        return  ans;
    }

}
