package de.onesty.confluence.content.expand;

import de.onesty.confluence.content.Content;
import de.onesty.confluence.content.ContentBodyType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the properties that can be expanded in a {@link
 * Content} instance returned from the Confluence Cloud
 * server.
 */
public class ExpandedContentProperties {

    private final Set<String> properties;

    private ExpandedContentProperties(Builder builder) {
        this.properties = Collections.unmodifiableSet(builder.properties);
    }

    /**
     * This method returns the set of properties that are to be expanded.
     *
     * @return The set of properties to be expanded.
     */
    public Set<String> getProperties() {
        return this.properties;
    }

    /**
     * This class can be used to construct an instance of {@link ExpandedContentProperties}.
     */
    public static class Builder {

        private Set<String> properties;

        public Builder() {
            this.properties = new HashSet<>();
        }

        /**
         * This method adds the given child types to the list of expanded properties.
         *
         * @param childTypes
         *         The child types of the content that should be expanded.
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addChildTypes(ExpandedChildTypeProperties childTypes) {
            for (String property : childTypes.getProperties()) {
                this.properties.add("childTypes." + property);
            }
            return this;
        }

        /**
         * This method adds "container" as a property to be expanded.
         *
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addContainer() {
            this.properties.add("container");
            return this;
        }

        /**
         * This method adds the given metadata to the list of expanded properties.
         *
         * @param properties
         *         The properties of the content metadata that should be expanded.
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addMetadata(ExpandedMetadataProperties properties) {
            for (String property : properties.getProperties()) {
                this.properties.add("metadata." + property);
            }

            return this;
        }

        /**
         * This method adds "operations" as a property to be expanded.
         *
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addOperations() {
            this.properties.add("operations");
            return this;
        }

        /**
         * This method adds the given children to the list of expanded properties.
         *
         * @param properties
         *         The children of the content children that should be expanded.
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addChildren(ExpandedChildrenProperties properties) {
            for (String property : properties.getProperties()) {
                this.properties.add("children." + property);
            }
            return this;
        }

        /**
         * This method adds the given restrictions to the list of expanded properties.
         *
         * @param properties
         *         The restrictions on the content that should be expanded.
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addRestrictions(ExpandedRestrictionProperties properties) {
            for (String property : properties.getProperties()) {
                this.properties.add("restrictions." + property);
            }
            return this;
        }

        /**
         * This method adds the given history to the list of expanded properties.
         *
         * @param properties
         *         The history properties that should be expanded.
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addHistory(ExpandedHistoryProperties properties) {
            for (String property : properties.getProperties()) {
                this.properties.add("history." + property);
            }
            return this;
        }

        /**
         * This method adds "ancestors" as a property to be expanded.
         *
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addAncestors() {
            this.properties.add("ancestors");
            return this;
        }

        /**
         * This method adds the given properties of the given body type to the list of expanded
         * properties.
         *
         * @param type
         *         The type of content.
         * @param properties
         *         The properties to expand for this type.
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addBody(ContentBodyType type, ExpandedBodyFormatProperties properties) {
            for (String property : properties.getProperties()) {
                this.properties.add("body." + type.getIdentifier() + "." + property);
            }
            return this;
        }

        /**
         * This method adds "version" as a property to be expanded.
         *
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addVersion() {
            this.properties.add("version");
            return this;
        }

        /**
         * This method adds the given descedants to the list of expanded properties.
         *
         * @param properties
         *         The descendants of the content that should be expanded.
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addDescendants(ExpandedDescendantsProperties properties) {
            for (String property : properties.getProperties()) {
                this.properties.add("descendants."  + "." + property);
            }
            return this;
        }

        /**
         * This method adds "space" as a property to be expanded.
         *
         * @return This instance, for the purposes of method chaining.
         */
        public Builder addSpace() {
            this.properties.add("space");
            return this;
        }

        /**
         * This method creates an instance of {@link ExpandedContentProperties} using the values that
         * were set on this instance.
         *
         * @return A new instance of {@link ExpandedContentProperties} with the values set on this
         * instance.
         */
        public ExpandedContentProperties build() {
            return new ExpandedContentProperties(this);
        }
    }
}
