package org.example.app.wizard;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class Page3 implements Wizard {

    private final EventBus bus;

    @Lazy
    @Autowired
    private  ContextWizard contextWizard;

    @PostConstruct
    public void onCreate() {
        bus.register(new EventHandler<Page3.Page3InitEvent>() {
            @Override @Subscribe
            public void handle(Page3.Page3InitEvent event) {
                event.state.setProp1Page3(5);
                event.state.setProp2Page3(5);
                log.info("Init page 3 - {}", event.state);
            }
        });

        bus.register(new EventHandler<Page3.Page3SuccesEvent>(){
            @Override @Subscribe
            public void handle(Page3.Page3SuccesEvent event) {
                event.state.setProp1Page3(6);
                event.state.setProp2Page3(6);
                log.info("Success page 3 - {}", event.state);
            }
        });

        bus.register(new EventHandler<Page3.Page3BackEvent>(){
            @Override @Subscribe
            public void handle(Page3.Page3BackEvent event) {
                event.state.setProp1Page3(0);
                event.state.setProp2Page3(0);
                log.info("Back page 3 - {}", event.state);
            }
        });
    }

    @Override
    public void init() {
        bus.post(Page3.Page3InitEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Override
    public void success() {
        bus.post(Page3.Page3SuccesEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Override
    public void back() {
        bus.post(Page3.Page3BackEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Builder
    public static class Page3InitEvent {
        private State3 state;
    }

    @Builder
    public static class Page3SuccesEvent {
        private State3 state;
    }

    @Builder
    public static class Page3BackEvent {
        private State3 state;
    }
}
