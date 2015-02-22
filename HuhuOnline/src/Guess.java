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
	// Frequency position;
	int freqPosition = 0;
	
	// Common bi-letter words
	String[] commonBiLetterWords = new String[]{
	    "an", "as", "at", "am", "be", "by", "do",
		"go", "he", "hi", "is", "it", "if", "in",
		"my", "no", "of", "on", "or", "oh", 
		"so", "to", "up", "us", "we,"
	};
	
	// and & the
	String[] three = new String[]{"and", "the"};
	
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
	
	public Character twoGuesser(String pattern) {
		int count = 0;
		if (pattern.charAt(0) == '_') count++;
		if (pattern.charAt(1) == '_') count++;
		if (count != 1) return null;
		for (int i = 0; i < commonBiLetterWords.length; i++) {
			String word = commonBiLetterWords[i];
			if (pattern.charAt(0) == '_' && !guessed.contains(word.charAt(0))) return word.charAt(0);
			if (pattern.charAt(1) == '_' && !guessed.contains(word.charAt(1))) return word.charAt(1);
		}
		return null;
	}
	
	public Character threeGuesser(String pattern) {
		int count = 0;
		if (pattern.charAt(0) == '_') count++;
		if (pattern.charAt(1) == '_') count++;
		if (pattern.charAt(2) == '_') count++;
		
		if (count != 1) return null;
		for (int i = 0; i < three.length; i++) {
			String word = three[i];
			if (pattern.charAt(0) == '_' && !guessed.contains(word.charAt(0))) return word.charAt(0);
			if (pattern.charAt(1) == '_' && !guessed.contains(word.charAt(1))) return word.charAt(1);
			if (pattern.charAt(2) == '_' && !guessed.contains(word.charAt(2))) return word.charAt(2);
		}
		return null;
	}
	
	public void doGuess() {
		boolean twoguesser = false;
		for (int i = 0; i < prison.state.size(); i++) {
			String pattern = prison.state.get(i);
			if (pattern.length() == 2) {
				Character ch = twoGuesser(pattern);
				if (ch != null) {
					twoguesser = true;
					guessChar(ch.charValue());
				}
			}
		}

		boolean threeguesser = false;
		if (!twoguesser) {
			for (int i = 0; i < prison.state.size(); i++) {
				String pattern = prison.state.get(i);
				if (pattern.length() == 3) {
					Character ch = threeGuesser(pattern);
					if (ch != null) {
						threeguesser = true;
						guessChar(ch.charValue());
					}
				}
			}
		}

		if (!twoguesser && !threeguesser) {
			if (guessed.size() < 3) {
				guessChar(nextFrequency());
			} else {
				ArrayList<ArrayList<String>> guessPattern = new ArrayList<ArrayList<String>>();
				int minPossible = 1 << 20;
				for (String pattern : prison.state) {
					ArrayList<String> possible = getPossible(pattern);
					guessPattern.add(possible);
					minPossible = Math
							.min(minPossible,
									possible.size() > 0 ? possible.size()
											: minPossible);
				}
				/*if (minPossible > 10) {
					// Too ambiguious to be determined
					int[] occurence = new int[26];
					for (int i = 0; i < 26; i++)
						occurence[i] = 0;

					for (int i = 0; i < guessPattern.size(); i++) {
						for (int k = 0; k < guessPattern.get(i).size(); k++) {
							String word = guessPattern.get(i).get(k);
							for (int j = 0; j < word.length(); j++) {
								if (word.charAt(j) != '_') {
									occurence[word.charAt(j) - 'a']++;
								}
							}
						}
					}

					double maxOccur = 0;
					char c = '$';
					for (int i = 0; i < 26; i++) {
						if (occurence[i] * Dictionary.frequencyNumber[i] > maxOccur) {
							maxOccur = occurence[i]
									* Dictionary.frequencyNumber[i];
							c = (char) (i + 'a');
						}
					}
					if (c != '$') {
						guessChar(c);
					} else {
						guessChar(nextFrequency());
					}
				} else */
				if (minPossible == 1) {
					// Determined by majority occurence of solo possible
					int[] occurence = new int[26];
					for (int i = 0; i < 26; i++)
						occurence[i] = 0;

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
							c = (char) (i + 'a');
						}
					}
					if (c != '$') {
						guessChar(c);
					} else {
						guessChar(nextFrequency());
					}
				} else {
					// majority vote, then scale by letter frequency
					int[] occurence = new int[26];
					for (int i = 0; i < 26; i++)
						occurence[i] = 0;

					for (int i = 0; i < guessPattern.size(); i++) {
						//if (guessPattern.get(i).size() < 10) {
							for (int k = 0; k < guessPattern.get(i).size(); k++) {
								String word = guessPattern.get(i).get(k);
								for (int j = 0; j < word.length(); j++) {
									if (word.charAt(j) != '_') {
										occurence[word.charAt(j) - 'a']++;
									}
								}
							}
						//}
					}

					double maxOccur = 0;
					char c = '$';
					for (int i = 0; i < 26; i++) {
						if (occurence[i] * Dictionary.frequencyNumber[i] > maxOccur) {
							maxOccur = occurence[i]
									* Dictionary.frequencyNumber[i];
							c = (char) (i + 'a');
						}
					}
					if (c != '$') {
						guessChar(c);
					} else {
						guessChar(nextFrequency());
					}
				}
			}
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
	
	private boolean endMatch(String ori, String pattern) {
		if (pattern.length() > ori.length()) return false;
		for (int i = 0; i < pattern.length(); i++) {
			if (ori.charAt(ori.length() - pattern.length() + i) != pattern.charAt(i) &&
				pattern.charAt(i) != '_') {
				return false;
			}
		}
		return true;
	}
	private ArrayList<String> getPossible(String pattern) {
		ArrayList<String> result = new ArrayList<String>();
		for (String word : dict.wordList) {
			if (word.length() == pattern.length() || 
				(word.length() == pattern.length() - 1 && endMatch(pattern, "s")) ||
				(word.length() == pattern.length() - 2 && endMatch(pattern, "es"))
				) {
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
		Guess guess = new Guess();
		//guess.newGuess();
		//guess.doGuess();
//		guess.guessed.add('t');
//		guess.guessed.add('e');
//		guess.guessed.add('a');
//		
		ArrayList<String> result = guess.getPossible("_o_got");
		for (String word : result) {
			System.out.println(word);
		}
		
	}
}
