package org.example;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.util.Lists;
import org.example.dao.DslUserRepository;
import org.example.dao.UserDao;
import org.example.dao.UserRepository;
import org.example.entity.QUser;
import org.example.entity.User;
import org.example.search.SearchCriteria;
import org.example.search.UserSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {FirebirdConfiguration.class})
@ComponentScan(basePackages = {"org.example"})
public class RestTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DslUserRepository dslUserRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void init$_0() {
        assertNotNull(this.userRepository);
        assertNotNull(this.dslUserRepository);
        assertNotNull(this.userDao);
        assertNotNull(this.entityManager);
    }

    @Test
    public void method$_0() {
        //https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        assertThat(userDao.searchUser()).isNotEmpty();

        List<SearchCriteria> searchCriteria = Lists.newArrayList(SearchCriteria.builder()
            .key("name")
            .operation(":")
            .value("Куташевский Станислав Владимирович")
            .build()
        );

        assertThat(userDao.searchUser(searchCriteria)).hasSize(1);
    }

    @Test
    public void method$_1() {
        //https://www.baeldung.com/rest-api-search-language-spring-data-specifications
        assertThat(userRepository.findAll()).isNotEmpty();

        Specification<User> specification = UserSpecification.builder().criteria(
            SearchCriteria.builder()
                .key("name")
                .operation(":")
                .value("Куташевский Станислав Владимирович")
                .build()).build();

        assertThat(userRepository.findAll(specification)).hasSize(1);
    }

    @Test
    public void method$_2() {
        //https://www.baeldung.com/rest-api-search-language-spring-data-querydsl
        assertThat(dslUserRepository.findAll()).isNotEmpty();
    }

    @Test
    public void method$_3() {
        assertThat(queryFactory.selectFrom(QUser.user)
            .where(QUser.user.name.eq("Куташевский Станислав Владимирович"))
            .fetch()).hasSize(1);
    }

    @Test
    public void method$_4() {
        assertThat(new JPAQuery<User>(entityManager)
            .select(QUser.user)
            .from(QUser.user)
            .where(QUser.user.name.eq("Куташевский Станислав Владимирович"))
            .fetch()).hasSize(1);
    }
}
