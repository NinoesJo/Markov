import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HashMarkov implements MarkovInterface {

    String[] myWords;		// Training text split into array of words 
	Random myRandom;		// Random number generator
	int myOrder;			// Length of WordGrams used
    protected static String END_OF_TEXT = "*** ERROR ***";
    HashMap<WordGram, List<String>> myMap;

    /**
	 * Initializes a model of given order and random number generator.
	 * @param order Number of words used to generate next 
	 * random word / size of WordGrams used.
	 */
	public HashMarkov(int order){
		myMap = new HashMap<>();
        myOrder = order;
		myRandom = new Random();
	}

    @Override
    public void setTraining(String text) {
        // TODO Auto-generated method stub (OK)
        //throw new UnsupportedOperationException("Unimplemented method 'setTraining'");
        myWords = text.split("\\s+");
        myMap.clear();
        int k = 0;
        WordGram wordGram = new WordGram(myWords, k, myOrder);

        while (k < myWords.length - myOrder) {
            myMap.putIfAbsent(wordGram, new ArrayList<String>());
            myMap.get(wordGram).add(myWords[k + myOrder]);
            wordGram = wordGram.shiftAdd(myWords[k + myOrder]);
            k++;
        }
    }

    @Override
    public List<String> getFollows(WordGram wgram) {
        // TODO Auto-generated method stub (OK)
        //throw new UnsupportedOperationException("Unimplemented method 'getFollows'");
        return myMap.get(wgram);
    }

    /**
	 * Returns a random word that follows kGram in the training text.
	 * In case no word follows kGram, returns END_OF_TEXT
	 * @param wgram is being searched for in training text. Typically
	 * the previous words of the randomly generated text, but could be
	 * an arbitrary WordGram.
	 * @return a random word among those that follow after kGram in 
	 * the training text, or END_OF_TEXT if none
	 */
	private String getNextWord(WordGram wgram) {
		List<String> follows = getFollows(wgram);
		if (follows == null || follows.isEmpty()) {
			return END_OF_TEXT;
		}
		else {
			int randomIndex = myRandom.nextInt(follows.size());
			return follows.get(randomIndex);
		}
	}

    @Override
    public String getRandomText(int length) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'getRandomText'");
        ArrayList<String> randomWords = new ArrayList<>(length);
		int index = myRandom.nextInt(myWords.length - myOrder + 1);
		WordGram current = new WordGram(myWords, index, myOrder);
		randomWords.add(current.toString());
        for(int k = 0; k < length - myOrder; k += 1) {
			String nextWord = getNextWord(current);
			if (nextWord.equals(END_OF_TEXT)) {
				break;
			}
			randomWords.add(nextWord);
			current = current.shiftAdd(nextWord);
		}
		return String.join(" ", randomWords);
    }

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub (OK)
        //throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
        return myOrder;
    }

    @Override
    public void setSeed(long seed) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'setSeed'");
        myRandom.setSeed(seed);
    }
    
}
