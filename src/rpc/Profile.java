package rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.MySQLConnection;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Profile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// allow access only if session exists
		HttpSession session = request.getSession(false);

		// if session not exists, return message
		if (session == null) {
			RpcHelper.protectEndpoint(request, response);
			return;
		}

		// prepare response body
		JSONObject res = new JSONObject();

		// if session exists, find the user_id from session
		String userId = session.getAttribute("user_id").toString();

		MySQLConnection connection = new MySQLConnection();
		try {
			String fullName = connection.getFullname(userId);
			List<String> interests = connection.getInterests(userId);

			// put attributes to response
			res.put("user_id", userId);
			res.put("full_name", fullName);
			res.put("interests", new JSONArray(interests));
			RpcHelper.writeJsonObject(request, response, res);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
