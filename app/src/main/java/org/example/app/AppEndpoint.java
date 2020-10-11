package org.example.app;

import lombok.RequiredArgsConstructor;
import org.example.app.model.User;
import org.example.app.service.Service1;
import org.example.logger.LogIt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class AppEndpoint {

    private final Service1 service1;

    @GetMapping ("/info0")
    @LogIt
    public @ResponseBody String info0 () {
        return service1.info() + " - info0 - " + new Random().nextInt();
    }


    @GetMapping ("/info1")
    @LogIt
    public @ResponseBody String info1 () {
        return service1.info() + " - info1 - " + new Random().nextInt();
    }

    @GetMapping ("/info2")
    @LogIt
    public @ResponseBody String info2 () {
        return service1.info() + " - info2 - " + new Random().nextInt();
    }

    @GetMapping ("/user1")
    public  @ResponseBody  User user1 () {
        return service1.user();
    }
}
