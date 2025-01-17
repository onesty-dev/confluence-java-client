# Confluence Java Client

A simple Java client for the Confluence Cloud REST API, 
based on https://github.com/crob1140/confluence-java-client


## Installation

To add this package to a Gradle project, add the following to your build.gradle:
```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile group: 'onesty-dev', name: 'confluence-java-client', version: '1.0.0'
}
```

## Usage

Create a new client instance:
```java
WebTarget wikiTarget = ClientBuilder.newClient().target("http://www.sample.atlassian.net/wiki");
AuthMethod basicAuth = new BasicAuth("username", "password");
Confluence client = new ConfluenceClient(wikiTarget, basicAuth);
```

Create some content:
```java
Content newPage = client.createContent(new CreateContentRequest.Builder()
    .setType(StandardContentType.PAGE)
    .setSpaceKey("SAMPLE")
    .setTitle("Sample Page")
    .setBody(ContentBodyType.STORAGE, "<ac:rich-text-body><p>SAMPLE</p></ac:rich-text-body>")
    .build());
```

Get existing content:
```java
List<Content> existingPages = client.getContent(new GetContentRequest.Builder()
    .setSpaceKey("SAMPLE")
    .setTitle("Sample Page")
    .setExpandedProperties(new ExpandedContentProperties.Builder().addVersion().build())
    .setLimit(1)
    .build())
```

Update existing content:
```java
Content updatedContent = client.updateContent(new UpdateContentRequest.Builder()
    .setId(existingPage.getId())
    .setType(existingPage.getType())
        .setTitle(existingPage.getTitle())
    .setStatus(ContentStatus.CURRENT)
    .setBody(ContentBodyType.STORAGE, "<ac:rich-text-body><p>Updated body</p></ac:rich-text-body>")
    .setVersion(existingPage.getVersion().getNumber() + 1)
    .build())
```

## Contribution
This client is a work-in-progress, and API methods will be added iteratively.
If there is a particular feature you would like added, feel free to raise it as an issue, or fork the repository and create a pull request with your own changes.
## License
MIT
