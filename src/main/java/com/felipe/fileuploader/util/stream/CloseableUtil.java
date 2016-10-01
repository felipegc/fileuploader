package com.felipe.fileuploader.util.stream;

import java.io.Closeable;
import java.io.IOException;

import javax.ws.rs.InternalServerErrorException;

import com.felipe.fileuploader.util.AppConfiguration;

public class CloseableUtil {
	
	public static void freeOSResources(Closeable output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
        	throw new InternalServerErrorException(AppConfiguration.get(
					"error.internal_error_message"));
        }
    }
}
