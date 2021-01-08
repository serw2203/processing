package org.example.app;

import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.Service1;
import org.example.logger.LogIt;
import org.example.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AppEndpoint {

    private final Service1 service1;

    @GetMapping("/info0")
    @LogIt
    public @ResponseBody
    String info0(@CookieValue(value = "user-name", defaultValue = "SERHIO") String userName,
                 @RequestHeader Map<String, String> headers) {
        return Joiner.on("\n").join(
            service1.info(), new Random().nextInt(), userName
            , "------------------------------"
            , headers.entrySet().stream()
                .map(
                    e -> Joiner.on(" = ").join(e.getKey(), e.getValue())
                )
                .collect(
                    Collectors.joining("\n")
                )
        );
    }

    @GetMapping("/info1")
    @LogIt
    public @ResponseBody
    String info1() {
        return service1.info() + " - info1 - " + new Random().nextInt();
    }

    @GetMapping("/info2")
    @LogIt
    public @ResponseBody
    String info2() {
        return service1.info() + " - info2 - " + new Random().nextInt();
    }

    @GetMapping("/env")
    @LogIt
    public @ResponseBody
    ResponseEntity<String> env() {
        return ResponseEntity.ok("<pre>" + System.getenv().entrySet().stream()
            .map(
                e -> Joiner.on(" = ").join(e.getKey(), e.getValue())
            )
            .collect(
                Collectors.joining("\n")
            ) + "</pre>");
    }

    @GetMapping("/user1")
    public @ResponseBody
    User user1() {
        return service1.user();
    }

    @GetMapping("/session")
    @LogIt
    public @ResponseBody
    ResponseEntity<String> session(HttpSession session,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
//        response.setHeader("Connection", "close");

        final ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok()
            .header(HttpHeaders.CONNECTION, "close")
            .header(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");

        return Optional.ofNullable(session)
            .map(
                HttpSession::getId
            )
            .map(
                s -> Optional.ofNullable(System.getenv().get("HOSTNAME"))
                    .map(h -> Joiner.on("\n").join(s, " --- ", h))
                    .orElse(s)
            )
            .map(
                bodyBuilder::body
            )
            .orElse(
                bodyBuilder.body("Session id not found")
            );
    }
}
