package com.felipe.fileuploader.endpoint.poc;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/TestService")
public class RestTest {
	
	@GET
	@Path("/tests")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Test> getTests() {
		Test test = new Test();
		test.setId(1);
		test.setName("Test");
		
		Test test2 = new Test();
		test2.setId(2);
		test2.setName("Test2");
		
		List<Test> tests = new ArrayList<Test>();
		tests.add(test);
		tests.add(test2);
		
		return tests;
	}
}
