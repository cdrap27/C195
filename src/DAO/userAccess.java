package DAO;

import Database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * gets user access
 */
public class userAccess {
    /**
     * attempts to find matching username and password
     * @param userName username
     * @param password password
     * @return userfound
     */
    public static Boolean userFound(String userName, String password){
        try{
            String sql = "SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" +
                    password  +"'";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(rs.getString("User_Name").equals(userName)){
                if(rs.getString("Password").equals(password)){
                    return true;
                }
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;

    }
}
