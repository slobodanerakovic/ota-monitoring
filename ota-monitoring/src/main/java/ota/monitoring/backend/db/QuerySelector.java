package ota.monitoring.backend.db;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 * @author Slobodan Erakovic
 */
public class QuerySelector<T> {

	private TypedQuery<T> query;

	public QuerySelector(final TypedQuery<T> query) {
		this.query = query;
	}

	public TypedQuery<T> getQuery() {
		return query;
	}

	public QuerySelector<T> setParameter(final String parameter, final Object object) {
		this.query.setParameter(parameter, object);
		return this;
	}

	public QuerySelector<T> setParameter(final String name, final Date value, final TemporalType temporalType) {
		this.query.setParameter(name, value, temporalType);
		return this;
	}

	public List<T> getResultList() {
		return query.getResultList();
	}

	public T getSingleResult() {
		try {
			return query.getSingleResult();
		} catch (final NoResultException e) {
			return null;
		}
	}

	public T getFirstResult() {
		final List<T> results = query.setMaxResults(1).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	public QuerySelector<T> maxResults(final int maxResults) {
		query.setMaxResults(maxResults);
		return this;
	}

	public QuerySelector<T> firstResult(final int startPosition) {
		query.setFirstResult(startPosition);
		return this;
	}

	public QuerySelector<T> setCacheable() {
		query.setHint("org.hibernate.cacheable", true);
		return this;
	}
}
