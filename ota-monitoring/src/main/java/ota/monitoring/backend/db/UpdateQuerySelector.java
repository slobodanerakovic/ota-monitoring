package ota.monitoring.backend.db;

import java.util.Date;

import javax.persistence.Query;
import javax.persistence.TemporalType;

public class UpdateQuerySelector {

	private Query query;

	public UpdateQuerySelector(final Query query) {
		this.query = query;
	}

	public Query getQuery() {
		return query;
	}

	public UpdateQuerySelector setParameter(final String parameter, final Object object) {
		this.query.setParameter(parameter, object);
		return this;
	}

	public UpdateQuerySelector setParameter(final String name, final Date value, final TemporalType temporalType) {
		this.query.setParameter(name, value, temporalType);
		return this;
	}

	public int executeUpdate() {
		return query.executeUpdate();
	}

}
