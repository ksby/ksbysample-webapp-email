package ksbysample.webapp.email.dao;

import ksbysample.webapp.email.annotation.dao.ComponentAndAutowiredDomaConfig;
import ksbysample.webapp.email.entity.Email;
import org.seasar.doma.*;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface EmailDao {

    /**
     * @param emailId
     * @return the Email entity
     */
    @Select
    Email selectById(Long emailId);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(Email entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(Email entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(Email entity);
}