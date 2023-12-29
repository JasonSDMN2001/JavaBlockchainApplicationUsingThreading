package com.unipi.iason;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBConnection {
    public void insertNewProduct(int id, String j){
        try {
            Connection connection = connect();
            String insertSQL = "INSERT OR REPLACE INTO PRODUCTS VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, j);
            int count = preparedStatement.executeUpdate();
            if(count>0){
                System.out.println(count+" record updated");
            }
            preparedStatement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String selectAll(int id){
        StringBuilder resultStringBuilder = new StringBuilder();
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            String selectSQL = "select PRODUCT from PRODUCTS where ID = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while(resultSet.next()){
                String product = resultSet.getString("PRODUCT");
                resultStringBuilder.append(product);
            }
            statement.close();
            connection.close();
            return resultStringBuilder.toString();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void createTableAndData(){
        try {
            Connection connection = connect();
            String createTableSQL = "CREATE TABLE PRODUCTS"
                    + "(ID INTEGER NOT NULL PRIMARY KEY,"
                    + "PRODUCT VARCHAR(255) NOT NULL)";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private Connection connect(){
        String connectionString = "jdbc:sqlite:blockchaindb.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
}
