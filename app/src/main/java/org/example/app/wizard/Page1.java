package org.example.app.wizard;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class Page1 implements Wizard {

    private final EventBus bus;

    @Lazy
    @Autowired
    private ContextWizard contextWizard;

    @Getter
    private Page1InitEventHandler page1InitEventHandler;

    @PostConstruct
    public void onCreate() {
        this.page1InitEventHandler = new Page1InitEventHandler();

        bus.register(this.page1InitEventHandler);

        bus.register(new EventHandler<Page1SuccesEvent>(){
            @Override @Subscribe
            public void handle(Page1SuccesEvent event) {
                event.state.setProp1Page1(2);
                event.state.setProp2Page1(2);
                log.info("Success page 1 - {}", event.state);
            }
        });

        bus.register(new EventHandler<Page1BackEvent>(){
            @Override @Subscribe
            public void handle(Page1BackEvent event) {
                event.state.setProp1Page1(0);
                event.state.setProp2Page1(0);
                log.info("Back page 1 - {}", event.state);
            }
        });
    }

    @Override
    public void init() {
        bus.post(Page1InitEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Override
    public void success() {
        bus.post(Page1SuccesEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Override
    public void back() {
        bus.post(Page1BackEvent.builder()
                .state(contextWizard)
                .build()
        );
    }

    @Builder
    public static class Page1InitEvent {
        private State1 state;
    }

    @Builder
    public static class Page1SuccesEvent {
        private State1 state;
    }

    @Builder
    public static class Page1BackEvent {
        private State1 state;
    }

    public static class Page1InitEventHandler implements EventHandler<Page1InitEvent> {
        @Override @Subscribe
        public void handle(Page1InitEvent event) {
            event.state.setProp1Page1(1);
            event.state.setProp2Page1(1);
            log.info("Init page 1 - {}", event.state);
        }
    }
}
