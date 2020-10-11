package org.example.app.wizard;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class ContextWizard implements Wizard, State1, State2, State3 {

    private final Page1 page1;
    private final Page2 page2;
    private final Page3 page3;

    private int prop1Page1;
    private int prop2Page1;

    private int prop1Page2;
    private int prop2Page2;

    private int prop1Page3;
    private int prop2Page3;

    private int index;
    private List<Wizard> pages;

    @PostConstruct
    public void onCreate() {
        pages = Lists.newArrayList(page1, page2, page3);
    }

    @Override
    public void init() {
        page1.init();
    }

    @Override
    public void success() {
        pages.get(index).success();

        if (index == 2) {
            return;
        }

        index++;

        pages.get(index).init();
    }

    @Override
    public void back() {
        pages.get(index).back();

        if (index == 0) {
            return;
        }

        index--;

        pages.get(index).init();
    }

    @Override
    public String toString() {
        return "ContextWizard{" +
                "\nprop1Page1=" + prop1Page1 +
                ", prop2Page1=" + prop2Page1 +
                "\nprop1Page2=" + prop1Page2 +
                ", prop2Page2=" + prop2Page2 +
                "\nprop1Page3=" + prop1Page3 +
                ", prop2Page3=" + prop2Page3 +
                "\n}";
    }
}
