package de.onesty.confluence.requests;

import java.io.File;
import java.util.Map;

import javax.ws.rs.core.MediaType;

/**
 * This class represents a HTTP file upload request performed against the Confluence REST API
 */
public abstract class ConfluenceFileRequest extends ConfluenceRequest{


  public abstract String getName();


  public abstract File getFile();


}
