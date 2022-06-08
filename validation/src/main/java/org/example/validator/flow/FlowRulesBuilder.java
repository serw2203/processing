package org.example.validator.flow;

import com.google.common.collect.Sets;
import org.example.infrastructure.Builder;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class FlowRulesBuilder<T extends Status> implements FlowRulesFromSetter<T>, FlowRulesToSetter<T> {

    public static <T extends Status> FlowRulesFromSetter<T> create() {
        return new FlowRulesBuilder<>();
    }

    private final Map<T, Set<T>> transitions;
    private final Set<T> initialStates;

    private T currentFromState;

    private FlowRulesBuilder() {
        this.transitions = new HashMap<>();
        this.initialStates = new HashSet<>();
    }

    @SafeVarargs
    @Override
    public final FlowRulesFromSetter<T> initial(T... initialStates) {
        this.initialStates.addAll(Sets.newHashSet(initialStates));
        return this;
    }

    @Override
    public final FlowRulesToSetter<T> fromState(T fromState) {
        currentFromState = fromState;
        return this;
    }

    @SafeVarargs
    @Override
    public final FlowRulesFromSetter<T> toState(T... toStates) {
        registerTransitions(currentFromState, Sets.newHashSet(toStates));
        return this;
    }

    @SafeVarargs
    @Override
    public final Builder<FlowRules<T>> terminal(T... terminalStates) {
        for (T terminalState : terminalStates) {
            registerTransitions(terminalState, Collections.emptySet());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final FlowRulesFromSetter<T> fromEmptyToAllExistingStates() {
        final Set<T> allowedFromStates = transitions.keySet();
        final Set<T> allowedToStates =
            transitions.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        final Sets.SetView<T> collectedStates = Sets.union(allowedFromStates, allowedToStates);
        registerEntryStates(collectedStates);
        return this;
    }

    @Override
    public FlowRules<T> build() {
        checkState(!initialStates.isEmpty(), "Нельзя создать правила для flow-валидации без входных статусов");
        return new FlowRules<>(transitions, initialStates);
    }

    private void registerTransitions(T fromState, Set<T> toStates) {
        checkNotNull(fromState);
        checkNotNull(toStates);
        checkState(!transitions.containsKey(fromState),
            String.format("Начальное состояние %s уже присутствует в графе переходов", fromState));
        transitions.put(fromState, toStates);
    }

    private void registerEntryStates(Set<T> entryStates) {
        checkState(this.initialStates.isEmpty(), "Входные состояния уже присутствуют в графе переходов");
        this.initialStates.addAll(entryStates);
    }
}
