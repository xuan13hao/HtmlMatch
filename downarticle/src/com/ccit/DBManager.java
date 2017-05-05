package com.ccit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.PreparedStatement;

public class DBManager
{
    private static DBManager instance=null;
    private Connection con=null;
    private Statement stat=null;
    private PreparedStatement ps=null;
    private DBManager()
    {
    	try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/movies?user=root&password=xuanhao13");
			 stat=con.createStatement();
			 ps=con.prepareStatement("insert into article(title,content,url) values(?,?,?)");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    public static DBManager getDBM()
    {
    	if(null==instance)instance=new DBManager();
    	return instance;
    }
    public void insertData(String title,String content,String url)
    {
    	try
		{
			ps.setString(1, title);
			ps.setString(2, content);
			ps.setString(3, url);
			ps.executeUpdate();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void insertData(String sql)
    {
    	try
		{
			stat.executeUpdate(sql);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
}
