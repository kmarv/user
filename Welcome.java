import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import encryption.MD5;

public class Welcome extends HttpServlet {
    PrintWriter out;
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String username = request.getParameter("userName");
        String password = MD5.getMD5(request.getParameter("password"));
        response.setContentType("text/html");
        out = response.getWriter();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jav_db", "root", "");
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM register WHERE UserName = '"+username+"' AND Password ='"+password+"'";
            ResultSet result = statement.executeQuery(query);
            if(result.next()){
                String firstName = result.getString("FirstName");
                String lastName = result.getString("LastName");
                username = result.getString("UserName");
                String address = result.getString("PhysicalAddress");
                String phone = result.getString("Telephone");
                password = result.getString("Password");
                String email = result.getString("Email");
                out.write("<!DOCTYPE html><html><head><title>Details</title><link rel='stylesheet' type='text/css' media='screen' href='main.css'></head>");
                out.write("<body><div class='detail-container'><div class='container-up'><div class='intro'><div class='welcome-section'><div class='welcome-left'></div><div class='welcome'>Welcome</div><div class='welcome-right'></div></div>");
                out.write("<div class='uname'>Hey <span style='font-weight:300;color:#B6D1F9;'>"+username+"</span>, checkout your details!</div></div>");
                out.write("<div class='field-section-go nobottom'><div class='info-section'><div class='field'>First Name</div><div class='detail'>"+firstName+"</div></div><hr>");
                out.write("<div class='info-section'><div class='field'>Last Name</div><div class='detail'>"+lastName+"</div></div><hr></div></div>");
                out.write("<div class='container-down'><div class='field-section-go'><div class='info-section'><div class='field'>Username</div><div class='detail'>"+username+"</div></div><hr>");
                out.write("<div class='info-section'><div class='field'>Address</div><div class='detail'>"+address+"</div></div><hr>");
                out.write("<div class='info-section'><div class='field'>Telephone</div><div class='detail'>"+phone+"</div></div><hr>");
                out.write("<div class='info-section'><div class='field'>Email</div><div class='detail'>"+email+"</div></div><hr>");
                out.write("<div class='buttons'><a class='go-btn go-btn-right btn-black' href='Logout'>Logout</a><div class='clr'></div></div></div></div></div></body></html>");
            }

            
        }
        catch(Exception e){
            e.printStackTrace();
            out.write(e.toString());
        } 
    }
}
