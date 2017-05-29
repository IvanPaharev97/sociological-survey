package com.epam.training.survey.dao;

import java.util.List;

import com.epam.training.survey.dao.exception.DaoException;

public interface BaseDao<E> {
    E persist(E e) throws DaoException;
    int update(E e) throws DaoException;
    int delete(int id) throws DaoException;
    E get(int id) throws DaoException;
    List<E> getAll() throws DaoException;
}
