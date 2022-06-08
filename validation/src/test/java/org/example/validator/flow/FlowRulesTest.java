package org.example.validator.flow;

import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FlowRulesTest {

    @Getter
    enum TestStatus implements Status {
        ACTIVE(),
        IN_ACTIVE();

        TestStatus() {
        }

        boolean activating;
        boolean deactivating;
    }

    @Test
    public void allowedAllInitialStateWhenSpecifyFromEmptyToAll() {
        //given
        FlowRules flowRulesWithoutFromNone = FlowRulesBuilder.create()
                .fromState(TestStatus.IN_ACTIVE).toState(TestStatus.ACTIVE)
                .fromEmptyToAllExistingStates()
                .build();
        //when
        Boolean activeResult = flowRulesWithoutFromNone.isAllowedInitialState(TestStatus.ACTIVE);
        Boolean inactiveResult = flowRulesWithoutFromNone.isAllowedInitialState(TestStatus.IN_ACTIVE);
        //then
        assertThat(activeResult).isTrue();
        assertThat(inactiveResult).isTrue();
    }


    @Test(expected = IllegalStateException.class)
    public void disallowedToNullStateFromNull(){
        //given
        FlowRules flowRulesWithoutFromNone = FlowRulesBuilder.create()
                .fromState(TestStatus.IN_ACTIVE).toState(TestStatus.ACTIVE)
                .build();
        //when
        flowRulesWithoutFromNone.isAllowedTransition(null, null);
        //then throw exception
    }

    @Test(expected = IllegalStateException.class)
    public void disallowedToAnyStateFromNull(){
        //given
        FlowRules flowRulesWithoutFromNone = FlowRulesBuilder.create()
                .fromState(TestStatus.IN_ACTIVE).toState(TestStatus.ACTIVE)
                .build();
        //when
        flowRulesWithoutFromNone.isAllowedTransition(null, TestStatus.ACTIVE);
        //then throw exception
    }

    @Test(expected = IllegalStateException.class)
    public void disallowedToNullStateFromAnyState(){
        //given
        FlowRules flowRulesWithoutFromNone = FlowRulesBuilder.create()
                .fromState(TestStatus.IN_ACTIVE).toState(TestStatus.ACTIVE)
                .build();
        //when
        flowRulesWithoutFromNone.isAllowedTransition(TestStatus.ACTIVE, null);
        //then throw exception
    }

    @Test
    public void returnsFalseByDefaultWhenToStateEqualsFromState() {
        //given
        Status fromState = TestStatus.IN_ACTIVE;
        Status toState = TestStatus.ACTIVE;
        FlowRules<Status> flowRules = flowRules(fromState, toState);
        //when
        Boolean result = flowRules.isAllowedTransition(fromState, fromState);
        //then
        assertThat(result)
                .overridingErrorMessage("По умолчанию переход из статуса в такой же статус запрещен")
                .isTrue();
    }

    @Test
    public void returnsTrueWhenToStateAndFromStateEqualityAllowed() {
        //given
        Status fromState = TestStatus.IN_ACTIVE;
        FlowRules<Status> flowRules = flowRules(fromState, fromState);
        //when
        Boolean result = flowRules.isAllowedTransition(fromState, fromState);
        //then
        assertThat(result)
                .overridingErrorMessage("Если явно указано что переход из статуса в такой же статус разрешен, должно быть разрешено")
                .isTrue();
    }

    private FlowRules<Status> flowRules(Status fromState, Status toState) {
        return FlowRulesBuilder.<Status>create()
                .fromState(fromState).toState(toState)
                .fromEmptyToAllExistingStates()
                .build();
    }
}
