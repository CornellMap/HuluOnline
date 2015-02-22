import java.util.ArrayList;
import java.util.HashMap;
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
	// Frequency position;
	int freqPosition = 0;
	
	String nowTmpToken = "";
	int success = 0, fail = 0;
	
	public void newGuess() {
		JSONObject obj = RestClient.getContent("http://gallows.hulu.com/play?code=xiamin1991@gmail.com");
		guessed.clear();
		prison = new Prison(obj);
		nowTmpToken = prison.token;
		freqPosition = 0;
	}
	
	public void guessChar(char ch) {
		JSONObject obj = RestClient.getContent(
			"http://gallows.hulu.com/play?code=xiamin1991@gmail.com&token=" + prison.token + "&guess=" + ch
		);
		prison.updatePrison(obj);
		guessed.add(ch);
	}
	
	public char nextFrequency() {
		while (freqPosition < Dictionary.frequency.length && guessed.contains(Dictionary.frequency[freqPosition])) {
			freqPosition++;
		}
		if (freqPosition == Dictionary.frequency.length) return '$';
		return Dictionary.frequency[freqPosition];
	}
	
	public void doGuess() {
		if (guessed.size() < 3) {
			guessChar(nextFrequency());
		} else {
			ArrayList<ArrayList<String>> guessPattern = new ArrayList<ArrayList<String>>();
			int minPossible = 1 << 20;
			for (String pattern : prison.state) {
				ArrayList<String> possible = getPossible(pattern);
				guessPattern.add(possible);
				minPossible = Math.min(minPossible, possible.size() > 0 ? possible.size() : minPossible);
			}
			if (minPossible > 10) {
				// Too ambiguious to be determined
				guessChar(nextFrequency());
			} else if (minPossible == 1){
				// Determined by majority occurence of solo possible
				int[] occurence = new int[26];
				for (int i = 0; i < 26; i++) occurence[i] = 0;
				
				for (int i = 0; i < guessPattern.size(); i++) {
					if (guessPattern.get(i).size() == 1) {
						String word = guessPattern.get(i).get(0);
						for (int j = 0; j < word.length(); j++) {
							if (word.charAt(j) != '_') {
								occurence[word.charAt(j) - 'a']++;
							}
						}
					}
				}
				
				int maxOccur = 0;
				char c = '$';
				for (int i = 0; i < 26; i++) {
					if (occurence[i] > maxOccur) {
						maxOccur = occurence[i];
						c = (char)(i + 'a');
					}
				}
				if (c != '$') {
					guessChar(c);
				} else {
					guessChar(nextFrequency());
				}
			} else {
				// majority vote, then scale by letter frequency
				
			}
			System.out.println(guessPattern);
		}
		
		System.out.println("Prison: " + prison.token + " Status: " + prison.status +" Now State: " + prison.state);
		if (prison.status == "FREE" && nowTmpToken == prison.token) {
			success++;
			nowTmpToken = "";
		}
		if (prison.status == "DEAD" && nowTmpToken == prison.token) {
			fail++;
			nowTmpToken = "";
		}
	}
	
	public void resetRatio() {
		success = fail = 0;
	}
	
	public void printRatio() {
		if (success + fail > 0) {
			System.out.println("Success Ratio: " + success / (double)(success + fail));
		}
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
//		Guess guess = new Guess();
//		//guess.newGuess();
//		//guess.doGuess();
//		guess.guessed.add('t');
//		guess.guessed.add('e');
//		guess.guessed.add('a');
//		
//		ArrayList<String> result = guess.getPossible("t_e_");
//		for (String word : result) {
//			System.out.println(word);
//		}
//		
	}
}
