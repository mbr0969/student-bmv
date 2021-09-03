package edu.bmv.studentorder;

import edu.bmv.studentorder.dao.StudentOrderDaoImpl;
import edu.bmv.studentorder.domain.*;
import edu.bmv.studentorder.domain.children.AnswerChildren;
import edu.bmv.studentorder.domain.register.AnswerCityRegister;
import edu.bmv.studentorder.domain.student.AnswerStudent;
import edu.bmv.studentorder.domain.wedding.AnswerWedding;
import edu.bmv.studentorder.exception.CityRegisterException;
import edu.bmv.studentorder.exception.DaoException;
import edu.bmv.studentorder.mail.MailSender;
import edu.bmv.studentorder.validator.ChildrenValidator;
import edu.bmv.studentorder.validator.CityRegisterValidator;
import edu.bmv.studentorder.validator.StudentValidator;
import edu.bmv.studentorder.validator.WeddingValidator;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.math.*;

public class StudentOrderValidator {

    private CityRegisterValidator cityRegisterVal;
    private WeddingValidator weddingVal;
    private ChildrenValidator childrenVal;
    private StudentValidator studentVal;
    private MailSender mailSender;

    public StudentOrderValidator(){
        cityRegisterVal = new CityRegisterValidator();
        weddingVal = new WeddingValidator();
        childrenVal = new ChildrenValidator();
        studentVal = new StudentValidator();
        mailSender = new MailSender();
    }

    public static void main(String[] args) throws CityRegisterException, DaoException {
          StudentOrderValidator sov = new StudentOrderValidator();
          sov.checkAll();
    }

    public void checkAll() throws CityRegisterException,DaoException {
        List<StudentOrder> soList = readStudentOrders();
        for (StudentOrder so : soList) {
            System.out.println();
            checkOneStudentOrder(so);
        }
    }

    public List<StudentOrder> readStudentOrders() throws DaoException {
        return new StudentOrderDaoImpl().getStudentOrders();
    }

    public void checkOneStudentOrder(StudentOrder so) throws CityRegisterException {
        AnswerCityRegister as =  checkCityRegister(so);
        //AnswerWedding aw = checkWedding(so);
        //AnswerChildren ac = checkChildren(so) ;
        //AnswerStudent astd = checkStudent(so);
        //sendMail(so);
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) throws CityRegisterException {
        return  cityRegisterVal.checkCityRegister(so);
    }

    public AnswerWedding checkWedding(StudentOrder so){
        return weddingVal.checkWedding(so);
    }

    public AnswerChildren checkChildren(StudentOrder so){
        return childrenVal.checkChildren(so);
    }

    public AnswerStudent checkStudent(StudentOrder so){
        return studentVal.checkStudent(so);
    }

    public void sendMail(StudentOrder so){
        mailSender.sendMail(so);
    }
}
