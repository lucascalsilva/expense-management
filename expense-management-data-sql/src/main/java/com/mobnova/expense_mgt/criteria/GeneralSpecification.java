package com.mobnova.expense_mgt.criteria;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.exceptions.InvalidCriteriaException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class GeneralSpecification implements Specification {

    private final SearchCriteria criteria;
    private final CriteriaConfigBean criteriaConfigBean;

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
                if (criteria.getValue().toString().length() >= criteriaConfigBean.getMinCharactersForLikeSearch()) {
                    return builder.like(builder.upper(root.<String>get(criteria.getKey())), "%"
                            + criteria.getValue().toString().toUpperCase() + "%");
                } else {
                    throw new InvalidCriteriaException("Criteria " + "\'" + criteria.getKey() + "\'"
                            + " invalid. You must provide at least " + criteriaConfigBean.getMinCharactersForLikeSearch()
                            + " characters in the value.");
                }
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
