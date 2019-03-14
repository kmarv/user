import javax.servlet.*;
import javax.servlet.http.*;
import encryption.MD5;
import java.io.*;
import java.sql.*;

public class Login extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        password = MD5.getMD5(password);
        
        try{
            // Loading the JDBC Driver
            Class.forName("com.mysql.jdbc.Driver");
            // out.println("Driver Loaded");

            // Establishing a connectiion
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jav_db", "root", "");
            // out.println("Database Connected");

            //Create a Statement
            String selectQ = "SELECT UserName FROM register WHERE UserName = ? AND Password = ?";
            PreparedStatement pstatement = conn.prepareStatement(selectQ);
            pstatement.setString(1, userName);
            pstatement.setString(2, password);
            ResultSet result = pstatement.executeQuery();
            if (result.next()){
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Welcome");
                dispatcher.forward(request,response);
            }else {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.html");
                dispatcher.include(request,response);
                out.write("<div class='wrongDetails'>Invalid Username or Password, Try Again</div>");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        out.close();
    }
}