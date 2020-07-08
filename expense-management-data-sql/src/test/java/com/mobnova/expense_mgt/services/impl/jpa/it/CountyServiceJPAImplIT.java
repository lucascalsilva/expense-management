package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CountyServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionsUtil;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CountyServiceJPAImplIT {

    @Autowired
    private CountyServiceJPAImpl countyServiceJPA;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            Country country = Country.builder().code("BR").name("Brazil").build();
            StateOrProvince stateOrProvince = StateOrProvince.builder().code("RS").name("Rio Grande do Sul")
                    .country(country).build();

            countryRepository.save(country);
            stateOrProvinceRepository.save(stateOrProvince);
            dbInitialized = true;
        }
    }

    @Test
    void saveValidationError() {
        County county = County.builder().code(null).name("Condado de Porto Alegre").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> countyServiceJPA.save(county));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.code: must not be blank");
    }

    @Test
    void save() {
        County county = County.builder().code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        County savedCounty = countyServiceJPA.save(county);

        assertThat(savedCounty.getId()).isNotNull();
        assertThat(savedCounty.getCreationDate()).isNotNull();

        assertThat(savedCounty.getStateOrProvince().getId()).isNotNull();
        assertThat(savedCounty.getStateOrProvince().getCreationDate()).isNotNull();
    }

    @Test
    void update() {
        County county = County.builder().code("TOR").name("Condado de Torres")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        county = countyServiceJPA.save(county);

        assertThat(county).isNotNull();
        assertThat(county.getId()).isNotNull();

        County updateCounty = countyServiceJPA.findById(county.getId());

        updateCounty.setName("Condado de Torres_1");

        updateCounty = countyServiceJPA.save(updateCounty);

        AssertionsUtil.entityUpdateAssertions(county, updateCounty);
        assertThat(county.getName()).isNotEqualTo(updateCounty.getName());
    }

    @Test
    void saveBulk() {
        County county1 = County.builder().code("CAX").name("Condado de Caxias")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();
        County county2 = County.builder().code("PEL").name("Condado de Pelotas")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        Set<County> counties = new HashSet<>();

        counties.add(county1);
        counties.add(county2);

        Set<County> savedCounties = countyServiceJPA.saveBulk(counties);

        for(County county : savedCounties){
            assertThat(county.getId()).isNotNull();
            assertThat(county.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        County county = County.builder().code("ALG").name("Condado do Alegrete")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        County savedCounty = countyServiceJPA.save(county);
        Long countyId = savedCounty.getId();

        County countyById = countyServiceJPA.findById(countyId);

        assertThat(countyById).isEqualTo(savedCounty);
    }

    @Test
    void deleteById() {
        County county = County.builder().code("CHU").name("Condado do Chui")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        County savedCounty = countyServiceJPA.save(county);
        Long countyId = savedCounty.getId();

        countyServiceJPA.deleteById(countyId);

        assertThrows(DataNotFoundException.class, () -> countyServiceJPA.findById(countyId));
    }

    @Test
    void findByCode() {
        County county = County.builder().code("STC").name("Condado do Santa Cruz")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        County savedCounty = countyServiceJPA.save(county);
        String countyCode = savedCounty.getCode();

        County countyByCode = countyServiceJPA.findByCode(countyCode);

        assertThat(countyByCode).isEqualTo(savedCounty);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> countyServiceJPA.findByCode(code));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(County.class.getName());
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> countyServiceJPA.findById(id));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(County.class.getName());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}