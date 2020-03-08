package rpc;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import entity.Item;
import external.GithubJobClient;

/**
 * Servlet implementation class Nearby
 */
@WebServlet("/nearby")
public class Nearby extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Nearby() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = "";

		// allow access only if session exists
		HttpSession session = request.getSession(false);
		if (session != null) {
			// optional
			userId = session.getAttribute("user_id").toString();
		}

		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));

		GithubJobClient client = new GithubJobClient();
		List<Item> jobs = client.nearby(lat, lon);

		MySQLConnection connection = new MySQLConnection();
		Set<String> savedJobs = connection.getSavedJobs(userId);
		connection.close();

		JSONArray array = new JSONArray();
		for (Item job : jobs) {
			JSONObject obj = job.toJSONObject();
			boolean isSaved = false;
			if (savedJobs.contains(job.getId())) {
				isSaved = true;
			}
			try {
				obj.put("is_saved", isSaved);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			array.put(obj);
		}
		RpcHelper.writeJsonArray(response, array);
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
