package com.mobnova.expense_mgt.criteria;

import com.mobnova.expense_mgt.config.CriteriaConfig;
import com.mobnova.expense_mgt.exceptions.InvalidCriteriaException;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
@RequiredArgsConstructor
public class GeneralSpecification implements Specification {

    private final SearchCriteria criteria;
    private final CriteriaConfig criteriaConfig;

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThan(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThan(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(">=")) {
            return builder.greaterThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            return builder.lessThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {

            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                if (criteria.getValue().toString().length() >= criteriaConfig.getMinCharactersForLikeSearch()) {
                    return builder.like(builder.upper(root.<String>get(criteria.getKey())), "%"
                            + criteria.getValue().toString().toUpperCase() + "%");
                } else {
                    throw new InvalidCriteriaException("Criteria " + "\'" + criteria.getKey() + "\'"
                            + " invalid. You must provide at least " + criteriaConfig.getMinCharactersForLikeSearch()
                            + " characters in the value.");
                }
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
