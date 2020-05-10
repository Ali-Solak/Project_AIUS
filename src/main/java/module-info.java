/**
 *
 */
module Project_AIUS {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.elusive;
    requires org.kordamp.ikonli.entypo;
    requires org.kordamp.ikonli.javafx;
    requires com.jfoenix;
    requires eu.hansolo.medusa;
    requires eu.hansolo.tilesfx;
    requires oshi.core;
    requires org.slf4j;

    opens Project_AIUS.Controller;
    opens Project_AIUS.application;
    exports Project_AIUS;
}
