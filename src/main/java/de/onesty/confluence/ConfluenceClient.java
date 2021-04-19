package de.onesty.confluence;

import de.onesty.confluence.auth.AuthMethod;
import de.onesty.confluence.content.Content;
import de.onesty.confluence.errors.ConfluenceRequestException;
import de.onesty.confluence.errors.ErrorResponse;
import de.onesty.confluence.requests.AddAttachmentsRequest;
import de.onesty.confluence.requests.ConfluenceFileRequest;
import de.onesty.confluence.requests.ConfluenceRequest;
import de.onesty.confluence.requests.CreateContentRequest;
import de.onesty.confluence.requests.DeleteAttachmentsRequest;
import de.onesty.confluence.requests.GetAttachmentsRequest;
import de.onesty.confluence.requests.GetAttachmentsResponse;
import de.onesty.confluence.requests.GetContentRequest;
import de.onesty.confluence.requests.GetContentResponse;
import de.onesty.confluence.requests.UpdateContentRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 * This class sends requests to a Confluence Cloud server.
 */
public class ConfluenceClient {

    private AuthMethod authMethod;
    private WebTarget wikiTarget;

    /**
     * <p>This constructor creates a client that can send requests to the Confluence Cloud server
     * located at the given target.</p>
     * <p>The requests generated by a client created through this constructor do not include any
     * authorisation, and can therefore only be used to access publicly available content.</p>
     *
     * @param wikiTarget
     *         The resource target pointing to the location of the Confluence Cloud server.
     */
    public ConfluenceClient(WebTarget wikiTarget) {
        this.wikiTarget = wikiTarget;
    }

    /**
     * <p>This constructor creates a client that can send requests to the Confluence Cloud server
     * located at the given target.</p>
     * <p>The requests generated by a client created through this constructor will use the
     * credentials defined by the given {@link AuthMethod}, and are therefore subject to the
     * permissions given to the user that corresponds to these credentials.</p>
     *
     * @param wikiTarget
     *         The resource target pointing to the location of the Confluence Cloud server.
     * @param authMethod
     *         The authorization method to use for all requests generated by this client.
     */
    public ConfluenceClient(WebTarget wikiTarget, AuthMethod authMethod) {
        this(wikiTarget);
        this.authMethod = authMethod;
    }

    /**
     * This method sends a request to the Confluence Cloud server to retrieve content matching the
     * conditions set in the given {@link GetContentRequest}.
     *
     * @param request
     *         The request defining the conditions for the Content that should be returned.
     * @return The content in the Conflucnce Cloud server that matches the conditions set in the given
     * {@link GetContentRequest}.
     * @throws ConfluenceRequestException
     *         if an error response is returned from the server
     */
    public List<Content> getContent(GetContentRequest request) throws ConfluenceRequestException {
        return ((GetContentResponse) performRequest(request)).getResults();
    }

    /**
     * This method sends a request to the Confluence Cloud server to update content the content as
     * defined in the given {@link UpdateContentRequest}.
     *
     * @param request
     *         The request defining what updates to apply, and which content to apply them to.
     * @return The new state of the content after the updates in the given {@link
     * UpdateContentRequest} were applied.
     * @throws ConfluenceRequestException
     *         if an error response is returned from the server
     */
    public Content updateContent(UpdateContentRequest request) throws ConfluenceRequestException {
        return (Content) performRequest(request);
    }

    /**
     * This method sends a request to the Confluence Cloud server to create the content defined in the
     * given {@link CreateContentRequest}.
     *
     * @param request
     *         The request defining the content that should be created, and what fields should
     *         be returned in the response.
     * @return The content that was created
     * @throws ConfluenceRequestException
     *         If the server responses with an error status code
     */
    public Content createContent(CreateContentRequest request) throws ConfluenceRequestException {
        return (Content) performRequest(request);
    }

    public Content addAttachment(AddAttachmentsRequest request) throws ConfluenceRequestException {
        return (Content) performFileRequest(request);
    }

    public Content deleteAttachment(DeleteAttachmentsRequest request) throws ConfluenceRequestException {
        return (Content) performRequest(request);
    }

    public List<Content> getAttachments(GetAttachmentsRequest request) throws ConfluenceRequestException {
        return ((GetAttachmentsResponse) performRequest(request)).getResults();
    }


    Object performFileRequest(ConfluenceFileRequest request) throws ConfluenceRequestException {
        WebTarget endpointTarget = wikiTarget.path(request.getRelativePath());
        for (Entry<String, String> queryParam : request.getQueryParams().entrySet()) {
            endpointTarget = endpointTarget.queryParam(queryParam.getKey(), queryParam.getValue());
        }

        Invocation.Builder invocationBuilder = endpointTarget.request();
        Map<String, String> headers = getRequestHeaders(request);
        for (Entry<String, String> headerEntry : headers.entrySet()) {
            invocationBuilder.header(headerEntry.getKey(), headerEntry.getValue());
        }
        invocationBuilder.header("X-Atlassian-Token", "nocheck");

        String methodName = request.getMethod();


        final FileDataBodyPart filePart = new FileDataBodyPart("file", request.getFile());
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);


        Response response = invocationBuilder.method(methodName, Entity.entity(multipart, multipart.getMediaType()));


        int statusCode = response.getStatus();
        if (response.getStatus() >= 300) {
            String errorMsg;
            if (MediaType.APPLICATION_JSON_TYPE.equals(response.getMediaType())) {
                ErrorResponse errResponse = response.readEntity(ErrorResponse.class);
                errorMsg = errResponse.getMessage();
            } else {
                errorMsg = response.getStatusInfo().getReasonPhrase();
            }
            throw new ConfluenceRequestException(statusCode, errorMsg);
        }

        return response.readEntity(request.getReturnType());
    }

    /**
     * This method performs the given request and returns the servers response.
     *
     * @param request
     *         The request to perform
     * @throws ConfluenceRequestException
     *         If the server responses with an error status code
     */
    Object performRequest(ConfluenceRequest request) throws ConfluenceRequestException {
        WebTarget endpointTarget = wikiTarget.path(request.getRelativePath());
        for (Entry<String, String> queryParam : request.getQueryParams().entrySet()) {
            endpointTarget = endpointTarget.queryParam(queryParam.getKey(), queryParam.getValue());
        }

        Invocation.Builder invocationBuilder = endpointTarget.request();
        Map<String, String> headers = getRequestHeaders(request);
        for (Entry<String, String> headerEntry : headers.entrySet()) {
            invocationBuilder.header(headerEntry.getKey(), headerEntry.getValue());
        }

        String methodName = request.getMethod();
        Response response;
        if (request.getBodyEntity() != null) {
            Object bodyEntity = request.getBodyEntity();
            response = invocationBuilder.method(methodName, Entity.json(bodyEntity));
        } else {
            response = invocationBuilder.method(methodName);
        }

        int statusCode = response.getStatus();
        if (response.getStatus() >= 300) {
            String errorMsg;
            if (MediaType.APPLICATION_JSON_TYPE.equals(response.getMediaType())) {
                ErrorResponse errResponse = response.readEntity(ErrorResponse.class);
                errorMsg = errResponse.getMessage();
            } else {
                errorMsg = response.getStatusInfo().getReasonPhrase();
            }
            throw new ConfluenceRequestException(statusCode, errorMsg);
        }

        return response.readEntity(request.getReturnType());
    }

    /**
     * This method returns the headers that should be included in the given request.
     *
     * @param request
     *         The request to generate headers for
     * @return A map representing the headers for the request
     */
    private Map<String, String> getRequestHeaders(ConfluenceRequest request) {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", request.getContentType().toString());
        requestHeaders.put("Accept", request.getAcceptedResponseType().toString());
        if (authMethod != null) {
            requestHeaders.put("Authorization", authMethod.getAuthHeaderValue());
        }
        return requestHeaders;
    }

    private Map<String, String> getRequestHeaders(ConfluenceFileRequest request) {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "multipart/form-data");
        requestHeaders.put("Accept", request.getAcceptedResponseType().toString());
        if (authMethod != null) {
            requestHeaders.put("Authorization", authMethod.getAuthHeaderValue());
        }
        return requestHeaders;
    }
}
