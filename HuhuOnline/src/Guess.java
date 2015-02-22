import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;


public class Guess {
	// Dictionary
	Dictionary dict = new Dictionary();
	// Guessed Characters
	Set<Character> guessed = new HashSet<Character>();
	// Prison
	Prison prison = null;
	
	public void newGuess() {
		JSONObject obj = RestClient.getContent("http://gallows.hulu.com/play?code=xiamin1991@gmail.com");
		guessed.clear();
		prison = new Prison(obj);
	}
	
	public void guessChar(char ch) {
		JSONObject obj = RestClient.getContent(
			"http://gallows.hulu.com/play?code=xiamin1991@gmail.com&token=" + prison.token + "&guess=" + ch
		);
		prison.updatePrison(obj);
	}
	
	public void doGuess() {
		prison.remainingGuesses--;
		if (prison.remainingGuesses == 0) prison.status = "DEAD";
		if (Math.random() < 0.1) prison.status = "FREE";
		System.out.println("Prison: " + prison.token + " Status: " + prison.status +" Now State: " + prison.state);
	}
	
	private ArrayList<String> getPossible(String pattern) {
		ArrayList<String> result = new ArrayList<String>();
		for (String word : dict.wordList) {
			if (word.length() == pattern.length()) {
				boolean valid = true;
				for (int i = 0; i < word.length(); i++) {
					if (pattern.charAt(i) != '_' && pattern.charAt(i) != word.charAt(i)) {
						valid = false;
						break;
					}
					if (pattern.charAt(i) == '_' && guessed.contains(word.charAt(i))) {
						valid = false;
						break;
					}
				}
				if (valid) {
					System.out.println(word);
					StringBuffer newWord = new StringBuffer("");
					for (int i = 0; i < word.length(); i++) {
						if (pattern.charAt(i) == '_') newWord.append(word.charAt(i));
						else newWord.append('_');
					}
					result.add(newWord.toString());
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		Guess guess = new Guess();
		guess.newGuess();
		guess.doGuess();
//		guess.guessed.add('r');
//		ArrayList<String> result = guess.getPossible("_r_");
//		for (String word : result) {
//			System.out.println(word);
//		}
		
	}
}
