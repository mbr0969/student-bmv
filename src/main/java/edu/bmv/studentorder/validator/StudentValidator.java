package edu.bmv.studentorder.validator;

import edu.bmv.studentorder.domain.student.AnswerStudent;
import edu.bmv.studentorder.domain.StudentOrder;

public class StudentValidator {

     public AnswerStudent checkStudent(StudentOrder so){
        System.out.println("Students running!");
        return new AnswerStudent();
    }
}
