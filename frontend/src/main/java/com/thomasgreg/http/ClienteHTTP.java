package com.thomasgreg.http;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataWriter;

import com.thomasgreg.config.AppConfig;

@ApplicationScoped
public class ClienteHTTP {
    private static final String BASE_URL = AppConfig.get("api.base.url");

    private final ResteasyClient clienteHTTP;

    public ClienteHTTP() {
        this.clienteHTTP = new ResteasyClientBuilder()
                .register(MultipartFormDataWriter.class)
                .build();
    }

    public ResteasyWebTarget target(String path) {
        return clienteHTTP.target(BASE_URL).path(path);
    }
    
    public void close() {
        clienteHTTP.close();
    }
}
