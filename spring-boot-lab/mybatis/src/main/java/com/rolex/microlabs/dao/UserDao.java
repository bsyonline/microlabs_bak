/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.dao;

import com.rolex.microlabs.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author rolex
 * @since 2019
 */
@Mapper
public interface UserDao {

    int save(User user);

    int batchSave(List<User> list);

}
