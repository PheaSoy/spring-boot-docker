package org.soyphea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${greeting.message:unknown}")
    String message;

    @GetMapping("/greeting")
    public String greet() {
        log.info("Greeting start - ");
        return String.format("Hello World from %s", message);
    }

    @GetMapping("/health/{status}")
    ResponseEntity<String> health(@PathVariable("status") String status) {
        if (status.equalsIgnoreCase("ok")) {
            return ResponseEntity.status(200).body("I am still alive");
        } else {
            return ResponseEntity.status(500).body("I am not ok");
        }


    }
}
