import static org.junit.Assert.*;

import org.junit.Test;

import actors.TwitterResultActor;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static akka.pattern.PatternsCS.ask;

import org.hibernate.validator.constraints.Mod10Check;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import akka.testkit.javadsl.TestKit;
import twitter.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import controllers.HomeController;
import play.api.test.Helpers;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Content;
import twitter4j.TwitterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import static org.mockito.Mockito.*;


public class Twitteractortesting {
	/**
	  * @author Misbahuddin Adil Syed
	  */
  static ActorSystem system;
  
  public static play.mvc.Http.Request request ;
  /**
   * Creates an Actor System
   */
  @BeforeClass
  public static void setup() {
      system = ActorSystem.create();
  }
  /**
   * Shuts down the created Actor System.
   */
  @AfterClass
  public static void teardown() {
      TestKit.shutdownActorSystem(system);
      system = null;
  }
  /**
   * Tests the Index page
   */

  @Test
  public void testHelloActor() throws Exception {
	  final TestKit testProbe = new TestKit(system);    
	  final ActorRef tweetactor =  system.actorOf(TwitterResultActor.props("") , "TwitterResultActor");	  
	  CompletableFuture<Object> s1 =  ask(tweetactor, "say42", 1000).toCompletableFuture();
	  assertThat(s1.get().toString().contains("42"));
  }
}