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

		//allow access only if session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		//optional
		String userId = session.getAttribute("user_id").toString();

		List<Item> items = new ArrayList<>();

		MySQLConnection connection = new MySQLConnection();
		Set<String> savedJobs = connection.getSavedJobs(userId);

		if (savedJobs.size() == 0) {
			// case 1: user has no saved jobs
			InterestsRecommendation recommendation = new InterestsRecommendation(); 
			try {
				items = recommendation.recommendItems(userId);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// case 2: user has saved jobs
			KeywordRecommendation recommendation = new KeywordRecommendation();

			try {
				items = recommendation.recommendItems(userId);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MonkeyLearnException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		JSONArray array = new JSONArray();
		for (Item item : items) {
			array.put(item.toJSONObject());
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
