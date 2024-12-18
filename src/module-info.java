/**
 * 
 */
/**
 * 
 */
module CalouseIF {
    requires javafx.controls;
    requires java.sql;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.base;
	requires mysql.connector.java;
	opens models to javafx.base;
	exports models;
    exports main to javafx.graphics;

}