package external;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LinkedinConnection {
	
	private static final String HOST = "https://www.linkedin.com";
	private static final String PATH = "/oauth/v2/accessToken";
//	private static final String DEFAULT_KEYWORD = "event";
//	private static final int DEFAULT_RADIUS = 50;
	private static final String CLIENT_ID = "86a6wbm263k8sx";
	private static final String CLIENT_SECRET = "SqnAElRGxmFLe7ws";
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	
	public static void main(String[] args) {
		String url = HOST + PATH;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", CONTENT_TYPE);
			
			connection.setDoOutput(true);
			
			String jsonInputString = String.format("{'grant_type':'client_credentials', 'client_id': %s, 'client_secret': %s}", CLIENT_ID, CLIENT_SECRET);
			
			try(OutputStream os = connection.getOutputStream()) {
			    byte[] input = jsonInputString.getBytes("utf-8");
			    os.write(input, 0, input.length);           
			}
			
			connection.connect();
			
			int responseCode = connection.getResponseCode();
			
			System.out.println("Sending requets to url: " + url);
			System.out.println("Response code: " + responseCode);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
