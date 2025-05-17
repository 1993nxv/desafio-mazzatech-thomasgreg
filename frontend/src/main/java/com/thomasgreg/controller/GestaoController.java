package com.thomasgreg.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.file.UploadedFile;

import com.thomasgreg.auth.controller.LoginController;
import com.thomasgreg.config.AppConfig;
import com.thomasgreg.dto.ClienteDTO;
import com.thomasgreg.dto.LogradouroDTO;
import com.thomasgreg.service.GestaoService;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class GestaoController implements Serializable {

	private static final long serialVersionUID = -4352995880801340890L;
	private static final String BASE_URL = AppConfig.get("api.base.url");
	
	@Getter @Setter
	private List<ClienteDTO> clientes;
	
	@Getter @Setter
	private ClienteDTO clienteSelecionado;

	@Getter @Setter
	private UploadedFile logotipoUpload;

	@Inject
	private LoginController loginController;

	@Inject
	private GestaoService gestaoService;

	@PostConstruct
	public void init() {
		if (loginController.existeUsuarioLogado()) {
			listarClientes();
		} else {
			redirecionaParaLogin();
		}
	}
	
	public void salvarOuAtualizarCliente() {
	    if (clienteSelecionado.getId() == null) {
	        salvarCliente();
	    } else {
//	        atualizarCliente();
	    }
	}

	public void salvarCliente() {
		gestaoService.salvarCliente(clienteSelecionado, logotipoUpload);
		listarClientes();
	}

	public void deletarCliente() {
		gestaoService.deletarClientePorId(clienteSelecionado.getId().toString());
		listarClientes();
	}

	public List<ClienteDTO> listarClientes() {
		List<ClienteDTO> clientesResponse = gestaoService.listarClientes();
		this.clientes = clientesResponse;
		return clientes;
	}
	
	public void novoClienteOpen() {
		this.clienteSelecionado = new ClienteDTO();
	}

	public void limparSelecao() {
		this.clienteSelecionado = null;
	}

	public void adicionarLogradouro() {
		if (clienteSelecionado.getLogradouros() == null) {
			clienteSelecionado.setLogradouros(new ArrayList<>());
		}
		LogradouroDTO novoLogradouro = new LogradouroDTO();
		novoLogradouro.setLogradouro("");
		clienteSelecionado.getLogradouros().add(novoLogradouro);
	}

	private void redirecionaParaLogin() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/frontend/pages/login/login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getBaseUrl() {
		return BASE_URL;
	}

}
