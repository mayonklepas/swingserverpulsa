/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author ASUS
 */
public class Dbhelper {
    
    Filehelper fh=new Filehelper();
    
    public String host="jdbc:postgresql://"+fh.host()+":"+fh.port()+"/"+fh.database()+"";
    //public String host="jdbc:postgresql://127.0.0.1:5432/serverpulsa";
    public String user=fh.username();
    public String password=fh.password();
    public Connection conn;
    

    public Dbhelper() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dbhelper.class.getName()).log(Level.SEVERE, null, ex);
              Alert al=new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Kesalahan");
                    al.setHeaderText("Terjadi Kesalahan Pada Aplikasi");
                    al.setContentText(String.valueOf(ex));
                    al.showAndWait();
        } catch (InstantiationException ex) {
            Logger.getLogger(Dbhelper.class.getName()).log(Level.SEVERE, null, ex);
             Alert al=new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Kesalahan");
                    al.setHeaderText("Terjadi Kesalahan Pada Aplikasi");
                    al.setContentText(String.valueOf(ex));
                    al.showAndWait();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Dbhelper.class.getName()).log(Level.SEVERE, null, ex);
           Alert al=new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Kesalahan");
                    al.setHeaderText("Terjadi Kesalahan Pada Aplikasi");
                    al.setContentText(String.valueOf(ex));
                    al.showAndWait();
        }
    
    }
    
    public void connect() throws SQLException{
        conn=DriverManager.getConnection(host, user, password);
    }
    
    public void disconnect() throws SQLException{
        conn.close();
    }
    
    public PreparedStatement read(String sql) throws SQLException{
        PreparedStatement pre=conn.prepareStatement(sql);
        return pre;
    }
    
     public PreparedStatement readdetail(String sql,int row,Object[] o) throws SQLException{
        PreparedStatement pre=conn.prepareStatement(sql);
        int i=0;
        while (i < row){
            int z=i+1;
            pre.setObject(z, o[i]);
            i++;
        }
        return pre;
    }
    
    public void insert(String sql,int row,Object[] o)throws SQLException{
        PreparedStatement pre=conn.prepareStatement(sql);
        int i=0;
        while (i < row){
            int z=i+1;
            pre.setObject(z, o[i]);
            i++;
        }
        pre.executeUpdate();
    }
    
    public void update(String sql,int row,Object[] o)throws SQLException{
        PreparedStatement pre=conn.prepareStatement(sql);
        int i=0;
        while (i < row){
            int z=i+1;
            pre.setObject(z, o[i]);
            i++;
        }
        pre.executeUpdate();
    }
    
     public void delete(String sql,String key)throws SQLException{
        PreparedStatement pre=conn.prepareStatement(sql);
        pre.setString(1, key);
        pre.executeUpdate();
    }
    
    
    public void exec(String sql)throws SQLException{
        PreparedStatement pre=conn.prepareStatement(sql);
        pre.executeUpdate();
    }
    
    
    
    
    
    
    
    
}
