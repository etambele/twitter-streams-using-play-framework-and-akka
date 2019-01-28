package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.NotUsed;
import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;
import play.libs.Json;
import twitter.SearchResults;
import twitter.Twitter;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

import static akka.pattern.PatternsCS.ask;
import java.util.stream.Collectors;
import akka.util.Timeout;

/**
 * This class makes a web socket connection with the actor which thereby allows the two way full Duplex communication.
 * The client can send messages and the server can receive messages at any time, as long as there is an active WebSocket connection between the server and the client.
 */
public class MyWebSocketActor extends AbstractActor {

	List<String> dummy = new ArrayList<>();
	JsonNode SampleJson = null;
	HashMap<ActorRef,String> map = new HashMap<>();
	List<SearchResults> res = new ArrayList<>();

	int count = 0;
	/**
	 * Creates a Websocket actor.
	 * @param out reference for actor to make connection.
	 * @return Connection for websocket actors.
	 */

	public static Props props(ActorRef out) {
		return Props.create(MyWebSocketActor.class, out);
	}

	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private final ActorRef out;
/**
	 * Constructor method which passes the Actor reference object as a parameter and adds dummy values to it in order to perform the actor test.
	 *@param out A reference object of an actor.
	 */
	public MyWebSocketActor(ActorRef out) {
		dummy.add("abc");
		dummy.add("bcd");
		SampleJson = Json.toJson(dummy);
		this.out = out;
	}
	/**
	 *  When a new UserActor is started, it has to register itself with the TimeActor. We do this by sending a RegisterMsg to the TimeActor.
	 */

	  @Override
	    public void preStart() {
	       	context().actorSelection("/user/TwitterResultActor/")
	                 .tell(new TwitterResultActor.RegisterMsg(), self());
	       	
	       	System.out.println("Started Server");
	    }
	   /**
	   * It handles messages from the TimeActor to push the new time information to the front-end through the WebSocket. For that, we need the message class.
	   * @author Sanghavi Kartigayan
	   *
	   */
	  static public class TimeMessage {
		  
	      public final String res;
	       public TimeMessage(String result) {
	    	   System.out.println("Before tell");
	           this.res = result;
	       }
	   }
	  /**
	   * The sendTime method creates a new JSON object with the time from the message and sends it through the WebSocket.
	   * @param msg That accepts time as a parameter for creating connection. 
	   */
 
	  
	   private void sendTime(TimeMessage msg) throws TwitterException {	  	
		  System.out.println("At tell");
		  System.out.println(msg.res.toString());//msg.res.toString());
		  gettweets(msg.res.toString());
		//  final ObjectNode response = Json.newObject();
	    //  response.put("response" , Json.toJson(msg.res));
		//  out.tell(response, self());
		  
	   }	
	   
	   
	   private void gettweets(String searchword) throws TwitterException {
       	Twitter tweet = new Twitter(searchword);
       	CompletionStage<QueryResult> SearchResults = tweet.get() ;		

       	res.clear();

       	SearchResults.thenAccept( r -> {
       		if (count == 0){
       			count++ ;
       			System.out.println("Am i called jsut once ??");
       			SearchResults newres = null;
       			int c =0 ;
       			if (c < 9){
       				for (Status s : r.getTweets()){   
       					c++;
       					String TweetHandle = "@" +s.getUser().getScreenName();
       					String Tweets = "\t" + s.getText();
       					String word = searchword;
       				    newres = new SearchResults(TweetHandle ,Tweets, word);
       				    
       					res.add(newres);   
       				}
       				sendtojs();
       			}
       		//	MyWebSocketActor.TimeMessage tMsg = new MyWebSocketActor.TimeMessage(res);
       		//	userActors.forEach(ar -> ar.tell(tMsg, self()));
       		}else{
       			System.out.println("Am i called at all ??");
       			Status s = r.getTweets().get(0);
       			String TweetHandle = "@" +s.getUser().getScreenName();
       			//System.out.println(TweetHandle);
       			String Tweets = "\t" + s.getText();        
       			String word = searchword;
       			SearchResults newres = new SearchResults(TweetHandle ,Tweets, word);
       			List<SearchResults> res_temp = new ArrayList<>();
       			res_temp.add(newres);
       			sendtojs_temp(res_temp);
       		//	MyWebSocketActor.TimeMessage tMsg = new MyWebSocketActor.TimeMessage(res_temp);
       		//	userActors.forEach(ar -> ar.tell(tMsg, self()));
       		}
       	}); 
	   }
	   /**
	   * Sends reponse to Json
		*
		*/
	   private void sendtojs() {
		
	   for (SearchResults s : res ){
			  System.out.println(s.getHandle());
			   final ObjectNode response = Json.newObject();
			  response.put("response" , Json.toJson(s));
			  out.tell(response, self()); 
		  }
	   }
	   /**
	   * puts result in response
	   */
	   private void sendtojs_temp(List<SearchResults> res_temp) {
		   for (SearchResults s : res_temp ){
				  System.out.println("How many times am i looping ?");
				  final ObjectNode response = Json.newObject();
				  response.put("response" , Json.toJson(s));
				  out.tell(response, self()); 
			  }
		   }
		     /** 
		  * This method builds the Receive by matching both methods properties.
		  * @return Connection
		  */
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(TimeMessage.class, this::sendTime).build();
	}
}