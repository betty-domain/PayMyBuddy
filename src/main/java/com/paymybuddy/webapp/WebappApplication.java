package com.paymybuddy.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/paymybuddy\",\"root\",\"rootroot\"");
        }
        catch (Exception exception)
        {
            String str = exception.getMessage();
        }
    }

}
