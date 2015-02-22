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
	
	// Common three-letter words
	String[] three = new String[]{
			"the","and","for","are","but","not","you","all",
			"any","can","her","was","one","our","out","day",
			"get","has","him","his","how","man","new","now",
			"old","see","two","way","who","boy","did","its",
			"let","put","say","she","too","use","dad","mom",
	};
	
	// Common four-letter words
	String[] four = new String[] {
			"bade", "bore", "silt", "swag", "fund", "pore", "whir", "char",
			"bail", "born", "sing", "swan", "funk", "pork", "whit", "chat",
			"bait", "bout", "sink", "swat", "furl", "port", "whiz", "chid",
			"bake", "bulk", "sire", "sway", "fury", "pour", "wide", "chin",
			"bald", "bung", "site", "swig", "fuze", "pout", "wild", "chit",
			"bale", "bunk", "size", "fade", "page", "prey", "wile", "chon",
			"balk", "bunt", "smog", "fail", "paid", "puke", "wilt", "chug",
			"band", "burn", "smug", "fain", "pail", "pule", "wily", "cite",
			"bane", "bury", "smut", "fair", "pain", "punk", "wind", "city",
			"bang", "sage", "soak", "fake", "pair", "punt", "wine", "coal",
			"bank", "said", "soar", "fane", "pale", "puny", "wing", "coat",
			"bard", "sail", "soil", "fang", "pane", "pure", "wink", "coax",
			"bare", "sake", "sold", "fare", "pang", "purl", "wire", "code",
			"bark", "sale", "sale", "fate", "pant", "wade", "wiry", "coil",
			"barn", "salt", "song", "faun", "pard", "wage", "woad", "coin",
			"bate", "sand", "sore", "faze", "pare", "wail", "woke", "coke",
			"bide", "sane", "sort", "file", "park", "wain", "wold", "cold",
			"bike", "sang", "soul", "find", "part", "wait", "wont", "colt",
			"bile", "sank", "sour", "fine", "pate", "wake", "word", "cone",
			"bilk", "sate", "span", "fire", "pave", "wale", "wore", "conk",
			"bind", "save", "spar", "flex", "phiz", "walk", "work", "cony",
			"bird", "scan", "spat", "foal", "pike", "wand", "worn", "cord",
			"bite", "scar", "spay", "fogy", "pile", "wane", "wort", "core",
			"boar", "Scot", "spin", "foil", "pine", "want", "wove", "cork",
			"boat", "scud", "spit", "fold", "ping", "ward", "cage", "corn",
			"bode", "shad", "spot", "folk", "pink", "ware", "cagy", "cote",
			"body", "shag", "spry", "fond", "pint", "warn", "cake", "cove",
			"bogy", "shin", "spud", "font", "pity", "wart", "calk", "cozy",
			"boil", "shod", "spun", "ford", "pixy", "wary", "cane", "cult",
			"bold", "shoe", "spur", "fore", "poke", "wave", "cant", "curd",
			"bole", "shot", "suit", "fork", "poky", "wavy", "card", "cure",
			"bolt", "shun", "sulk", "fort", "pole", "waxy", "care", "curl",
			"bond", "shut", "sung", "foul", "pond", "what", "cart", "curt",
			"bone", "side", "sunk", "four", "pone", "whey", "cave", "cute",
			"bony", "silk", "sure", "foxy", "pony", "Whig", "cavy", "hail",
			"hair", "hazy", "hove", "July", "mane", "mind", "moly", "aide",
			"hake", "hide", "huge", "June", "Manx", "mine", "monk", "airy",
			"hale", "hike", "hulk", "junk", "many", "mink", "more", "ante",
			"halt", "hilt", "hung", "jury", "mare", "mint", "morn", "arty",
			"hand", "hind", "hunk", "jute", "mark", "minx", "mote", "auld",
			"hang", "hint", "hunt", "quid", "marl", "mire", "moue", "aunt",
			"hank", "hire", "hurl", "quit", "mart", "miry", "move", "avid",
			"hard", "hoax", "hurt", "quiz", "mate", "mite", "mule", "vide",
			"hare", "hold", "jade", "made", "maul", "moan", "murk", "vile",
			"hark", "hole", "jail", "maid", "maze", "moat", "mute", "vine",
			"hart", "holy", "jilt", "mail", "mazy", "mode", "ogle", "ugly",
			"hate", "hone", "jinx", "main", "mike", "moil", "oily", "urge",
			"haul", "honk", "join", "make", "mild", "mold", "only", "inky",
			"have", "horn", "joke", "male", "mile", "mole", "orgy",
			"haze", "hour", "jolt", "malt", "milk", "molt", "adze",
	};
	
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
		//System.out.println("My Guess: " + ch);
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
		ArrayList<Character> possible = new ArrayList<Character>();
		for (int i = 0; i < commonBiLetterWords.length; i++) {
			String word = commonBiLetterWords[i];
			if (pattern.charAt(0) == '_' && !guessed.contains(word.charAt(0)) && word.charAt(1) == pattern.charAt(1)) possible.add(word.charAt(0));
			if (pattern.charAt(1) == '_' && !guessed.contains(word.charAt(1)) && word.charAt(0) == pattern.charAt(0)) possible.add(word.charAt(1));
		}
		if (possible.size() > 0 && possible.size() < 3) return possible.get(0);
		return null;
	}
	
	public Character threeGuesser(String pattern) {
		int count = 0;
		if (pattern.charAt(0) == '_') count++;
		if (pattern.charAt(1) == '_') count++;
		if (pattern.charAt(2) == '_') count++;
		
		if (count != 1) return null;
		ArrayList<Character> possible = new ArrayList<Character>();
		for (int i = 0; i < three.length; i++) {
			String word = three[i];
			if (pattern.charAt(0) == '_' && !guessed.contains(word.charAt(0)) && word.charAt(1) == pattern.charAt(1) && word.charAt(2) == pattern.charAt(2)) possible.add(word.charAt(0));
			if (pattern.charAt(1) == '_' && !guessed.contains(word.charAt(1)) && word.charAt(0) == pattern.charAt(0) && word.charAt(2) == pattern.charAt(2)) possible.add(word.charAt(1));
			if (pattern.charAt(2) == '_' && !guessed.contains(word.charAt(2)) && word.charAt(1) == pattern.charAt(1) && word.charAt(0) == pattern.charAt(0)) possible.add(word.charAt(2));
		}
		if (possible.size() > 0 && possible.size() < 3) return possible.get(0);
		return null;
	}
	
	public Character fourGuesser(String pattern) {
		int count = 0;
		if (pattern.charAt(0) == '_') count++;
		if (pattern.charAt(1) == '_') count++;
		if (pattern.charAt(2) == '_') count++;
		if (pattern.charAt(3) == '_') count++;
		
		if (count != 1) return null;
		ArrayList<Character> possible = new ArrayList<Character>();
		for (int i = 0; i < four.length; i++) {
			String word = four[i];
			if (pattern.charAt(0) == '_' && !guessed.contains(word.charAt(0)) && word.charAt(1) == pattern.charAt(1) && word.charAt(2) == pattern.charAt(2) && word.charAt(3) == pattern.charAt(3)) possible.add(word.charAt(0));
			if (pattern.charAt(1) == '_' && !guessed.contains(word.charAt(1)) && word.charAt(0) == pattern.charAt(0) && word.charAt(2) == pattern.charAt(2) && word.charAt(3) == pattern.charAt(3)) possible.add(word.charAt(1));
			if (pattern.charAt(2) == '_' && !guessed.contains(word.charAt(2)) && word.charAt(1) == pattern.charAt(1) && word.charAt(0) == pattern.charAt(0) && word.charAt(3) == pattern.charAt(3)) possible.add(word.charAt(2));
			if (pattern.charAt(3) == '_' && !guessed.contains(word.charAt(3)) && word.charAt(0) == pattern.charAt(0) && word.charAt(1) == pattern.charAt(1) && word.charAt(2) == pattern.charAt(2)) possible.add(word.charAt(3));
		}
		if (possible.size() > 0 && possible.size() <= 3) return possible.get(0);
		return null;
	}
	
	public void doGuess() {
		// Judge if only one possible
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
		boolean solo = false;
		if (minPossible == 1) {
			solo = true;
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
			
			//System.out.println(guessPattern);
			
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
		}
		
		if (!solo) {
			boolean twoguesser = false;
			for (int i = 0; i < prison.state.size(); i++) {
				String pattern = prison.state.get(i);
				if (pattern.length() == 2) {
					Character ch = twoGuesser(pattern);
					//System.out.println(pattern + " " + ch);
					if (ch != null) {
						twoguesser = true;
						guessChar(ch.charValue());
						break;
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
							break;
						}
					}
				}
			}
			
			boolean fourguesser = false;
			if (!twoguesser && !threeguesser) {
				for (int i = 0; i < prison.state.size(); i++) {
					String pattern = prison.state.get(i);
					if (pattern.length() == 4) {
						Character ch = fourGuesser(pattern);
						if (ch != null) {
							fourguesser = true;
							guessChar(ch.charValue());
							break;
						}
					}
				}
			}
			
			//System.out.println("This round: " + twoguesser + " " + threeguesser);
	
			if (!twoguesser && !threeguesser && !fourguesser) {
				if (guessed.size() < 3) {
					guessChar(nextFrequency());
				} else {
					guessPattern = new ArrayList<ArrayList<String>>();
					minPossible = 1 << 20;
					for (String pattern : prison.state) {
						ArrayList<String> possible = getPossible(pattern);
						guessPattern.add(possible);
						minPossible = Math
								.min(minPossible,
										possible.size() > 0 ? possible.size()
												: minPossible);
					}
//					/*if (minPossible > 10) {
//						// Too ambiguious to be determined
//						int[] occurence = new int[26];
//						for (int i = 0; i < 26; i++)
//							occurence[i] = 0;
//	
//						for (int i = 0; i < guessPattern.size(); i++) {
//							for (int k = 0; k < guessPattern.get(i).size(); k++) {
//								String word = guessPattern.get(i).get(k);
//								for (int j = 0; j < word.length(); j++) {
//									if (word.charAt(j) != '_') {
//										occurence[word.charAt(j) - 'a']++;
//									}
//								}
//							}
//						}
//	
//						double maxOccur = 0;
//						char c = '$';
//						for (int i = 0; i < 26; i++) {
//							if (occurence[i] * Dictionary.frequencyNumber[i] > maxOccur) {
//								maxOccur = occurence[i]
//										* Dictionary.frequencyNumber[i];
//								c = (char) (i + 'a');
//							}
//						}
//						if (c != '$') {
//							guessChar(c);
//						} else {
//							guessChar(nextFrequency());
//						}
//					} else */
//					if (minPossible == 1) {
//						// Determined by majority occurence of solo possible
//						int[] occurence = new int[26];
//						for (int i = 0; i < 26; i++)
//							occurence[i] = 0;
//	
//						for (int i = 0; i < guessPattern.size(); i++) {
//							if (guessPattern.get(i).size() == 1) {
//								String word = guessPattern.get(i).get(0);
//								for (int j = 0; j < word.length(); j++) {
//									if (word.charAt(j) != '_') {
//										occurence[word.charAt(j) - 'a']++;
//									}
//								}
//							}
//						}
//	
//						int maxOccur = 0;
//						char c = '$';
//						for (int i = 0; i < 26; i++) {
//							if (occurence[i] > maxOccur) {
//								maxOccur = occurence[i];
//								c = (char) (i + 'a');
//							}
//						}
//						if (c != '$') {
//							guessChar(c);
//						} else {
//							guessChar(nextFrequency());
//						}
//					} else 
					{
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
		}

		
		System.out.println("Prison: " + prison.token + " Status: " + prison.status +" Now State: " + prison.state);
		if (prison.status.equals("FREE") && nowTmpToken.equals(prison.token)) {
			success++;
			nowTmpToken = "";
		}
		if (prison.status.equals("DEAD") && nowTmpToken.equals(prison.token)) {
			fail++;
			nowTmpToken = "";
		}
	}
	
	public void resetRatio() {
		success = fail = 0;
	}
	
	public void printRatio() {
		if (success + fail > 0) {
			System.out.println("Current Success Ratio: " + success / (double)(success + fail));
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
		if (pattern.indexOf('_') == -1) {
			return result;
		}
		for (String word : dict.wordList) {
			if (word.length() == pattern.length()/* || 
				(word.length() == pattern.length() - 1 && endMatch(pattern, "s")) ||
				(word.length() == pattern.length() - 2 && endMatch(pattern, "es"))*/
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
		
		guess.newGuess();

		guess.prison.state = new ArrayList<String>();
		//, _art
		guess.prison.state.add("_art");
		guess.prison.state.add("of");
		guess.prison.state.add("the");
		guess.prison.state.add("answer");
		
		guess.doGuess();
		
		//guess.newGuess();
		//guess.doGuess();
		guess.guessed.add('t');
		guess.guessed.add('e');
		guess.guessed.add('a');

		
		ArrayList<String> result = guess.getPossible("oa_");
		for (String word : result) {
			System.out.println(word);
		}
		
	}
}
