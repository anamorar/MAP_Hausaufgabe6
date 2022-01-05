package university.repository;

import university.exceptions.NullException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ICrudRepository<E> {

    /**
     * @param id {@code id} - the entity with given ID to be returned
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException, if input parameter -ID- is NULL
     * @throws SQLException, if connection to database could not succeed
     */
    E findOne(Long id) throws NullException, SQLException;

    /**
     * @return all entities
     * @throws SQLException, if connection to database could not succeed
     */
    List<E> findAll() throws SQLException;

    /**
     * @param entity {@code entity} must not be null
     * @throws NullException if input parameter -entity- is NULL
     * @throws IOException
     * @throws SQLException, if connection to database could not succeed
     */
    void update(E entity) throws NullException, IOException, SQLException;

    /**
     * @param id {@code id} id must be not null     * @return
     * @throws NullException, if input parameter -ID- is NULL
     * @throws IOException
     * @throws SQLException, if connection to database could not succeed
     */
    void del(Long id) throws NullException, IOException, SQLException;

    /**
     * @param entity {@code entity} must be not null
     * @throws NullException if input parameter -entity- is NULL
     * @throws IOException
     * @throws SQLException, if connection to database could not succeed
     */
    void save(E entity) throws NullException, IOException, SQLException;

}
