package edu.bmv.studentorder;

import edu.bmv.studentorder.dao.IStudentOrderDao;
import edu.bmv.studentorder.dao.StudentOrderDaoImpl;
import edu.bmv.studentorder.domain.*;

import java.time.LocalDate;
import java.util.List;

public class SaveStudentOrder {

    public static void main(String[] args) throws Exception{

        StudentOrder so = buildStudentOrder(10);
        IStudentOrderDao dao = new StudentOrderDaoImpl();
        Long id = dao.saveStudentOrder(so);
        System.out.println("Order Id : " + id );

        List<StudentOrder> soList = dao.getStudentOrders();

        for (StudentOrder s : soList){
            System.out.println();
            System.out.println("Order Id : " + s.getStudentOrderId());
            System.out.println("Husband FIO "  +s.getHusband().getSurName() + " "+ s.getHusband().getGivenName() + " " + s.getHusband().getPatronymicName());
            System.out.println("Husband student number : "  + s.getHusband().getStudentId());
            System.out.println("Husband university Id: "  + s.getHusband().getUniversity().getUniversityId() + ", university name: " +s.getHusband().getUniversity().getUniversityName());
            System.out.println("Husband passport office " + s.getHusband().getIssueDepartment().getOfficeName());
            System.out.println();
            System.out.println("Wife FIO "  +s.getWife().getSurName() + " "+ s.getWife().getGivenName() + " " + s.getWife().getPatronymicName());
            System.out.println("Wife student number : "  + s.getWife().getStudentId());
            System.out.println("Wife university Id: "  + s.getWife().getUniversity().getUniversityId() + ", university name: " +s.getWife().getUniversity().getUniversityName());
            System.out.println("Wife passport office " + s.getWife().getIssueDepartment().getOfficeName());
            System.out.println("Marriage Date : " + s.getMarriageDate());
            System.out.println("Certificate Id : " + s.getMarriageCertificateId());
        }

       // Class.forName("org.postgresql.Driver");
        //buildStudentOrder(10);
    }

//    static long saveStudentOrder(StudentOrder studentOrder){
//        long answer = 1999;
//        System.out.println("Save student order: " );
//        return answer;
//    }


    public static StudentOrder buildStudentOrder(long id){
        StudentOrder so = new StudentOrder();
        so.setStudentOrderId(id);
        so.setMarriageCertificateId("" + (123456000 + id));
        so.setMarriageDate(LocalDate.of(2016, 7, 4));
        RegisterOffice ro = new RegisterOffice(1L,"","");
        so.setMarriageOffice(ro);
        Street street = new Street(1L,"Заневский пр.");
        Address address = new Address("195000", street, "12", "", "142");

        // Муж
        Adult husband = new Adult("Петров", "Виктор", "Сергеевич", LocalDate.of(1997, 8, 24));
        husband.setPassportSeria("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017, 9, 15));
        PassportOffice po1 = new PassportOffice(1L,"","");
        husband.setIssueDepartment(po1);
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, "Санкт-Петербургский Политехнический Университет"));
        husband.setStudentId("HH12345");

        // Жена
        Adult wife = new Adult("Петрова", "Вероника", "Алекссевна", LocalDate.of(1998, 3, 12));
        wife.setPassportSeria("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));
        PassportOffice po2 = new PassportOffice(2L,"","");
        wife.setIssueDepartment(po2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1L, "Санкт-Петербургский Государственный Университет"));
        wife.setStudentId("HP56789");

        // Дети
        Child child1 = new Child("Петрова", "Ирина", "Викторовна", LocalDate.of(2018, 6, 29));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 7, 19));
        RegisterOffice ro2 = new RegisterOffice(2L,"","");
        child1.setIssueDepartment(ro2);
        child1.setAddress(address);

        Child child2 = new Child("Петров", "Иван", "Викторович", LocalDate.of(2016, 7, 30));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2016, 7, 30));
        RegisterOffice ro3 = new RegisterOffice(3L,"","");
        child2.setIssueDepartment(ro3);
        child2.setAddress(address);

        so.setHusband(husband);
        so.setWife(wife);
        so.addChild(child1);
        so.addChild(child2);
        return so;
    }


//    static void printStudentOrder(StudentOrder stOr){
//        System.out.println(stOr.getStudentOrderId());
//    }
}
