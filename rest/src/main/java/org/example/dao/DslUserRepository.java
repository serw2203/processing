package org.example.dao;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.example.entity.QUser;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import java.util.Optional;

public interface DslUserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(QuerydslBindings bindings, QUser root) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

        bindings.excluding(root.email);
    }

    Optional<User> findByNameIgnoreCase(String name);
}
