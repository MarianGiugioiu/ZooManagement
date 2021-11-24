package zoomanagement.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceInterface<T> {
    /**
     * Retrieves a list of all entries found in the DB
     * @return the list of entries
     */
    List<T> getAll();

    /**
     * Retrieves one entry with the given id from the DB
     * or throws an error if no entry with that id is found.
     * @param id - id of the entry
     * @return the entry
     */
    T getOneById( UUID id ) throws Exception;

    /**
     * Adds an entry in the DB based on the received object.
     * @param entry
     * @return the object that has been saved in the DB
     */
    T add( T entry ) throws Exception;


    /**
     * Updates the entry with the given id based on the received object.
     * Throws an exception if no entry with that id was found.
     * @param id - the id of the entry to update
     * @param entry
     * @return the object saved in the DB
     */
    T update( UUID id, T object) throws Exception;

    /**
     * Deletes the entry with the given id or throws an exception if no
     * entry with that id can be found
     * @param id
     */
    void delete( UUID id ) throws Exception;
}
