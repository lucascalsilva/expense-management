package com.mobnova.expense_mgt.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mobnova.expense_mgt.config.DataLoaderConfig;
import com.mobnova.expense_mgt.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("bootstrap")
public class DataLoader implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final DataLoaderConfig dataLoaderConfig;
    private final ObjectMapper mapper;
    private final Validator validator;

    @Override
    public void run(String... args) throws Exception {
        log.info("Running data loader...");
        bootstrapData();
    }

    private void bootstrapData() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        URL bootstrapFolderUrl = Thread.currentThread().getContextClassLoader().getResource(dataLoaderConfig.getBootstrapFilesFolder());
        if (bootstrapFolderUrl != null) {
            Optional<File[]> files = Optional.ofNullable(new File(bootstrapFolderUrl.getPath()).listFiles());
            files.ifPresent(files_ -> {
                Arrays.sort(files_, Comparator.comparing(File::getName));
                Arrays.stream(files_).forEach(this::loadDataFile);
            });
        } else {
            log.warn("Bootstrap folder not found...");
        }
    }

    private void loadDataFile(File file) {
        try {
            String baseTypeClassName = StringUtils.capitalize(file.getName().replace(".json", "")
                    .split("-", 0)[1]);
            String serviceInterfaceName = baseTypeClassName + "Service";

            Class typeClass = Class.forName(dataLoaderConfig.getBaseDtoPackage() + "." + baseTypeClassName + "Dto");
            Class arrayClass = Array.newInstance(typeClass, 0).getClass();
            Class serviceClass = Class.forName(dataLoaderConfig.getBaseServicePackage() + "." + serviceInterfaceName);
            Optional<Object[]> data = Optional.of((Object[]) mapper.readValue(file, arrayClass));
            BaseService service = (BaseService) applicationContext.getBean(serviceClass);

            AtomicInteger loadedRecords = new AtomicInteger(0);

            data.ifPresent(objects -> {
                Arrays.stream(objects).forEach(object -> {
                    try {
                        service.save(object);
                        loadedRecords.incrementAndGet();
                        log.debug("Loaded record into {} ", file.getName());
                        log.debug("Record {} ", object.toString());
                    }
                    catch(Exception e){
                        log.error("Couldn't load record {}", object.toString());
                        log.error("Exception: ", e);
                    }
                });
            });
            if(loadedRecords.get() > 0){
                log.info("Loaded file {} ", file.getName());
            }
            else{
                log.info("Couldn't load any record of file {}, please check the errors", file.getName());
            }

        } catch (IOException | ClassNotFoundException | ValidationException e) {
            log.error("Issue on loading data for file {}", file.getName());
            log.error("Exception: ", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
