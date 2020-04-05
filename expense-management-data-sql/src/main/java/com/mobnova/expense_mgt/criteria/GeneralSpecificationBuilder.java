package com.mobnova.expense_mgt.criteria;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GeneralSpecificationBuilder {

    private final CriteriaConfigBean criteriaConfigBean;
    private List<SearchCriteria> params = new ArrayList<SearchCriteria>();

    public GeneralSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(searchCriteria -> {
                    return new GeneralSpecification(searchCriteria, criteriaConfigBean);
                })
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specs.get(i))
                    : Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
