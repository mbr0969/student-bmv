package edu.bmv.studentorder.dao;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DBInit {

    public static void startUp() throws Exception {
        URL studentProject = DictionaryDaoImplTest.class.getClassLoader().getResource("student_project.sql");
        URL studentData = DictionaryDaoImplTest.class.getClassLoader().getResource("student_data.sql");

        List<String> strProject = Files.readAllLines(Paths.get(studentProject.toURI()));
        List<String> strData= Files.readAllLines(Paths.get(studentData.toURI()));

        String sqlProject = strProject.stream().collect(Collectors.joining());
        String sqlData = strData.stream().collect(Collectors.joining());

        try(Connection con = ConnectionBuilder.getConnection(); Statement stmt = con.createStatement()){
            stmt.executeUpdate(sqlProject);
            stmt.executeUpdate(sqlData);
            System.out.println("Start up");
        }
    }
}
