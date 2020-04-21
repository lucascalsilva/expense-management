package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.util.DemoDataGenerator;
import com.mobnova.expense_mgt.util.DemoDataPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class CityServiceJPAImplIT {

    @Autowired
    private CityServiceJPAImpl cityServiceJPA;

    @Autowired
    private DemoDataPersistence demoDataPersistence;

    private DemoDataGenerator demoDataGenerator;

    private static Boolean baseDataPersisted = false;

    @BeforeEach
    public void init(){
        demoDataGenerator = new DemoDataGenerator();
        if(!baseDataPersisted) {
            demoDataPersistence.persistCountries(demoDataGenerator.getCountries());
            demoDataPersistence.persistStateOrProvince(demoDataGenerator.getStateOrProvinces());
            baseDataPersisted = true;
        }
    }

    @Test
    void save() {
        String cityCode = "CAN";
        String cityName = "Canoas";
        City citySaved = cityServiceJPA.save(City.builder().code(cityCode).name(cityName)
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build());

        assertThat(citySaved.getId()).isNotNull();
        assertThat(citySaved.getCreationDate()).isNotNull();

        assertThat(citySaved.getStateOrProvince()).isNotNull();
        assertThat(citySaved.getStateOrProvince().getCode()).isEqualTo("RS");
    }

    @Test
    void saveBulk() {
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findByCode() {
    }
}