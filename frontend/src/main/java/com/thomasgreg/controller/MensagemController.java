package com.thomasgreg.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@ApplicationScoped
public class MensagemController {
	
	public void addMensagemInfo(String resumo, String detalhe) {
	    FacesContext.getCurrentInstance().addMessage(null,
	        new FacesMessage(FacesMessage.SEVERITY_INFO, resumo, detalhe));
	}

	public void addMensagemErro(String resumo, String detalhe) {
	    FacesContext.getCurrentInstance().addMessage(null,
	        new FacesMessage(FacesMessage.SEVERITY_ERROR, resumo, detalhe));
	}
	
	public void addMensagemAviso(String resumo, String detalhe) {
	    FacesContext.getCurrentInstance().addMessage(
	        null, new FacesMessage(FacesMessage.SEVERITY_WARN, resumo, detalhe));
	}
	
}
