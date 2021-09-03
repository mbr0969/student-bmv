package edu.bmv.studentorder.dao;

import edu.bmv.studentorder.config.Config;
import edu.bmv.studentorder.domain.*;
import edu.bmv.studentorder.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentOrderDaoImpl implements IStudentOrderDao{

    private static final Logger logger =  LoggerFactory.getLogger(StudentOrderDaoImpl.class);


    private static final String INSERT_ORDER = "INSERT INTO jc_student_order( student_order_status, student_order_date, h_sur_name, " +
            " h_given_name, h_patronymic, h_date_of_birth, h_passport_seria,  h_passport_number, h_passport_date, h_passport_office_id, h_post_index, " +
            " h_street_code, h_building, h_extension, h_apartment, h_university_id, h_student_number,  w_sur_name, w_given_name, w_patronymic, " +
            "w_date_of_birth, w_passport_seria,  w_passport_number, w_passport_date, w_passport_office_id, w_post_index,  w_street_code, w_building," +
            " w_extension, w_apartment, w_university_id, w_student_number,  certificate_id, register_office_id, marriage_date)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_CHILD = "INSERT INTO public.jc_student_child( student_order_id, c_sur_name, c_given_name, c_patronymic, c_date_of_birth," +
            " c_certificate_number, c_certificate_date, c_register_office_id,  c_post_index, c_street_code, c_building, c_extension, c_apartment)" +
            " VALUES (?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_ORDERS =  "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
            "po_h.p_office_area_id AS h_p_office_area_id, po_h.p_office_name AS h_p_office_name,  " +
            "po_w.p_office_area_id AS w_p_office_area_id, po_w.p_office_name AS w_p_office_name " +
            "FROM jc_student_order AS so " +
            "INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
            "INNER JOIN jc_passport_office AS po_h ON so.h_passport_office_id = po_h.p_office_id " +
            "INNER JOIN jc_passport_office AS po_w ON so.w_passport_office_id = po_w.p_office_id " +
            "WHERE student_order_status = ?  ORDER BY student_order_date LIMIT  ?;";

    private static final String SELECT_CHILD = "SELECT soc.*, ro.r_office_area_id, ro.r_office_name " +
            "FROM jc_student_child AS soc " +
            "INNER JOIN jc_register_office as ro ON ro.r_office_id = soc.c_register_office_id " +
            "WHERE soc.student_order_id IN ";

    private static final String SELECT_ORDERS_FULL =  "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
            "po_h.p_office_area_id AS h_p_office_area_id, po_h.p_office_name AS h_p_office_name,  " +
            "po_w.p_office_area_id AS w_p_office_area_id, po_w.p_office_name AS w_p_office_name, " +
            "soc.*, ro_c.r_office_area_id , ro_c.r_office_name "+
            "FROM jc_student_order AS so " +
            "INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
            "INNER JOIN jc_passport_office AS po_h ON so.h_passport_office_id = po_h.p_office_id " +
            "INNER JOIN jc_passport_office AS po_w ON so.w_passport_office_id = po_w.p_office_id " +
            "INNER JOIN  jc_student_child AS soc ON soc.student_order_id = so.student_order_id "+
            "INNER JOIN jc_register_office AS ro_c ON ro_c.r_office_id = soc.c_register_office_id "+
            "WHERE student_order_status = ?  ORDER BY so.student_order_id LIMIT ?;";


    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }

    //Save student order
    @Override
    public Long saveStudentOrder(StudentOrder studentOrder) throws DaoException {
        long result = -1L;

        logger.debug("SO : {}",studentOrder.toString());

        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"} )) {
            con.setAutoCommit(false);
            try {
                //header
                stmt.setInt(1, StudentOrderStatus.START.ordinal());// student_order_status
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now())); //student_order_date

                //husband
                setParamsForAdult( stmt, 3, studentOrder.getHusband());

                //wife
                setParamsForAdult( stmt, 18, studentOrder.getWife());

                stmt.setString(33, studentOrder.getMarriageCertificateId()); //certificate_id
                stmt.setLong(34, studentOrder.getMarriageOffice().getOfficeId()); //register_office_id
                stmt.setDate(35, java.sql.Date.valueOf(studentOrder.getMarriageDate()));//marriage_date

                stmt.executeUpdate();

                ResultSet gkRs = stmt.getGeneratedKeys();
                if(gkRs.next()){
                    result = gkRs.getLong(1);
                }

                gkRs.close();
                saveChildren( con,  result, studentOrder);
                con.commit();

            }catch (SQLException ex){
                con.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException();
        }
        return result;
    }

     private void saveChildren(Connection con, Long soId, StudentOrder so) throws SQLException{

        try(PreparedStatement stmt = con.prepareStatement(INSERT_CHILD)) {
             for (Child child: so.getChildren()){
                 stmt.setLong(1,soId);
                 setParamsForChild(stmt, child);
                 stmt.addBatch();
             }
             stmt.executeBatch();
        }
    }

    private void setParamsForAdult( PreparedStatement stmt, int start, Adult adult) throws SQLException {
        setParamsForPerson(stmt, start, adult);
        stmt.setString(start + 4, adult.getPassportSeria()); //h_passport_seria
        stmt.setString(start + 5, adult.getPassportNumber());  //h_passport_number
        stmt.setDate(start + 6, Date.valueOf(adult.getIssueDate())); //h_passport_date
        stmt.setLong(start + 7, adult.getIssueDepartment().getOfficeId()); //h_passport_office_id

        setParamsForAddress(stmt, start + 8, adult);

        stmt.setLong(start + 13,adult.getUniversity().getUniversityId()); //h_university_id
        stmt.setString(start + 14, adult.getStudentId());//h_student_number
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {
        setParamsForPerson(stmt,2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, java.sql.Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt,9, child);

    }

     private void setParamsForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        stmt.setString(start, person.getSurName()); //h_sur_name
        stmt.setString(start + 1, person.getGivenName()); //h_given_name
        stmt.setString(start + 2, person.getPatronymicName());//h_patronymic
        stmt.setDate(start + 3, Date.valueOf(person.getDateOfBirth())); //h_date_of_birth
    }

    private void setParamsForAddress(PreparedStatement stmt, int start, Person person) throws SQLException {
        Address h_address = person.getAddress();
        stmt.setString(start ,h_address.getPostCode()); //h_post_index
        stmt.setLong(start + 1, h_address.getStreet().getStreetCode()); //h_street_code
        stmt.setString(start + 2, h_address.getBuilding());//h_building
        stmt.setString(start + 3, h_address.getExtension()); //h_extension
        stmt.setString(start + 4, h_address.getApartment()); //h_apartment
    }

    //get student order
    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        //getOrdersTwoSelect();
        return getOrdersOneSelect();
        //return getOrdersTwoSelect();
    }

    private List<StudentOrder> getOrdersOneSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try(Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS_FULL)){

            Map<Long, StudentOrder> maps = new HashMap<>();

            stmt.setInt(1,StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt( Config.getProperty(Config.DB_LIMIT));
            stmt.setInt(2, limit);
            ResultSet rs =stmt.executeQuery();
            int counter = 0;
            while (rs.next()){
                Long soId = rs.getLong("student_order_id");
                if (!maps.containsKey(soId)) {
                    StudentOrder so = getFullStudentOrder(rs);
//                    logger.info("So : {} ", so.toString());
                    result.add(so);
                    maps.put(soId,so);
                }
                StudentOrder so = maps.get(soId);
                so.addChild(fillChild(rs));
                counter++;
            }
            if(counter >= limit){
                result.remove(result.size() -1);
            }
            rs.close();
        }catch (SQLException ex){
            logger.error(ex.getMessage(), ex);
            throw  new DaoException();
        }

        return result;
    }

    private List<StudentOrder> getOrdersTwoSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try(Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS)){

            stmt.setInt(1,StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt( Config.getProperty(Config.DB_LIMIT));
            stmt.setInt(2, limit);
            ResultSet rs =stmt.executeQuery();

            while (rs.next()){
                StudentOrder so = getFullStudentOrder(rs);
                result.add(so);
            }
            findChildren(con, result);

            rs.close();
        }catch (SQLException ex){
            logger.error(ex.getMessage(), ex);
            throw  new DaoException();
        }

        return result;
    }

    private StudentOrder getFullStudentOrder(ResultSet rs) throws SQLException {
        StudentOrder so = new StudentOrder();
        fillStudentOrder(rs, so);
        fillMarriage(rs, so);
        so.setHusband(fiiAdult(rs, "h_"));
        so.setWife(fiiAdult(rs, "w_"));
        return so;
    }

    private void findChildren(Connection con, List<StudentOrder> result) throws SQLException {
       String cl = "(" + result.stream().map(so -> String.valueOf(so.getStudentOrderId()))
                .collect(Collectors.joining(",")) +")";

       Map<Long, StudentOrder> maps = result.stream().collect(Collectors
               .toMap(StudentOrder::getStudentOrderId, so -> so));

       try(PreparedStatement stmt = con.prepareStatement(SELECT_CHILD + cl)){
           ResultSet rs = stmt.executeQuery();
           while (rs.next()){
              //System.out.println(rs.getLong(1) +" : " + rs.getString(2) +" : "+rs.getString(3));
              Child ch = fillChild(rs);
              StudentOrder so = maps.get(rs.getLong("student_order_id"));
              so.addChild(ch);
           }
       }
    }

     private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException {
        so.setStudentOrderId(rs.getLong("student_order_id"));
        so.setStudentOrderStatus(StudentOrderStatus.fromValue( rs.getInt("student_order_status")));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
    }

    private Adult fiiAdult(ResultSet rs, String prefix) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(rs.getString(prefix+ "sur_name"));
        adult.setGivenName(rs.getString(prefix+ "given_name"));
        adult.setPatronymicName(rs.getString(prefix+ "patronymic"));
        adult.setDateOfBirth(rs.getDate(prefix+ "date_of_birth").toLocalDate());
        adult.setPassportSeria(rs.getString(prefix+ "passport_seria"));
        adult.setPassportNumber(rs.getString(prefix+ "passport_number"));
        adult.setIssueDate(rs.getDate(prefix+ "passport_date").toLocalDate());

        Long poOd = rs.getLong(prefix+"passport_office_id");
        String poAreaId = rs.getString(prefix+"p_office_area_id");
        String poName = rs.getString(prefix+"p_office_name");
        PassportOffice po = new PassportOffice(poOd, poAreaId, poName);
        adult.setIssueDepartment(po);

        Street street = new Street(rs.getLong(prefix + "street_code"),"Street name");
        Address address = new Address(rs.getString(prefix + "post_index"), street, rs.getString(prefix + "building"), rs.getString(prefix+"extension")
                , rs.getString(prefix + "apartment") );
        adult.setAddress(address);
        adult.setStudentId(rs.getString(prefix +"student_number"));

        University university = new University(rs.getLong(prefix +"university_id"), prefix +" university Name ");
        adult.setUniversity(university);

        //  public Address(String postCode, Street street, String building, String extension, String apartment) {
        //    this.postCode = postCode;
        //    this.street = street;
        //    this.building = building;
        //    this.extension = extension;
        //    this.apartment = apartment;
        // public Street(Long streetCode, String streetName) {
        //        this.streetCode = streetCode;
        //        this.streetName = streetName;
        //    }
        // }
        return adult;
    }

    private void fillMarriage(ResultSet rs, StudentOrder so) throws SQLException {
        so.setMarriageCertificateId(rs.getString("certificate_id"));
        so.setMarriageDate(rs.getDate("marriage_date").toLocalDate());
        Long roId = rs.getLong("register_office_id");
        String areaId = rs.getString("r_office_area_id");
        String officeName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, areaId, officeName);
        so.setMarriageOffice(ro);
    }

    private Child fillChild(ResultSet rs) throws SQLException{
        String surName = rs.getString("c_sur_name");
        String givenName = rs.getString("c_given_name");
        String patronymicName = rs.getString("c_patronymic");
        LocalDate dateOfBirth = rs.getDate("c_date_of_birth").toLocalDate();
        Child child = new Child(surName, givenName,patronymicName,dateOfBirth);

        child.setCertificateNumber(rs.getString("c_certificate_number"));
        child.setIssueDate(rs.getDate("c_certificate_date").toLocalDate());

        Long roId = rs.getLong("c_register_office_id");
        String areaId = rs.getString("r_office_area_id");
        String officeName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, areaId, officeName);
        Street street = new Street(rs.getLong("c_street_code"), "");
        child.setIssueDepartment(ro);
        Address address = new Address(rs.getString( "c_post_index"), street, rs.getString( "c_building"),
                rs.getString("c_extension")  , rs.getString( "c_apartment") );
        child.setAddress(address);
        return child;
    }

}
