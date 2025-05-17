package com.thomasgreg.controller;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.thomasgreg.auth.controller.LoginController;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named
@RequestScoped
public class IndexController {

	@Inject
	private LoginController loginController;

	public void onPageLoad() {
		try {
			String redirectPage = loginController.existeUsuarioLogado() ? "/frontend/pages/gestao/"
					: "/frontend/pages/login/login.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(redirectPage);
		} catch (IOException e) {

		}
	}
}
