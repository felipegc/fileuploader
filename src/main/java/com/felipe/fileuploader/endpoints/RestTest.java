package com.felipe.fileuploader.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.felipe.fileuploader.entities.TestBean;


@Path("/TestService")
public class RestTest {
	
	@GET
	@Path("/tests")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTests() {
		TestBean test = new TestBean();
		test.setId(1);
		test.setName("Test");
		
		TestBean test2 = new TestBean();
		test2.setId(2);
		test2.setName("Test2");
		
		List<TestBean> tests = new ArrayList<TestBean>();
		tests.add(test);
		tests.add(test2);
		
		return Response.ok().entity(tests).build();
	}
}
