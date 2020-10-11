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
public class Page2 implements Wizard {

    private final EventBus bus;

    @Lazy
    @Autowired
    private ContextWizard contextWizard;

    @PostConstruct
    public void onCreate() {
        bus.register(new EventHandler<Page2.Page2InitEvent>() {
            @Override @Subscribe
            public void handle(Page2.Page2InitEvent event) {
                event.state.setProp1Page2(3);
                event.state.setProp2Page2(3);
                log.info("Init page 2 - {}", event.state);
            }
        });

        bus.register(new EventHandler<Page2.Page2SuccesEvent>(){
            @Override @Subscribe
            public void handle(Page2.Page2SuccesEvent event) {
                event.state.setProp1Page2(4);
                event.state.setProp2Page2(4);
                log.info("Success page 2 - {}", event.state);
            }
        });

        bus.register(new EventHandler<Page2.Page2BackEvent>(){
            @Override @Subscribe
            public void handle(Page2.Page2BackEvent event) {
                event.state.setProp1Page2(0);
                event.state.setProp2Page2(0);
                log.info("Back page 2 - {}", event.state);
            }
        });
    }

    @Override
    public void init() {
        bus.post(Page2.Page2InitEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Override
    public void success() {
        bus.post(Page2.Page2SuccesEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Override
    public void back() {
        bus.post(Page2.Page2BackEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Builder
    public static class Page2InitEvent {
        private State2 state;
    }

    @Builder
    public static class Page2SuccesEvent {
        private State2 state;
    }

    @Builder
    public static class Page2BackEvent {
        private State2 state;
    }
}
