module university {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens university to javafx.fxml;
    exports university;
    exports university.ui;
    exports university.entities;
    exports university.exceptions;
    exports university.controller;
    exports university.repository;
}