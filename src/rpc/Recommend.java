package rpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import com.monkeylearn.MonkeyLearnException;

import db.MySQLConnection;
import entity.Item;
import recommendation.InterestsRecommendation;
import recommendation.KeywordRecommendation;

/**
 * Servlet implementation class Recommend
 */
@WebServlet("/recommend")
public class Recommend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Recommend() {
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

		// session exists, get userId from session
		String userId = session.getAttribute("user_id").toString();

		// find saved jobs from db
		MySQLConnection connection = new MySQLConnection();
		Set<String> savedJobs = connection.getSavedJobs(userId);
		connection.close();

		List<Item> jobs = new ArrayList<>();
		// find recommended results based on user behavior
		try {
			if (savedJobs.size() == 0) {
				// case 1: user has no saved jobs
				System.out.println("recommended based on interests");
				InterestsRecommendation recommendation = new InterestsRecommendation();
				jobs = recommendation.recommendItems(userId);
			} else {
				// case 2: user has saved jobs
				System.out.println("recommended based on history");
				KeywordRecommendation recommendation = new KeywordRecommendation();
				jobs = recommendation.recommendItems(userId);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MonkeyLearnException e) {
			e.printStackTrace();
		}

		// lable saved jobs before return
		JSONArray array = RpcHelper.labeledJobs(jobs, savedJobs);

		// return jobs
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
