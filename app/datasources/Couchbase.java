package datasources;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import play.*;
import com.couchbase.client.CouchbaseClient;

/**
 * The `Couchbase` class acts a simple connection manager for the `CouchbaseClient`
 * and makes sure that only one connection is alive throughout the application.
 *
 * You may want to extend and harden this implementation in a production environment.
 */
public final class Couchbase {

	/**
	 * Holds the actual `CouchbaseClient`.
	 */
	private static CouchbaseClient client = null;

	/**
	 * Connects to Couchbase based on the configuration settings.
	 *
	 * If the database is not reachable, an error message is written and the
	 * application exits.
	 */
	public static boolean connect() {
		String hostname = Play.application().configuration().getString("couchbase.hostname");
		String port = Play.application().configuration().getString("couchbase.port");
		String bucket = Play.application().configuration().getString("couchbase.bucket");
		String password = Play.application().configuration().getString("couchbase.password");

		List<URI> uris = new LinkedList<URI>();
		uris.add(URI.create("http://"+hostname+":"+port+"/pools"));


		try {
			client = new CouchbaseClient(uris, bucket, password);
		} catch(IOException e) {
			Logger.error("Error connection to Couchbase: " + e.getMessage());
			System.exit(0);
		}

		return true;
	}

	/**
	 * Disconnect from Couchbase.
	 */
	public static boolean disconnect() {
		if(client == null) {
			return false;
		}

		return client.shutdown(3, TimeUnit.SECONDS);
	}

	/**
	 * Returns the actual `CouchbaseClient` connection object.
	 *
	 * If no connection is established yet, it tries to connect. Note that
	 * this is just in place for pure convenience, make sure to connect explicitely.
	 */
	public static CouchbaseClient getConnection() {
		if(client == null) {
			connect();
		}

		return client;
	}

}