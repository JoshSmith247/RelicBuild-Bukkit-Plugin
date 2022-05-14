package com.pezapp.relicbuild.Games;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CoinHuntDBConnect {
	 
    private Connection connection;
    private String host, database, username, password;
    private int port;
 
    public void onEnable() {
        host = "localhost";
        port = 3306; // Default mySQL port
        database = "aicrealmDatabase";
        username = "AICadminuser";
        password = "pass";    
        try {    
            openConnection();
            //Statement statement = connection.createStatement();          
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public void onDisable() {
    }
 
    public void openConnection() throws SQLException, ClassNotFoundException {
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
	    }
    }
}