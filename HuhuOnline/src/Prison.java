import java.util.ArrayList;
import java.net.*;
import java.io.*;

import org.json.JSONObject;

public class Prison {
	public String status;
	public String token;
	public int remainingGuesses;
	public ArrayList<String> state = new ArrayList<String>();

	public Prison(JSONObject obj) {
		updatePrison(obj);
	}
	
	public void updatePrison(JSONObject obj) {
		status = obj.get("status").toString();
		token = obj.get("token").toString();
		remainingGuesses = (Integer) obj.get("remaining_guesses");
		String states = obj.get("state").toString();
		String[] statesArray = states.split(" ");
		
		state.clear();
		for (String s : statesArray) {
			s = s.toLowerCase();
			for (int i = 0; i < s.length(); i++) {
				if (!(s.charAt(i) == '_' || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z'))) {
					s = s.subSequence(0, i) + s.substring(i + 1);
					i--;
				}
			}
			state.add(s);
		}
	}

	public static void main(String[] args) {
		JSONObject obj = RestClient.getContent("http://gallows.hulu.com/play?code=xiamin1991@gmail.com");
		Prison p1 = new Prison(obj);
		System.out.println(p1.status);
		System.out.println(p1.token);
		System.out.println(p1.remainingGuesses);
		System.out.println(p1.state);
	}
}
