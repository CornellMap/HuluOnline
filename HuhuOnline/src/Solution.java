import org.json.JSONObject;
public class Solution {
    public Solution(){
    	
    	Guess guess = new Guess();
    	guess.newGuess();
    	while (true){
    		if (guess.prison.status.equals("ALIVE")) {
    			guess.doGuess();
    		} else {
    			guess.printRatio();
    			guess.newGuess();
    		}
    		
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    public static void main(String[] args) {
		Solution solution = new Solution();
	//	System.out.println(solution);
	}
}

