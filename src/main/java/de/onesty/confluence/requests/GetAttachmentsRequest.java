package de.onesty.confluence.requests;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HttpMethod;

public class GetAttachmentsRequest extends ConfluenceRequest {


    final static String url="rest/api/content/{id}/child/attachment";

    private String id;
    public GetAttachmentsRequest(Builder builder) {
        super();
        this.id = builder.id;
    }

    @Override
    public String getRelativePath() {
        return url.replace("{id}", this.id);
    }

    /**
     * This method returns the HTTP method used by this request.
     */
    @Override
    public String getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getQueryParams() {
        return new HashMap<>();
    }

    @Override
    public Object getBodyEntity() {
        return null;
    }

    /**
     * This method returns the class of the object in the body of the response for this request.
     *
     * @return The class of the object in the body of response for this request.
     */
    @Override
    public Class<?> getReturnType() {
        return GetAttachmentsResponse.class;
    }

    public static class Builder {

              private String id;

              /**
         * This method sets the unique identifier for the content.
         *
         * @param id
         *         The unique identifier for the content.
         * @return This instance, for the purposes of method chaining.
         */
        public GetAttachmentsRequest.Builder setId(String id) {
            this.id = id;
            return this;
        }



        /**
         * This method creates an instance of {@link GetAttachmentsRequest} using the values that were
         * set on this instance.
         *
         * @return A new instance of {@link GetAttachmentsRequest} with the values set on this instance.
         * @throws IllegalStateException
         *         If the request that would be created would be invalid.
         */
        public GetAttachmentsRequest build() throws IllegalStateException {
            if (this.id == null || this.id.equals("")) {
                throw new IllegalStateException("You must specify the id of the page you want to fetch attachemnts");
            }


            return new GetAttachmentsRequest(this);
        }
    }
}
