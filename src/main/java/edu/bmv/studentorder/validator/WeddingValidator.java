package edu.bmv.studentorder.validator;

import edu.bmv.studentorder.domain.wedding.AnswerWedding;
import edu.bmv.studentorder.domain.StudentOrder;

public class WeddingValidator {

   public AnswerWedding checkWedding(StudentOrder so) {
        System.out.println("Wedding running!");
        AnswerWedding aw = new AnswerWedding();
        aw.success = false;
        return aw;
    }
}
