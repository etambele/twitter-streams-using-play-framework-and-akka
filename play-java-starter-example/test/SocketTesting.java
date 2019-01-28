import org.junit.Test;
import play.test.TestServer;
import play.test.WSTestClient;
import static play.mvc.Http.Status.OK;

import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.BoundRequestBuilder;
import play.shaded.ahc.org.asynchttpclient.ListenableFuture;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketListener;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketTextListener;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.slf4j.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import static org.awaitility.Awaitility.*;
import static org.junit.Assert.*;

public class SocketTesting {
	@Test
	public void testInServer() throws Exception {
	    TestServer server = testServer(9000);
	    running(server, () -> {
	        try (WSClient ws = WSTestClient.newClient(9000)) {
	            CompletionStage<WSResponse> completionStage = ws.url("/script").get();
	            WSResponse response = completionStage.toCompletableFuture().get();
	            assertEquals(OK, response.getStatus());
	        } catch (Exception e) {
	            System.out.println(e);
	        }
	    });
	}
}