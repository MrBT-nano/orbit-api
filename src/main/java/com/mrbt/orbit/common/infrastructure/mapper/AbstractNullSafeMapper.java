package com.mrbt.orbit.common.infrastructure.mapper;

import java.util.List;

public abstract class AbstractNullSafeMapper<E, D> implements EntityMapper<E, D> {

	@Override
	public List<D> toDomainList(List<E> entities) {
		if (entities == null) {
			return null;
		}
		return entities.stream().map(this::toDomain).toList();
	}

	@Override
	public List<E> toEntityList(List<D> domains) {
		if (domains == null) {
			return null;
		}
		return domains.stream().map(this::toEntity).toList();
	}

}
