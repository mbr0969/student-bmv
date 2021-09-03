package edu.bmv.studentorder.dao;

import edu.bmv.studentorder.domain.StudentOrder;
import edu.bmv.studentorder.exception.DaoException;

import java.util.List;

public interface IStudentOrderDao {

    Long saveStudentOrder(StudentOrder studentOrder) throws DaoException;

    List<StudentOrder> getStudentOrders() throws DaoException;
}
