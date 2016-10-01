package com.felipe.fileuploader.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

public class StreamingOutputImpl implements StreamingOutput{

	private String fileLocation;
	
	public StreamingOutputImpl(String fileLocation){
		this.fileLocation = fileLocation;
	}
	
	@Override
	public void write(OutputStream os) throws IOException,
			WebApplicationException {

		try{
			Path path = Paths.get(fileLocation);
            byte[] data = Files.readAllBytes(path);
            os.write(data);
		}finally{
			IOUtils.closeQuietly(os);
		}
		
	}

}
