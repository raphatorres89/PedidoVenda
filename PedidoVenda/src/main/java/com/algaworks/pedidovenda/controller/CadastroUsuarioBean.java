package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.service.CadastroUsuarioService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Grupos grupos;

	@Inject
	private CadastroUsuarioService cadastroUsuarioService;

	private Usuario usuario;
	private Grupo grupoSelecionado;

	private List<Grupo> gruposRaizes;

	public CadastroUsuarioBean() {
		limpar();
	}

	public void init() {
		if (FacesUtil.isNotPostback()) {
			gruposRaizes = grupos.raizes();
		}
	}

	public void limpar() {
		this.usuario = new Usuario();
		this.grupoSelecionado = new Grupo();
	}

	public void salvar() {
		this.usuario = cadastroUsuarioService.salvar(this.usuario);
		limpar();

		FacesUtil.addInfoMessage("Usuário cadastrado com sucesso!");
	}

	public void excluir() {
		this.usuario.getGrupos().remove(grupoSelecionado);

		FacesUtil.addInfoMessage("Grupo " + grupoSelecionado.getNome() + " excluído com sucesso.");
	}

	public void adicionarGrupo() {
		this.usuario.getGrupos().add(grupoSelecionado);
	}
	
	public boolean isEditando() {
		if (usuario == null) {
			limpar();
		}
		return this.usuario.getId() != null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Grupo> getGruposRaizes() {
		return gruposRaizes;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

}
