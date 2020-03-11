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
		// if session not exists, return message
		if (session == null) {
			RpcHelper.protectEndpoint(request, response);
			return;
		}

		String userId = session.getAttribute("user_id").toString();
		JSONArray array = new JSONArray();

		// find saved jobs from db
		MySQLConnection connection = new MySQLConnection();
		Set<String> savedJobIds = connection.getSavedJobs(userId);
		connection.close();

		// find all saved jobs from Github
		GithubJobClient client = new GithubJobClient();
		try {
			for (String id : savedJobIds) {
				Item job = client.getJobfromJobId(id);
				JSONObject obj = job.toJSONObject();
				obj.append("is_saved", true);
				array.put(obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
		// if session not exists, return message
		if (session == null) {
			RpcHelper.protectEndpoint(request, response);
			return;
		}

		// prepare response
		JSONObject res = new JSONObject();

		// session exists, find user id
		String userId = session.getAttribute("user_id").toString();
		JSONObject input = RpcHelper.readJSONObject(request);

		// save job id into db
		MySQLConnection connection = new MySQLConnection();
		try {
			// parse request body
			String jobId = input.getString("job_id");
			boolean isSave = input.getBoolean("is_save");
			// save job id into interest table
			String result = isSave ? "SAVE" : "UNSAVE";
			String message = connection.setSavedJob(userId, jobId, isSave) ? "successful!" : "failed!";
			res.put("result", result);
			res.put("message", message);

			RpcHelper.writeJsonObject(request, response, res);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
