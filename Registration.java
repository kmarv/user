import javax.servlet.*;
import javax.servlet.http.*;
import encryption.MD5;
import java.io.*;
import java.sql.*;

public class Registration extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String firstName = request.getParameter("firstName");
        String lastName= request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String address = request.getParameter("address");
        String phone = request.getParameter("telephone");
        String password = request.getParameter("password");
        String confirmPass = request.getParameter("confirmPass");
        String email = request.getParameter("email");

        try{
            if (confirmPass.equals(password)){    
                password = MD5.getMD5(password);
                // Loading the JDBC Driver
                Class.forName("com.mysql.jdbc.Driver");
                // out.println("Driver Loaded");

                // Establishing a connectiion
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jav_db", "root", "");
                // out.println("Database Connected");

                //Create a Statements
                String checkQ = "SELECT UserName FROM register WHERE Username='"+userName+"'"; //Ensuring no one else has the same username
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(checkQ);
                if(result.next()){
                    //If there already exists a user with the same username
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/register.html");
                    dispatcher.include(request,response);
                    out.write("<div class='wrongDetails-three'>Username '"+userName+"' Already exists</div>");
                }
                else{
                    String insertQ = "INSERT into register (FirstName,LastName,UserName,PhysicalAddress,Telephone,Password,Email) VALUES (?,?,?,?,?,?,?)";
                    PreparedStatement pstatement = conn.prepareStatement(insertQ);
                    pstatement.setString(1, firstName);
                    pstatement.setString(2, lastName);
                    pstatement.setString(3, userName);
                    pstatement.setString(4, address);
                    pstatement.setString(5, phone);
                    pstatement.setString(6, password);
                    pstatement.setString(7, email);
                    pstatement.executeUpdate();
                    //Maintaining the values in ServletContext
                    //Transferring To the Welcome Servlet
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Welcome");
                    dispatcher.forward(request,response);
                }
            }
            else{ //Incase the user enters unmatching passwords
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/register.html");
                dispatcher.include(request,response);
                out.write("<div class='wrongDetails-two'>Please enter matching passwords</div>");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        out.close();
    }
}