package org.example.dao;

import org.example.entity.User;
import org.example.search.SearchCriteria;
import org.example.search.UserSearchQueryCriteriaConsumer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUser() {
        return this.searchUser(new ArrayList<>());
    }

    @Override
    public List<User> searchUser(List<SearchCriteria> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = builder.conjunction();

        UserSearchQueryCriteriaConsumer searchConsumer = UserSearchQueryCriteriaConsumer.builder()
            .predicate(predicate)
            .builder(builder)
            .root(root)
            .build();

        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }
}
