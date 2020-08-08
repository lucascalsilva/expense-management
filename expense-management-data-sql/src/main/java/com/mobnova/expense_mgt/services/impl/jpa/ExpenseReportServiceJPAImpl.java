package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.criteria.CriteriaUtil;
import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;
import com.mobnova.expense_mgt.exception.ExceptionVariable;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.services.ExpenseReportService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
public class ExpenseReportServiceJPAImpl extends AbstractBaseServiceJPA<ExpenseReport, ExpenseReportDto, Long> implements ExpenseReportService {

    private final ExpenseReportRepository expenseReportRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final CityRepository cityRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CriteriaConfigBean criteriaConfigBean;
    private final SegmentValuePairRepository segmentValuePairRepository;

    public ExpenseReportServiceJPAImpl(ExpenseReportRepository expenseReportRepository, UserRepository userRepository,
                                       CountryRepository countryRepository, CurrencyRepository currencyRepository,
                                       CityRepository cityRepository, ExpenseCategoryRepository categoryRepository,
                                       SegmentValuePairRepository segmentValuePairRepository, ModelMapper modelMapper,
                                       CriteriaConfigBean criteriaConfigBean) {
        super(expenseReportRepository, modelMapper, ExpenseReport.class, ExpenseReportDto.class);
        this.expenseReportRepository = expenseReportRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
        this.cityRepository = cityRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.criteriaConfigBean = criteriaConfigBean;
        this.segmentValuePairRepository = segmentValuePairRepository;
    }

    @Override
    public ExpenseReportDto save(ExpenseReportDto expenseReportDto) {
        ExpenseReport expenseReport = modelMapper.map(expenseReportDto, ExpenseReport.class);

        if (expenseReport.getId() != null) {
            expenseReportRepository.findById(expenseReport.getId())
                    .ifPresent(currentObject -> {
                        expenseReport.setId(currentObject.getId());
                        expenseReport.setVersion(currentObject.getVersion());
                    });
        }

        String username = expenseReport.getUser().getUsername();
        userRepository.findByUsername(username)
                .ifPresentOrElse(expenseReport::setUser,
                        () -> {
                            throw new DataNotFoundException(User.class, Fields.USERNAME, username);
                        });


        String countryCode = expenseReport.getCountry().getCode();
        countryRepository.findByCode(countryCode)
                .ifPresentOrElse(expenseReport::setCountry,
                        () -> {
                            throw new DataNotFoundException(Country.class, Fields.CODE, countryCode);
                        });


        for (ExpenseItem expenseItem : expenseReport.getExpenses()) {
            String currencyCode = expenseItem.getCurrency().getCode();
            currencyRepository.findByCode(currencyCode)
                    .ifPresentOrElse(expenseItem::setCurrency,
                            () -> {
                                throw new DataNotFoundException(Currency.class, Fields.CODE, currencyCode);
                            });

            if (expenseItem.getExpenseCity() != null && expenseItem.getExpenseCity().getCode() != null) {
                String expenseCityCode = expenseItem.getExpenseCity().getCode();
                cityRepository.findByCode(expenseCityCode)
                        .ifPresentOrElse(expenseItem::setExpenseCity,
                                () -> {
                                    throw new DataNotFoundException(City.class, Fields.CODE, expenseCityCode);
                                });

            }

            String categoryCode = expenseItem.getCategory().getCode();
            categoryRepository.findByCode(categoryCode)
                    .ifPresentOrElse(expenseItem::setCategory,
                            () -> {
                                throw new DataNotFoundException(ExpenseCategory.class, Fields.CODE, categoryCode);
                            });

            List<SegmentValuePair> segmentValuePairs = expenseItem.getSegmentValuePairs().stream()
                    .filter(segmentValuePair -> !segmentValuePair.getSegmentValue().equalsIgnoreCase("0")).map(segmentValuePair -> {
                        String segmentValuePairValue = segmentValuePair.getSegmentValue();
                        Long order = segmentValuePair.getSegmentType().getOrder();

                        SegmentValuePair currentObject = segmentValuePairRepository.findByValueAndSegmentTypeOrder(segmentValuePairValue, order)
                                .orElseThrow(() -> {
                                    List<ExceptionVariable> exceptionVariables = new ArrayList<ExceptionVariable>();
                                    exceptionVariables.add(ExceptionVariable.builder().field(Fields.ORDER).value(order).build());
                                    exceptionVariables.add(ExceptionVariable.builder().field(Fields.SEGMENT_VALUE).value(segmentValuePairValue).build());

                                    throw new DataNotFoundException(SegmentValuePair.class, exceptionVariables);
                                });

                        return currentObject;
                    }).collect(Collectors.toList());

            expenseItem.setSegmentValuePairs(segmentValuePairs);

            if (expenseItem.getExpenseReport() == null) {
                expenseItem.setExpenseReport(expenseReport);
            }

        }

        ExpenseReport savedExpenseReport = expenseReportRepository.save(expenseReport);

        return modelMapper.map(findById(savedExpenseReport.getId()), ExpenseReportDto.class);
    }

    @Override
    public Set<ExpenseReportDto> search(String search) {
        Specification<ExpenseReport> specification = CriteriaUtil.extractSpecification(search, criteriaConfigBean);

        return expenseReportRepository.findAll(specification).stream()
                .map(expenseReport -> modelMapper.map(expenseReport, ExpenseReportDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ExpenseReportDto findByReferenceID(String referenceID) {
        return modelMapper.map(expenseReportRepository.findByReferenceID(referenceID)
                .orElseThrow(() -> new DataNotFoundException(ExpenseReport.class, Fields.REFERENCE_ID, referenceID)), ExpenseReportDto.class);
    }
}
