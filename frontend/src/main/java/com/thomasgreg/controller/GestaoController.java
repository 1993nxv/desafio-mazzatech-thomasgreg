package com.thomasgreg.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.modelmapper.ModelMapper;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.thomasgreg.config.AppConfig;
import com.thomasgreg.dto.ClienteResponseDTO;
import com.thomasgreg.model.Cliente;
import com.thomasgreg.model.Logradouro;
import com.thomasgreg.service.GestaoService;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class GestaoController implements Serializable {

	private static final long serialVersionUID = -4352995880801340890L;
	private static final String BASE_URL = AppConfig.get("api.base.url");

	private List<Cliente> clientes;

	private Cliente clienteSelecionado;

	@Getter
	@Setter
	private UploadedFile logotipoUpload;

	@Inject
	private LoginController loginController;

	@Inject
	private GestaoService gestaoService;

	@Inject
	private ModelMapper modelMapper;

	@PostConstruct
	public void init() {
		try {
			if (loginController.existeUsuarioLogado()) {
				listarClientes();
			} else {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/frontend/pages/login/login.xhtml");
			}
		} catch (IOException e) {
		}
	}

	public void salvarCliente() {
		try {
			gestaoService.salvarCliente(clienteSelecionado, logotipoUpload);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente salvo com sucesso!"));
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

	public List<Cliente> listarClientes() {
		List<ClienteResponseDTO> dtos = gestaoService.listarClientes();
		this.clientes = dtos.stream().map(dto -> modelMapper.map(dto, Cliente.class)).collect(Collectors.toList());
		return clientes;
	}

	public void novoClienteOpen() {
		this.clienteSelecionado = new Cliente();
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente cliente) {
		this.clienteSelecionado = cliente;
	}

	public void limparSelecao() {
		this.clienteSelecionado = null;
	}

	public void adicionarLogradouro() {
		if (clienteSelecionado.getLogradouros() == null) {
			clienteSelecionado.setLogradouros(new ArrayList<>());
		}
		Logradouro novoLogradouro = new Logradouro();
		novoLogradouro.setLogradouro("");
		clienteSelecionado.getLogradouros().add(novoLogradouro);
	}

	public StreamedContent getPreviewLogotipo() {
		if (logotipoUpload != null && logotipoUpload.getSize() > 0) {
			try {
				return DefaultStreamedContent.builder().name(logotipoUpload.getFileName())
						.contentType(logotipoUpload.getContentType()).stream(() -> {
							try {
								return logotipoUpload.getInputStream();
							} catch (IOException e) {
								e.printStackTrace();
								return null;
							}
						}).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getBaseUrl() {
		return BASE_URL;
	}

}
