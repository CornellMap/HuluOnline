import java.net.*;

import java.io.*;

import org.json.JSONObject;

public class RestClient {
	public static JSONObject getContent(String url) {
		URL conn;
		try {
			conn = new URL(url);
			URLConnection yc = conn.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	        String inputLine;
	        String result = "";
	        while ((inputLine = in.readLine()) != null) 
	            result += inputLine;
	        in.close();
	        return new JSONObject(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
    public static void main(String[] args) throws Exception {
//        JSONObject obj = RestClient.getContent("http://gallows.hulu.com/play?code=xiamin1991@gmail.com");
//        System.out.println(obj.get("token"));
//        System.out.println(obj.get("state"));
//        
//    	http://gallows.hulu.com/play?code=xiamin1991@gmail.com&token=8280899&guess=e
    }
}