import static org.junit.Assert.*;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;

import org.hibernate.validator.constraints.Mod10Check;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import akka.testkit.javadsl.TestKit;
import twitter.*;
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
public class ScriptViewTesting {
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
  @Before
  public void setUp() throws Exception {
      Http.Context context = mock(Http.Context.class);
      Http.Flash flash = mock(Http.Flash.class);
      when(context.flash()).thenReturn(flash);
      Http.Context.current.set(context);
  }
 // assertThat(service.addCustomer(customer), is(notNullValue()));
 /**
 * tests that scripts have expected tags
 */
  @Test 
  public  void testprofile () throws TwitterException, InterruptedException ,ExecutionException
  {
	   Result result = new HomeController(system).scripts();
	   assertEquals(OK, result.status());
	   assertEquals("text/html", result.contentType().get());
	   assertEquals("utf-8", result.charset().get());
	   assertThat(contentAsString(result).contains("tweet"));
  }
  

}
