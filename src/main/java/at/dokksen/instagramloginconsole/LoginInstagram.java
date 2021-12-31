package at.dokksen.instagramloginconsole;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.Date;



public class LoginInstagram {


    private String link = "https://www.instagram.com/accounts/login/";
    private String login_url = "https://www.instagram.com/accounts/login/ajax/";

    private MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();


    private Date time = new Timestamp(System.currentTimeMillis());

    private WebClient webClient;

    public LoginInstagram(WebClient webClient) {
        this.webClient = webClient;
    }

    public String login(String username, String password) throws RuntimeException {
        formData.add("username", username);
        formData.add("enc_password", "#PWD_INSTAGRAM_BROWSER:0:" + time.getTime() + ":" + password);
        formData.add("queryParams", "{}");
        formData.add("optIntoOneTap", "false");

        try {
            String response =  webClient.post()
                    .uri(login_url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("Referer","https://www.instagram.com/accounts/login/")
                    .header("x-csrftoken","all")
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println(response);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(response);

            JsonNode user = actualObj.get("user");
            JsonNode authenticated = actualObj.get("authenticated");


            if(user.booleanValue() == false){
                return "Given user does not exist!";
            } else if (authenticated.booleanValue() == false){
                return "Entered credentials are not correct!";
            } else {
                return "User is authenticated! Login successful!";
            }
        }
        catch (Exception ex){
            throw new RuntimeException();
        }
    }


}
