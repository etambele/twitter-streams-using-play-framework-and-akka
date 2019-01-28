package twitter;
/**
 * the SearchResults class searches for the tweets and respective twitter Handler from the twitter API.
 */

public class SearchResults {
	/**
	 * A Constructor method which assigns the tweets and the tweet handler references.
	 * @param handle Twitter handler.
	 * @param tweets Tweets for that particular search.
	 */
	public SearchResults(String handle, String tweets, String words) {
		super();
		this.handle = handle;
		this.tweets = tweets;
		this.words = words;
	}
	public String handle ;
	public String tweets ;
	public String words ;
	/**
	 * Method to fetch the tweet Handler.
	 * @return handle 
	 */
	public String getHandle() {
		return handle;
	}
	/**
	 * Method that sets the reference of the handle.
	 * @param handle
	 */
	public void setHandle(String handle) {
		this.handle = handle;
	}
	/**
	 * Method that fetches the tweets.
	 * @return tweets
	 */
	public String getTweets() {
		return tweets;
	}
	/**
	 * Method that sets the reference of Tweets.
	 * @param tweets
	 */

	public void setTweets(String tweets) {
		this.tweets = tweets;
	}
	/**
	* returns search keyword
	*@return keyword for search
	*/
	public String getWords() {
		return words;
	}
	/**
	* sets the search keyword
	*/
	public void setWords(String words) {
		this.words = words;
	}
	
}
