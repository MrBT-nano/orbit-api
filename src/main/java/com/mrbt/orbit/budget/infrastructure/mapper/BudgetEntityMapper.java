package com.mrbt.orbit.budget.infrastructure.mapper;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mrbt.orbit.budget.core.model.Budget;
import com.mrbt.orbit.budget.core.model.BudgetItem;
import com.mrbt.orbit.budget.infrastructure.entity.BudgetEntity;
import com.mrbt.orbit.budget.infrastructure.entity.BudgetItemEntity;
import com.mrbt.orbit.common.infrastructure.mapper.AbstractNullSafeMapper;

@Component
public class BudgetEntityMapper extends AbstractNullSafeMapper<BudgetEntity, Budget> {

	public Budget toDomain(BudgetEntity entity) {
		if (entity == null)
			return null;

		List<BudgetItem> items = entity.getItems() != null
				? entity.getItems().stream().map(this::itemToDomain).toList()
				: List.of();

		return Budget.builder().id(entity.getId()).userId(entity.getUserId()).name(entity.getName())
				.periodType(entity.getPeriodType()).startDate(entity.getStartDate()).endDate(entity.getEndDate())
				.totalAmount(entity.getTotalAmount()).status(entity.getStatus()).items(items)
				.createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
				.build();
	}

	public BudgetEntity toEntity(Budget domain) {
		if (domain == null)
			return null;

		BudgetEntity entity = new BudgetEntity();
		entity.setId(domain.getId());
		entity.setUserId(domain.getUserId());
		entity.setName(domain.getName());
		entity.setPeriodType(domain.getPeriodType());
		entity.setStartDate(domain.getStartDate());
		entity.setEndDate(domain.getEndDate());
		entity.setTotalAmount(domain.getTotalAmount());
		entity.setStatus(domain.getStatus());

		if (domain.getItems() != null) {
			List<BudgetItemEntity> itemEntities = domain.getItems().stream().map(item -> {
				BudgetItemEntity ie = itemToEntity(item);
				ie.setBudget(entity);
				return ie;
			}).toList();
			entity.setItems(itemEntities);
		}

		return entity;
	}

	public BudgetItem itemToDomain(BudgetItemEntity entity) {
		if (entity == null)
			return null;

		return BudgetItem.builder().id(entity.getId())
				.budgetId(entity.getBudget() != null ? entity.getBudget().getId() : null)
				.categoryId(entity.getCategoryId()).allocatedAmount(entity.getAllocatedAmount())
				.spentAmount(entity.getSpentAmount()).alertThresholdPct(entity.getAlertThresholdPct())
				.createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
				.build();
	}

	public BudgetItemEntity itemToEntity(BudgetItem domain) {
		if (domain == null)
			return null;

		BudgetItemEntity entity = new BudgetItemEntity();
		entity.setId(domain.getId());
		entity.setCategoryId(domain.getCategoryId());
		entity.setAllocatedAmount(domain.getAllocatedAmount());
		entity.setSpentAmount(domain.getSpentAmount() != null ? domain.getSpentAmount() : BigDecimal.ZERO);
		entity.setAlertThresholdPct(domain.getAlertThresholdPct());
		return entity;
	}

}
