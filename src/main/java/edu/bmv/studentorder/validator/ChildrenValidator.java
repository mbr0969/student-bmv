package edu.bmv.studentorder.validator;

import edu.bmv.studentorder.domain.children.AnswerChildren;
import edu.bmv.studentorder.domain.StudentOrder;

public class ChildrenValidator {
     public AnswerChildren checkChildren(StudentOrder so){
        System.out.println("Children running!");
        return new AnswerChildren();
    }
}
