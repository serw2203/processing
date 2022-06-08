package org.example.infrastructure;


import org.assertj.core.api.Assertions;
import org.example.infrastructure.test.classes.BaseService;
import org.example.infrastructure.test.classes.TestClass1;
import org.example.infrastructure.test.classes.TestClass2;
import org.example.infrastructure.test.classes.TestService1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class TypeSpecificServiceResolverTest {

    @Autowired
    private TypeSpecificServiceResolver resolver;

    @Test
    public void resolveConcreteServiceIfExist() {
        //when
        BaseService testService = resolver.resolveStrictly(TestClass1.class, BaseService.class);
        //then
        Assertions.assertThat(testService.getClass()).isEqualTo(TestService1.class);
    }

    @Test
    public void resolveBaseServiceIfConcreteDoesNotExist() {
        //when
        BaseService testService = resolver.resolveStrictly(TestClass2.class, BaseService.class);
        //then
        Assertions.assertThat(testService.getClass()).isEqualTo(BaseService.class);
    }

}
