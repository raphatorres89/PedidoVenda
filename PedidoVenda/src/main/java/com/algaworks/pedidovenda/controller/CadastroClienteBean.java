package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.model.TipoPessoa;
import com.algaworks.pedidovenda.service.CadastroClienteService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*@Inject
	private Clientes clientes;*/

	@Inject
	private CadastroClienteService clienteService;

	private Cliente cliente;
	
	private Endereco endereco;
	
	private Endereco enderecoSelecionado;

	private List<Endereco> enderecos;

	public CadastroClienteBean() {
		limpar();
	}
	
	public void init() {
		if (FacesUtil.isNotPostback()) {
			if (this.cliente == null) {
				limpar();
			} else {
				this.enderecos = this.cliente.getEnderecos();
			}
		}
	}
	
	public boolean isEditando() {
		if (cliente == null) {
			limpar();
		}
		return this.cliente.getId() != null;
	}

	public void salvar() {
		this.cliente.setEnderecos(this.enderecos);
		this.cliente = clienteService.salvar(cliente);
		FacesUtil.addInfoMessage("Cliente " + cliente.getNome() + " salvo com sucesso.");
		
		limpar();
	}
	
	public void inserirEndereco() {
		this.endereco.setCliente(this.cliente);
		this.enderecos.add(this.endereco);
		
		this.endereco = new Endereco();
	}

	public void limpar() {
		this.cliente = new Cliente();
		this.endereco = new Endereco();
		this.enderecos = new ArrayList<Endereco>();
	}
	
	public void excluirEndereco() {
		this.enderecos.remove(enderecoSelecionado);
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoPessoa[] getTipoPessoa() {
		return TipoPessoa.values();
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Endereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(Endereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}

}
