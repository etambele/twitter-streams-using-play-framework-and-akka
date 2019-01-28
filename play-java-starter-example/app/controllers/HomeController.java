package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;
import actors.MyWebSocketActor;
import actors.TwitterResultActor;
import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ws.TextMessage;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.scaladsl.Keep;
import play.mvc.*;
import play.mvc.Http.RequestHeader;
import scala.compat.java8.FutureConverters;
import services.Counter;
import twitter.Twitter;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import views.html.*;
import views.*;

import twitter4j.Query;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import play.libs.streams.ActorFlow;
import play.http.websocket.Message;
import play.libs.concurrent.HttpExecutionContext;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	
	public static play.mvc.Http.Request request ;
    
    @Inject
    private ActorSystem actorSystem;
    @Inject
    private Materializer materializer;
  /**
     * Constructor method which creates the actor system for tweets and tweet handler.
     * @param system An actor system that handles the searches.
     */
    @Inject public HomeController(ActorSystem system) {
        system.actorOf(TwitterResultActor.props("") , "TwitterResultActor");
        }
  /**
     * This method produces an index page in a HTML form.
     * @return A HTML page that tells our application is ready to use.
     */
    public Result index() {
    	
        return ok(index.render("Your new application is ready."));
    }
     /**
     * This method generates the JavaScript for webSocket test.
     * @return A HTML page that generates script.
     */
    public Result scripts() {
    	request = request();
        return ok(script.render(request()));
    }
    /**
     * This method directs us to the mainview page of our application that enables us to perform a search on a keyword.
     * @return The Search HTML page.
     */
 
    public Result Search() {   		
        return ok(mainview.render("Tweet Analytics")); 
       
    }
	/**
     * This method is called when the search is made on the person who made the tweet.
     * It contains an ArrayList that store the Person's information such as the no of Followers, no.of friend etc. 
     * and another List that contains the tweets that is made on the Application.
     * @param name The name of the person who has made that particular tweet.
     * @return A profile HTML page of the person.
     * @throws TwitterException An exception class that will be thrown when TwitterAPI calls are failed. In case the Twitter server returned HTTP error code, you can get the HTTP status code using getStatusCode() method.
     * @throws InterruptedException It is thrown when the thread is waiting or sleeping and another Thread interrupts it.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception.
     */
  public Result profile(String name) throws TwitterException ,InterruptedException ,ExecutionException{    	
    	
	    Twitter tweet = new Twitter(name);
	    
  	    CompletableFuture<String> profileInfo = (CompletableFuture<String>) tweet.getProfile();
  	    CompletableFuture<QueryResult> FutureResult = (CompletableFuture<QueryResult>) tweet.get();
  	    
  	    Thread.sleep(1000);
     	String Profile = getProfile(name);
    	List<String> Tweets = getDetails(name);
    	
    	System.out.println(Tweets.toString());  
    	
		return ok(profile.render(Profile, Tweets));
	}
/**
   * This method is called when the ArrayList that contains the person's followers count, friend count, location, description and the screen name has to be obtained.
   * @param Key The name of the person who made the tweet.
   * @return the person's basic profile information.
   * @throws TwitterException An exception class that will be thrown when TwitterAPI calls are failed. In case the Twitter server returned HTTP error code, you can get the HTTP status code using getStatusCode() method.
   * @throws InterruptedException It is thrown when the thread is waiting or sleeping and another Thread interrupts it.
   */
    public String getProfile (String Key)throws TwitterException, InterruptedException  {
    	Twitter tweet = new Twitter(Key);
    	
    	CompletableFuture<String> profileInfo = (CompletableFuture<String>) tweet.getProfile();
    	
    	
		try {
			return profileInfo.get() ;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		return null;
    }
     /**
     * This method is called when the ArrayList that contains all the tweets the person has made on the application has to be retrieved.
     * @param Key The name of the person who has made the tweets.
     * @return The Detailed tweets that the person has made so far on the application.
     * @throws TwitterException An exception class that will be thrown when TwitterAPI calls are failed. In case the Twitter server returned HTTP error code, you can get the HTTP status code using getStatusCode() method.
     * @throws InterruptedException It is thrown when the thread is waiting or sleeping and another Thread interrupts it.
     */
    public List<String> getDetails (String Key)throws TwitterException, InterruptedException  {
    	
    	Twitter tweet = new Twitter(Key);
    	CompletableFuture<List<Status>> FutureList = (CompletableFuture<List<Status>>) tweet.getDetails();
    	List<String> res = new ArrayList<>();
    	
    	FutureList.thenAccept(s ->  s.stream()
    			                    .map(f -> f.getText())
    			                    .limit(10)
    			                    .forEach(res::add));
    	return res;
    	
     }
    /**
     * This method accepts Web Socket requests and creates the connection.
     * @return Connection to perform the searches using actors.
     */
    public WebSocket socket() {
        return  WebSocket.Json.accept(request ->
                ActorFlow.actorRef(MyWebSocketActor::props,
                        actorSystem, materializer));
    }
    


}
