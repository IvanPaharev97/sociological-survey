package com.epam.training.survey.service;

import java.util.List;

import com.epam.training.survey.dao.RoleDao;
import com.epam.training.survey.dao.exception.DaoException;
import com.epam.training.survey.dao.impl.MySqlRoleDao;
import com.epam.training.survey.entity.Role;
import com.epam.training.survey.service.exception.ServiceException;

import lombok.extern.log4j.Log4j;

@Log4j
public class RoleService {
    private RoleDao roleDao;

    public RoleService() {
        super();
        this.roleDao = new MySqlRoleDao();
    }
    
    public List<Role> getAllRoles() throws ServiceException {
        try {
            return roleDao.getAll();
        } catch (DaoException e) {
            log.error("Exception while fetching all roles: ", e);
            throw new ServiceException("Exception while fetching all roles: ", e);
        }
    }
    
    public Role getRoleByName(String name) throws ServiceException {
        try {
            return roleDao.getRoleByName(name);
        } catch (DaoException e) {
            log.error("Exception while fetching all roles: ", e);
            throw new ServiceException("Exception while fetching role by name: ", e);
        }
    }
}
