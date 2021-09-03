package edu.bmv.city_register.dao;

import edu.bmv.city_register.domaim.PersonRequest;
import edu.bmv.city_register.domaim.PersonResponse;
import edu.bmv.city_register.exception.PersonCheckException;

import java.sql.*;

public class PersonCheckDao {

    private static final String SQL_REQUEST = "SELECT ap.temporal  FROM cr_address_person AS ap" +
            " INNER JOIN cr_person AS p ON p.person_id = ap.person_id" +
            " INNER JOIN cr_address AS a ON a.address_id = ap.address_id" +
            " WHERE CURRENT_DATE > ap.start_date AND (CURRENT_DATE <= ap.end_date OR ap.end_date IS NULL)" +
            " AND UPPER(p.sur_name) = UPPER(?) AND UPPER(p.given_name) =UPPER(?) AND UPPER(p.patronymic) = UPPER(?) " +
            " AND p.date_of_birth = ? AND a.street_code = ? AND a.building = ? ";

    private IConnectionBuilder connectionBuilder;

    public void setConnectionBuilder(IConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    private Connection getConnection() throws SQLException {
        return connectionBuilder.getConnection();
    }

//    public PersonCheckDao() {
//        try {
//            Class.forName("org.postgresql.Driver");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public PersonResponse checkPerson(PersonRequest request) throws PersonCheckException {
        PersonResponse response = new PersonResponse();

        String sql = SQL_REQUEST;
        if (request.getExtension() != null) {
            sql += "AND a.extension = ? ";
        } else {
            sql += " AND  a.extension IS NULL ";
        }
        if (request.getApartment() != null) {
            sql += "AND a.apartment = ? ";
        } else {
            sql += " AND  a.apartment IS NULL ";
        }

        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            int count = 1;
            stmt.setString(count++, request.getSurName());
            stmt.setString(count++, request.getGivenName());
            stmt.setString(count++, request.getPatronymic());
            stmt.setDate(count++, java.sql.Date.valueOf(request.getDateOfBirth()));
            stmt.setInt(count++, request.getStreetCode());
            stmt.setString(count++, request.getBuilding());
            if (request.getExtension() != null) {
                stmt.setString(count++, request.getExtension());
            }
            if (request.getApartment() != null) {
                stmt.setString(count, request.getApartment());
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                response.setRegistered(true);
                response.setTemporal(rs.getBoolean("temporal"));
            }
        } catch (SQLException e) {
            throw new PersonCheckException(e);
        }
        return response;
    }
}
