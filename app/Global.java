import play.*;

import datasources.Couchbase;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		Logger.info("Application started");
		Couchbase.connect();
	}

	public void  onStop(Application app) {
		Logger.info("Application stopped");
		Couchbase.disconnect();
	}

}