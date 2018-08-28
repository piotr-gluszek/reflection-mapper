package com.pgluszek.mapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Optional;

public class Mapper {

    private static final String MAPPING_DOES_NOT_EXIST = "Mapping file %s does not exist.";
    private static final String LOADING_MAPPING = "Loading mapping...";
    private static final String MAPPING_LOADED = "Loading mapping successful.";

    private static final Logger log = Logger.getLogger(Mapper.class);
    private String path;
    private HashMap<String, HashMap<String, String>> mapping;


    public Mapper(@NotNull String path) {
        this.path = path;
    }

    public void loadMapping() throws MapperException {
        log.debug(LOADING_MAPPING);
        File file = new File(path);
        if (!file.exists())
            throw new MapperException(String.format(MAPPING_DOES_NOT_EXIST, path));
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapping = mapper.readValue(file, new TypeReference<HashMap<String, HashMap<String, String>>>() {
            });
            log.debug(MAPPING_LOADED);
        } catch (JsonParseException ex) {
            throw new MapperException(ex.getMessage());
        } catch (JsonMappingException ex) {
            throw new MapperException(ex.getMessage());
        } catch (IOException ex) {
            throw new MapperException(ex.getMessage());
        }
    }

    /**
     * Mapped object has to have default constructor.
     */
    public Optional<Object> map(Object source) {
        if(mapping != null)
        try {
            Object result = source.getClass().getConstructor().newInstance();
            Field[] fields = source.getClass().getDeclaredFields();

            // TODO: add hot reloading (apache commons configuration)

            for (Field field : fields) {
                field.setAccessible(true);
                Optional<HashMap<String, String>> fieldMap = Optional.ofNullable(mapping.get(field.getName()));
                if (fieldMap.isPresent()) {
                    Optional<String> newFieldValue = Optional.ofNullable(fieldMap.get().get(field.get(source)));
                    field.set(result, newFieldValue.orElse((String) field.get(source)));
                } else
                    field.set(result, field.get(source));
            }
            return Optional.of(result);
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException ex) {
            log.debug(ex.getMessage());
        }
        return Optional.empty();
    }
}
