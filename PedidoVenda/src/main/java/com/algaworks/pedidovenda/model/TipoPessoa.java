package com.algaworks.pedidovenda.model;

public enum TipoPessoa {

	FISICA("Física"), 
	JURIDICA("Jurídica");

	String tipo;

	TipoPessoa(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return this.tipo;
	}
}