package pro.marcuss.calculator.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.marcuss.calculator.service.RandomStringService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class RandomStringServiceImpl implements RandomStringService {

    private static final String TARGET_URL = "https://www.random.org/quota/?format=plain";
    private final Logger log = LoggerFactory.getLogger(RandomStringServiceImpl.class);
    private final ObjectMapper mapper;

    public RandomStringServiceImpl(ObjectMapper mapper) {
        this.mapper = new ObjectMapper();
    }

    public String getRandomDouble() {
        log.debug("Received a request for a random double");
        try {
            URI targetURI = new URI(TARGET_URL);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(targetURI)
                .GET()
                .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Double randomDouble = mapper.readValue(response.body(), Double.class);

            log.debug("Received random double : " + randomDouble);
            return randomDouble.toString();

        } catch (JsonMappingException e) {
            log.debug("JsonMappingException", e);
        } catch (URISyntaxException e) {
            log.debug("URISyntaxException", e);
        } catch (IOException e) {
            log.debug("IOException", e);
        } catch (InterruptedException e) {
            log.debug("InterruptedException", e);
        }
        return "0";
    }
}
