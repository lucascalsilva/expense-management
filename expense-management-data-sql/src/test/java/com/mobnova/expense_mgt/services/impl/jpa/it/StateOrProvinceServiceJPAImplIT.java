package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.services.impl.jpa.StateOrProvinceServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionsUtil;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StateOrProvinceServiceJPAImplIT {

    @Autowired
    private StateOrProvinceServiceJPAImpl stateOrProvinceServiceJPA;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            Country country = Country.builder().code("BR").name("Brazil").build();
            countryRepository.save(country);
            dbInitialized = true;
        }
    }

    @Test
    void save() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().code("RS").name("Rio Grande do Sul")
                .country(Country.builder().code("BR").build()).build();

        StateOrProvince savedStateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvince);

        assertThat(savedStateOrProvince.getId()).isNotNull();
        assertThat(savedStateOrProvince.getCreationDate()).isNotNull();

        assertThat(savedStateOrProvince.getCountry()).isNotNull();
        assertThat(savedStateOrProvince.getCountry().getCode()).isEqualTo("BR");
    }

    @Test
    void update() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().code("CE").name("Ceara")
                .country(Country.builder().code("BR").build()).build();

        stateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvince);

        Assertions.assertThat(stateOrProvince).isNotNull();
        Assertions.assertThat(stateOrProvince.getId()).isNotNull();

        StateOrProvince updatedStateOrProvince = stateOrProvinceServiceJPA.findById(stateOrProvince.getId());

        updatedStateOrProvince.setName("Future 3_1");

        updatedStateOrProvince = stateOrProvinceServiceJPA.save(updatedStateOrProvince);

        AssertionsUtil.entityUpdateAssertions(stateOrProvince, updatedStateOrProvince);
        Assertions.assertThat(stateOrProvince.getName()).isNotEqualTo(updatedStateOrProvince.getName());
    }

    @Test
    void saveBulk() {
        StateOrProvince stateOrProvince1 = StateOrProvince.builder().code("PR").name("Parana")
                .country(Country.builder().code("BR").build()).build();
        StateOrProvince stateOrProvince2 = StateOrProvince.builder().code("SC").name("Santa Catarina")
                .country(Country.builder().code("BR").build()).build();
        Set<StateOrProvince> stateOrProvinces = new HashSet<>();

        stateOrProvinces.add(stateOrProvince1);
        stateOrProvinces.add(stateOrProvince2);

        Set<StateOrProvince> savedStateOrProvince = stateOrProvinceServiceJPA.saveBulk(stateOrProvinces);

        for(StateOrProvince stateOrProvince : savedStateOrProvince){
            assertThat(stateOrProvince.getId()).isNotNull();
            assertThat(stateOrProvince.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().code("SP").name("Sao Paulo")
                .country(Country.builder().code("BR").build()).build();

        StateOrProvince savedStateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvince);
        Long stateOrProvinceId = savedStateOrProvince.getId();

        StateOrProvince stateOrProvinceById = stateOrProvinceServiceJPA.findById(stateOrProvinceId);

        assertThat(stateOrProvinceById).isEqualTo(savedStateOrProvince);
    }

    @Test
    void deleteById() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().code("RJ").name("Rio de Janeiro")
                .country(Country.builder().code("BR").build()).build();

        StateOrProvince savedStateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvince);
        Long stateOrProvinceId = savedStateOrProvince.getId();

        stateOrProvinceServiceJPA.deleteById(stateOrProvinceId);

        assertThrows(DataNotFoundException.class, () -> stateOrProvinceServiceJPA.findById(stateOrProvinceId));

    }

    @Test
    void findByCode() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().code("MG").name("Minas Gerais")
                .country(Country.builder().code("BR").build()).build();

        StateOrProvince savedStateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvince);
        String stateOrProvinceCode = savedStateOrProvince.getCode();

        StateOrProvince stateOrProvinceByCode = stateOrProvinceServiceJPA.findByCode(stateOrProvinceCode);

        assertThat(stateOrProvinceByCode).isEqualTo(stateOrProvince);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> stateOrProvinceServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(StateOrProvince.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(code);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> stateOrProvinceServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(StateOrProvince.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}