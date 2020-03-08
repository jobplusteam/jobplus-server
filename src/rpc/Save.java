package rpc;

import java.io.IOException;
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
 * Servlet implementation class save
 */
@WebServlet("/save")
public class Save extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Save() {
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
		if (session == null) {
			response.setStatus(403);
			return;
		}

		String userId = session.getAttribute("user_id").toString();
		JSONArray array = new JSONArray();

		MySQLConnection connection = new MySQLConnection();
		Set<String> savedJobIds = connection.getSavedJobs(userId);
		connection.close();

		GithubJobClient client = new GithubJobClient();

		for (String id : savedJobIds) {
			Item job = client.getJobfromJobId(id);
			JSONObject obj = job.toJSONObject();
			try {
				obj.append("is_saved", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);
		}
		RpcHelper.writeJsonArray(request, response, array);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// allow access only if session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		
		String userId = session.getAttribute("user_id").toString();
		JSONObject input = RpcHelper.readJSONObject(request);
		try {
			JSONObject res = new JSONObject();
			// payload expected { "job_id" : "xxxxx" , "is_save" : "true"}
			String jobId = input.getString("job_id");
			Boolean isSave = input.getBoolean("is_save");

			MySQLConnection connection = new MySQLConnection();
			if (isSave) {
				if (connection.setSavedJob(userId, jobId)) {
					res.put("result", "SAVE");
					res.put("message", "success!");
				} else {
					res.put("result", "SAVE");
					res.put("message", "failed!");
				}
				
			} else {
				if (connection.unSetSavedJob(userId, jobId)) {
					res.put("result", "UNSAVE");
					res.put("message", "success!");
				} else {
					res.put("result", "UNSAVE");
					res.put("message", "failed!");
				}
			}

			connection.close();
			RpcHelper.writeJsonObject(request, response, res);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
