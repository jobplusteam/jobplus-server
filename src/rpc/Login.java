// this is a test push from mengxuan 

package rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import db.MySQLConnection;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// check if session exists
		HttpSession session = request.getSession(false);

		// prepare message
		String msg = "";
		if (session == null) { // session not exists
			msg = "no session";
		} else {			   // session exists
			String userId = session.getAttribute("user_id").toString();
			MySQLConnection connection = new MySQLConnection();
			msg = connection.getFullname(userId);
			connection.close();
		}

		// prepare response body
		JSONObject res = new JSONObject();
		try {
			res.put("status", msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonObject(request, response, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		MySQLConnection connection = new MySQLConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			String userId = input.getString("user_id");
			String password = input.getString("password");

			// prepare response body
			JSONObject res = new JSONObject();
			if (connection.verifyLogin(userId, password)) {
				HttpSession session = request.getSession();
				session.setAttribute("user_id", userId);
				session.setMaxInactiveInterval(600);
				res.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
			} else {
				res.put("status", "User Doesn't Exist");
				response.setStatus(401);
			}
			RpcHelper.writeJsonObject(request, response, res);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
