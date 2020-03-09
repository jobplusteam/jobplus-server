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
 * Servlet implementation class Search
 */
@WebServlet("/search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Search() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// find userId from session
		String userId = "";
		HttpSession session = request.getSession(false);
		if (session != null) {
			userId = session.getAttribute("user_id").toString();
		}

		// parse request parameters
		String description = request.getParameter("description");
		String location = request.getParameter("location");
		String full_time = request.getParameter("full_time");

		// find search results from Github
		GithubJobClient client = new GithubJobClient();
		List<Item> jobs = client.search(description, location, full_time);

		// find saved jobs from db
		MySQLConnection connection = new MySQLConnection();
		Set<String> savedJobs = connection.getSavedJobs(userId);
		connection.close();

		// label saved jobs before return 
		JSONArray array = RpcHelper.labeledJobs(jobs, savedJobs);

		// return labeled jobs
		RpcHelper.writeJsonArray(request, response, array);
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
