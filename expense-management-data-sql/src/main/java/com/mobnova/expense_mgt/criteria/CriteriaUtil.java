package com.mobnova.expense_mgt.criteria;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.exceptions.InvalidCriteriaException;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriteriaUtil {

    public static Specification extractSpecification(String search, CriteriaConfigBean criteriaConfigBean) {
        GeneralSpecificationBuilder specificationBuilder = new GeneralSpecificationBuilder(criteriaConfigBean);
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|>=|<=)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            specificationBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        return Optional.of(specificationBuilder.build())
                .orElseThrow(() -> new InvalidCriteriaException("Informed search criteria is invalid " + "\'" + search + "\'"));

    }
}
