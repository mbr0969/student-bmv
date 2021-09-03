package edu.bmv.studentorder.dao;

import edu.bmv.studentorder.config.Config;
import edu.bmv.studentorder.domain.CountryArea;
import edu.bmv.studentorder.domain.PassportOffice;
import edu.bmv.studentorder.domain.RegisterOffice;
import edu.bmv.studentorder.domain.Street;
import edu.bmv.studentorder.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DictionaryDaoImpl implements IDictionaryDao {
    private static final Logger logger =  LoggerFactory.getLogger(DictionaryDaoImpl.class);

    private static final  String GET_STREET = "SELECT * FROM jc_street WHERE  UPPER(street_name) LIKE  UPPER(?)";
    private static final  String GET_PASSPORT_OFFICE = "SELECT p_office_id, p_office_area_id, p_office_name FROM jc_passport_office WHERE p_office_area_id=?  ";
    private static final  String GET_REGISTER_OFFICE = "SELECT r_office_id, r_office_area_id, r_office_name FROM jc_register_office WHERE r_office_area_id=?  ";
    private static final  String GET_AREA = "SELECT area_id, area_name FROM jc_country_struct WHERE area_id LIKE ? AND area_id <> ?";

     private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }

    public List<Street> findStreets(String pattern) throws DaoException {
        List<Street> list = new LinkedList<>();
        try(Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_STREET)) {
            stmt.setString(1,"%"+pattern+"%");
            ResultSet rs =stmt.executeQuery();
            while (rs.next()){
                Street street = new Street(rs.getLong(1),  rs.getString(2));
                list.add(street);
            }
        }catch (SQLException ex){
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return list;
    }

    @Override
    public List<PassportOffice> findPassportOffice(String areaId) throws DaoException {
        List<PassportOffice> list = new LinkedList<>();
        try(Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_PASSPORT_OFFICE)) {
            stmt.setString(1,areaId);
            ResultSet rs =stmt.executeQuery();
            while (rs.next()){
                PassportOffice passportOffice = new PassportOffice(rs.getLong(1), rs.getString(2), rs.getString(3));
                list.add(passportOffice);
            }
        }catch (SQLException ex){
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return list;
    }

    @Override
    public List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException {
        List<RegisterOffice> list = new LinkedList<>();
        try(Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_REGISTER_OFFICE)) {
            stmt.setString(1,areaId);
            ResultSet rs =stmt.executeQuery();
            while (rs.next()){
                RegisterOffice registerOffice = new RegisterOffice(rs.getLong(1), rs.getString(2), rs.getString(3));
                list.add(registerOffice);
            }
        }catch (SQLException ex){
            throw new DaoException(ex);
        }
        return list;
    }

    @Override
    public List<CountryArea> findAreas(String areaId) throws DaoException {

        List<CountryArea> list = new LinkedList<>();
        try(Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(GET_AREA)) {
            String param1 = buildParam(areaId);
            String param2 =areaId;

            stmt.setString(1, param1);
            stmt.setString(2, param2);
            ResultSet rs =stmt.executeQuery();
            while (rs.next()){
                CountryArea countryArea = new CountryArea(rs.getString(1), rs.getString(2));
                list.add(countryArea);
            }
        }catch (SQLException ex){
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return list;
    }

    private String buildParam(String areaId) throws SQLException {
        if(areaId == null || areaId.trim().isEmpty()){
            return "__0000000000";
        } else if(areaId.endsWith("0000000000")){
            return areaId.substring(0,2)+"___0000000";
        }else if(areaId.endsWith("0000000")){
            return areaId.substring(0, 5) +"___0000";
        }else if(areaId.endsWith("0000")){
            return  areaId.substring(0,8) + "____";
        }
        throw new SQLException("Invalid parameter areaId :" + areaId);

    }

}
