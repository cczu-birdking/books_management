package com.itBoy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class BooksManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksManagementApplication.class, args);
    }

}
