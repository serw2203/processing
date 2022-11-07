package org.example.app;

import com.google.common.base.Joiner;
import lombok.*;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CrossOrigin(origins = "*")
@RestController
@ControllerAdvice //??
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
    @SneakyThrows
    public @ResponseBody
    String info1() {
        return service1.info() + " - info1 - " + new Random().nextInt();
    }

    @ModelAttribute("param") //??
    public String implicit() {
        return "value";
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

    @GetMapping(value = "/deals", produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<BrokerDeal>> deals() {
        return ResponseEntity.ok()
            .body(IntStream.range(1, 11).mapToObj(AppEndpoint::dealGenerator).collect(Collectors.toList())
            );
    }

    @AllArgsConstructor
    @Getter
    public enum Currency {
        RUB ("Российский рубль"), USD ("Доллар США"), EUR ("Евро");
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class BrokerDeal {
        private String dealId;
        private String isin;
        private String share;
        private Double amount;
        private Double price;
        private String currency;
        private String currencyName;
        private String date;
    }

    public static BrokerDeal dealGenerator(Integer dealId) {
        Random random = new Random();

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7 + random.nextInt(20);

        String s1 = random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        targetStringLength = 7 + random.nextInt(100);

        String s2 = random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        targetStringLength = 7 + random.nextInt(20);

        String s3 = random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Currency currency = Currency.values()[random.nextInt(3)];

        return BrokerDeal.builder().dealId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase())
            .isin("XS" + (100000000 + new Random().nextInt(89999999)))
            .share(s1.toUpperCase(Locale.ROOT) + " " + s2.toUpperCase() + " " + s3.toUpperCase(Locale.ROOT))
            .amount(Math.floor(random.nextDouble() * 100000) / 1000)
            .price(Math.floor(random.nextDouble() * 1000000) / 100)
            .date(LocalDateTime.ofInstant(new Date(1549821443243L + Integer.valueOf(random.nextInt() * 100).longValue()).toInstant(), ZoneId.systemDefault()).format(formatter))
            .currency(currency.toString())
            .currencyName(currency.getName())
            .build();
    }

/*
   @PostMapping("/test")
    @ResponseBody
    public String test(@RequestBody Test2 test) {
        return service1.info() + " - test - " + new Random().nextInt();
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class Test2 {
        @JsonDeserialize(as = ArrayList.class)
        private List<SDRs> sDRs;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class SDRs {
        @JsonDeserialize(as = ArrayList.class)
        private List<SDR> sDR;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class SDR {
        private String desc;
    }*/
}
