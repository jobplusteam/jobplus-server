package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// MySQLConnection connection = new MySQLConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			String userid = input.getString("user_id");
			String password = input.getString("password");
			String firstname = input.getString("firstname");
			String lastname = input.getString("lastname");
			
			JSONObject obj = new JSONObject();
//			if (connection.registerUser(userid, password, firstname, lastname)) {
//				obj.put("status", "ok");
//			}else {
//				obj.put("status", "User Already Exists");
//			}
			RpcHelper.writeJsonObject(response, obj);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			// connection.close();
			System.out.print("connection closed");
		}
	}

}
