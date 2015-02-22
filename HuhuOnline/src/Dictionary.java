import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
/* Use the English letter frequency(http://www.math.cornell.edu/~mec/2003-2004/cryptography/subs/frequencies.html)
 * Guess from the high frequency letter to low frequency letter every time.
 * */
public class Dictionary {
	public static char[] frequency = new char[]{
		'e','t','a','o','i','n','s',
		'h','r','d','l','u','c','m',
		'w','f','g','y','p','b','v',
		'k','j','x','q','z'
	};
	
	Set<String> wordList = new HashSet<String>();
	
/*Add the dictionaries downloaded from the internet */	
	public Dictionary() {
		String[] wordListFiles = new String[]{
				"./data/adj.txt", 
				"./data/adv.txt", 
				"./data/noun.txt", 
				"./data/verb.txt", 
				"./data/namelist.txt", 
				"./data/placename.txt", 
				"./data/sense.txt",
			};
		Scanner s = null;
		for (String fileName : wordListFiles) {
			try {
	            s = new Scanner(new BufferedReader(new FileReader(fileName)));
	            while (s.hasNext()) {
	                wordList.add(s.next());
	            }
	        } catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
	            if (s != null) {
	                s.close();
	            }
	        }
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(new Dictionary().wordList.size());
	}
}
