import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static final String targetUrl = "https://api.nasa.gov/planetary/apod?api_key=";

    public static final String key = "nMleXLp4YW04sOCkXtclUstgW1pLHJR5bGG2s2Ng";

    public static final String targetUrlWithKey = targetUrl + key;

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(targetUrlWithKey);
        CloseableHttpResponse response = httpClient.execute(request);

        NasaResponse nasaResponse = mapper.readValue(response.getEntity().getContent(),
                new TypeReference<>() {
                });

        String imageUrl = nasaResponse.getUrl();

        String[] imageUrlParts = imageUrl.split("/");

        String fileName = imageUrlParts[imageUrlParts.length - 1];

        File file = new File(fileName);

        URL url = new URL(imageUrl);

        FileUtils.copyURLToFile(url, file);
    }

}