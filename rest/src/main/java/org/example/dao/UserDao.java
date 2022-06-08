package org.example.dao;

import org.example.entity.User;
import org.example.search.SearchCriteria;

import java.util.List;

public interface UserDao {
    List<User> searchUser(List<SearchCriteria> params);
    List<User> searchUser();
}
