package com.thomasgreg.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.file.UploadedFile;

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

	private List<ClienteDTO> clientes;

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
		try {
			gestaoService.salvarCliente(clienteSelecionado, logotipoUpload);
			listarClientes();
		} catch (Exception e) {
			String erro = e.getMessage() != null ? e.getMessage() : "Erro inesperado";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar cliente", erro));
			e.printStackTrace();
		}
	}

	public void deletarCliente() {
		try {
			gestaoService.deletarClientePorId(clienteSelecionado.getId().toString());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente deletado"));
			listarClientes();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Não foi possível deletar o cliente: " + e.getMessage()));
		}
	}

	public List<ClienteDTO> listarClientes() {
		List<ClienteDTO> clientesResponse = gestaoService.listarClientes();
		this.clientes = clientesResponse;
		return clientes;
	}

	public void novoClienteOpen() {
		this.clienteSelecionado = new ClienteDTO();
	}

	public List<ClienteDTO> getClientes() {
		return clientes;
	}

	public ClienteDTO getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(ClienteDTO cliente) {
		this.clienteSelecionado = cliente;
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

	public String getBaseUrl() {
		return BASE_URL;
	}

	private void redirecionaParaLogin() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/frontend/pages/login/login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
