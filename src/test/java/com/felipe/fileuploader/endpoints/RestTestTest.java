package com.felipe.fileuploader.endpoints;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Ignore;
import org.junit.Test;

import com.felipe.fileuploader.entities.TestBean;

public class RestTestTest {

	@Test
    @Ignore
	public void test() {
		Client client = ClientBuilder.newClient(new ClientConfig()
				.register(LoggingFilter.class));
		WebTarget webTarget = client.target(
				"http://localhost:8080/fileuploader/rest/TestService/").path(
				"tests");

		Invocation.Builder invocationBuilder = webTarget
				.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get(Response.class);

		List<TestBean> tests = response
				.readEntity(new GenericType<List<TestBean>>() {
				});
		
		assertThat(tests.size(), is(equalTo(2)));
	}

}
