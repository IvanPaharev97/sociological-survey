package com.epam.training.survey.dao;

import java.util.List;

import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.entity.Role;

public interface RoleDao extends BaseDao<Role> {
    void loadRoleUsers(Role role) throws DaoException;
    List<Role> getUserRoles(int user_id) throws DaoException;
    Role getRoleByName(String name) throws DaoException;
}
