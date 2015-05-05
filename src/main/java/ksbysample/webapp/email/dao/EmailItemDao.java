package ksbysample.webapp.email.dao;

import ksbysample.webapp.email.annotation.dao.ComponentAndAutowiredDomaConfig;
import ksbysample.webapp.email.entity.EmailItem;
import org.seasar.doma.*;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface EmailItemDao {

    /**
     * @param emailItemId
     * @return the EmailItem entity
     */
    @Select
    EmailItem selectById(Long emailItemId);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(EmailItem entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(EmailItem entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(EmailItem entity);
}