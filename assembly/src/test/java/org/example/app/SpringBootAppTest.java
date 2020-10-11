package org.example.app;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.example.app.wizard.ContextWizard;
import org.example.app.wizard.EventHandler;
import org.example.app.wizard.Page1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SpringBootAppTest {

    @Autowired
    private ContextWizard contextWizard;

    @Autowired
    private EventBus bus;

    @Autowired
    private Page1 page1;

    @DisplayName("WIZARD")
    @Test
    void test2() {
        bus.unregister(page1.getPage1InitEventHandler());

        bus.register(new EventHandler<Page1.Page1InitEvent>() {
            @Override
            @Subscribe
            public void handle(Page1.Page1InitEvent event) {
                log.info("================== {}", "BEFORE INIT PAGE 1.1");
                page1.getPage1InitEventHandler().handle(event);
                log.info("================== {}", "AFTER INIT PAGE 1.1\n");
            }
        });

        contextWizard.init();
        contextWizard.back();
        contextWizard.success();
        contextWizard.back();
        contextWizard.success();
        contextWizard.success();
        contextWizard.back();
        contextWizard.success();
        contextWizard.success();
        contextWizard.back();
    }
}
