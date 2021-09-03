package edu.bmv.city_register.web;

import edu.bmv.city_register.dao.PersonCheckDao;
import edu.bmv.city_register.dao.PoolConnectionBuilder;
import edu.bmv.city_register.domaim.PersonRequest;
import edu.bmv.city_register.domaim.PersonResponse;
import edu.bmv.city_register.exception.PersonCheckException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.*;

@WebServlet(name = "CheckPersonServlet", urlPatterns = {"/checkPerson"})
public class CheckPersonServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CheckPersonServlet.class);
    private  PersonCheckDao dao;

    @Override
    public void init() throws ServletException {
        logger.info("Servlet is created. Init PersonCheckDao");
        dao = new PersonCheckDao();
        dao.setConnectionBuilder(new PoolConnectionBuilder());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        PersonRequest pr = new PersonRequest();
        pr.setSurName( req.getParameter("surName"));
        pr.setGivenName( req.getParameter("givenName"));
        pr.setPatronymic( req.getParameter("patronymic"));
        //resp.getWriter().println(req.getParameter("dateOfBirth"));
        LocalDate dateOfBirth = parse(req.getParameter("dateOfBirth"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        pr.setDateOfBirth(dateOfBirth);
        pr.setStreetCode(Integer.parseInt(req.getParameter("streetCode")));
        pr.setBuilding(req.getParameter("building"));
        pr.setExtension(req.getParameter("extension"));
        pr.setApartment(req.getParameter("apartment"));
        try {
            PersonResponse ps = dao.checkPerson(pr);
            if(ps.isRegistered()){
                resp.getWriter().println(pr.getSurName() +" зарегестрирован в ГРН.");
            }else {
                resp.getWriter().println(pr.getSurName()  +" НЕ зарегестрирован в ГРН.");
            }
        } catch (PersonCheckException e) {
            e.printStackTrace();
        }
    }
}