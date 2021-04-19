package de.onesty.confluence.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.onesty.confluence.content.Content;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAttachmentsResponse {
    @JsonProperty
    private List<Content> results;
    @JsonProperty
    private Integer size;

    @SuppressWarnings("unused")
    private GetAttachmentsResponse() {
        // Required for Jackson deserialization
    }



    /**
     * This constructor initialises the response with the given set of results.
     *
     * @param results The results contained in the response
     */
    public GetAttachmentsResponse(List<Content> results) {
        this.results = results;
    }

    public Integer getSize() {
        return size;
    }

    /**
     * This method returns the matching content contained in the response to a {@link
     * GetContentRequest}.
     *
     * @return The contained in the response to a {@link GetContentRequest}.
     */
    public List<Content> getResults() {
        return this.results;
    }
}
