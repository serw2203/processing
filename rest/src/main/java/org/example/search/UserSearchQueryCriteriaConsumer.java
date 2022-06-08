package org.example.search;

import lombok.Builder;
import lombok.Getter;
import org.example.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

@Getter
@Builder
public class UserSearchQueryCriteriaConsumer implements Consumer<SearchCriteria> {

    private Predicate predicate;
    private final CriteriaBuilder builder;
    private final Root<User> root;

    @Override
    public void accept(SearchCriteria param) {
        if (param.getOperation().equalsIgnoreCase(">")) {
            predicate = builder.and(predicate, builder
                .greaterThanOrEqualTo(root.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase("<")) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(
                root.get(param.getKey()), param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase(":")) {
            if (root.get(param.getKey()).getJavaType() == String.class) {
                predicate = builder.and(predicate, builder.like(
                    root.get(param.getKey()), "%" + param.getValue() + "%"));
            } else {
                predicate = builder.and(predicate, builder.equal(
                    root.get(param.getKey()), param.getValue()));
            }
        }
    }
}
