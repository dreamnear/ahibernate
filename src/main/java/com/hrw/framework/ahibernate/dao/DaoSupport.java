
package com.hrw.framework.ahibernate.dao;

import java.util.List;

public interface DaoSupport<T> {
    int delete(T t);

    List<T> getAll(Class<T> clazz) throws InstantiationException, IllegalAccessException;

}
