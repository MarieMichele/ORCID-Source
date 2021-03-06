package org.orcid.listener.orcid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.orcid.jaxb.model.message.OrcidDeprecated;
import org.orcid.jaxb.model.message.OrcidMessage;
import org.orcid.listener.exception.DeprecatedRecordException;
import org.orcid.listener.exception.LockedRecordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Component
public class Orcid12APIClient {

    Logger LOG = LoggerFactory.getLogger(Orcid12APIClient.class);
    protected final Client jerseyClient;
    protected final URI baseUri;
    protected final String accessToken;

    @Autowired
    public Orcid12APIClient(@Value("${org.orcid.message-listener.api12BaseURI}") String baseUri,
            @Value("${org.orcid.message-listener.api.read_public_access_token}") String accessToken) throws URISyntaxException {
        LOG.info("Creating Orcid12APIClient with baseUri = " + baseUri);
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJaxbJsonProvider.class);
        jerseyClient = Client.create(config);
        this.baseUri = new URI(baseUri);
        this.accessToken = accessToken;

    }

    /**
     * Fetches the profile from the ORCID public API v1.2
     * 
     * @param orcid
     * @return
     * @throws LockedRecordException
     * @throws IOException 
     */
    public byte[] fetchPublicProfile(String orcid, String mediaType) throws LockedRecordException, DeprecatedRecordException, IOException {
        WebResource webResource = jerseyClient.resource(baseUri).path(orcid + "/orcid-profile");
        webResource.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);
        Builder builder = webResource.accept(mediaType).header("Authorization", "Bearer " + accessToken);
        ClientResponse response = builder.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            switch (response.getStatus()) {
            case 301:
                OrcidDeprecated orcidDeprecated = response.getEntity(OrcidDeprecated.class);
                throw new DeprecatedRecordException(orcidDeprecated);
            case 409:
                OrcidMessage orcidMessage = response.getEntity(OrcidMessage.class);
                throw new LockedRecordException(orcidMessage);
            default:
                LOG.error("Unable to fetch public record " + orcid + " on API 1.2 HTTP error code: " + response.getStatus());
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
        }
        byte [] elements = IOUtils.toByteArray(response.getEntityInputStream());
        response.close();
        return elements;
    }
}
