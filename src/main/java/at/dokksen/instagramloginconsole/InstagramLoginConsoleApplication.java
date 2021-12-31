package at.dokksen.instagramloginconsole;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Scanner;

@SpringBootApplication
public class InstagramLoginConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstagramLoginConsoleApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doAfterStartup() {
        WebClient webClient = WebClient.create();

        LoginInstagram loginInstagram = new LoginInstagram(webClient);

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.println("------Login Instagram via Console------");
            System.out.println("Please enter you username");
            String username = input.next();
            System.out.println("Please enter your password");
            String password = input.next();
            try {
                System.out.println(loginInstagram.login(username,password));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("-----------------------------------------");
        }
    }


}
