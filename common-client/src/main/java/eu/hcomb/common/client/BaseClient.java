package eu.hcomb.common.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

public class BaseClient {

	@Inject
	public Client jerseyClient;

	public void setJerseyClient(Client jerseyClient) {
		this.jerseyClient = jerseyClient;
	}

	public void expect(Response response, int[] statusCode) {
		boolean found = false;
		for (int i = 0; i < statusCode.length; i++) {
			if(response.getStatus() == statusCode[i])
				found = true;
		}
		if(!found)
			throw new RuntimeException("expecting response status "+statusCode+" but got "+ response.getStatus());

	}
}
