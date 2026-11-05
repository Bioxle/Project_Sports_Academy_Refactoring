package com.example.SprotsAcademy.repositories;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();

    boolean save(T entity);

    boolean delete(T entity);

}
