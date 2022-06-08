package org.example.validator.flow;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlowRules<T extends Status> {

    private final Map<T, Set<T>> allowedTransitions;
    private final Set<T> initialStates;

    FlowRules(Map<T, Set<T>> allowedTransitions, Set<T> initialStates) {
        this.allowedTransitions = allowedTransitions;
        this.initialStates = initialStates;
    }

    public boolean isAllowedTransition(T fromState, T toState) {
        return fromState == toState || allowedTransitionStates(fromState).contains(toState);
    }

    public boolean isAllowedInitialState(T state) {
        return initialStates.contains(state);
    }

    public Set<T> all() {
        final Set<T> items = this.allowedTransitions.values()
                .stream()
                .flatMap(e -> e.stream())
                .collect(Collectors.toSet());
        items.addAll(Sets.newHashSet(this.allowedTransitions.keySet()));
        return items;
    }

    public Set<T> nextStatuses(Status status) {
        if (status == null) {
            return initialStates;
        }
        return allowedTransitions.get(status);
    }

    private Set<T> allowedTransitionStates(T fromState) {
        final Set<T> allowedToStates = allowedTransitions.get(fromState);
        checkNotNull(allowedToStates, String.format("Начальное состояние %s отсутствует в графе переходов", fromState));
        return allowedToStates;
    }
}
