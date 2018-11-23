package ota.monitoring.backend.db.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ota.monitoring.backend.db.QuerySelector;
import ota.monitoring.backend.db.UpdateQuerySelector;
import ota.monitoring.backend.db.model.AbstractEntity;

/**
 * @author Slobodan Erakovic
 */
@Repository
public abstract class DatabaseRepository<E extends AbstractEntity> {

	/**
	 * default JPA configuration, without specific name of persistence unit, and
	 * with generic methods
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 
	 * @param String
	 *            queryString
	 * @param Class
	 *            resultClass
	 * @return AbstractEntity
	 */
	public <T> QuerySelector<T> query(final String queryString, final Class<T> resultClass) {
		return new QuerySelector<T>(entityManager.createQuery(queryString, resultClass));
	}

	/**
	 * 
	 * @param AbstractEntity
	 *            entity
	 */
	@Transactional
	public <E extends AbstractEntity> void persist(final E entity) {
		entityManager.persist(entity);
	}

	/**
	 * 
	 * @param Class
	 *            entityClass
	 * @param long
	 *            id
	 * @return AbstractEntity
	 */
	public <E extends AbstractEntity> E get(final Class<E> entityClass, final long id) {
		return entityManager.find(entityClass, id);
	}

	/**
	 * 
	 * @param queryString
	 * @return
	 */
	public UpdateQuerySelector createUpdateQuery(final String queryString) {
		return new UpdateQuerySelector(entityManager.createQuery(queryString));
	}

	/**
	 * 
	 * @param queryString
	 * @return
	 */
	public Query nativeQuery(final String queryString) {
		return entityManager.createNativeQuery(queryString);
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional
	public <E extends AbstractEntity> E merge(final E entity) {
		return entityManager.merge(entity);
	}
}