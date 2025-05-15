package com.thomasgreg.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.modelmapper.ModelMapper;

public class ModelMapperConfig {

    @Produces
    @ApplicationScoped
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.getConfiguration()
            .setSkipNullEnabled(true)
            .setPreferNestedProperties(false);
       
        return modelMapper;
    }
}
