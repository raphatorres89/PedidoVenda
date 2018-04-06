package com.algaworks.pedidovenda.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory factory;

	private EntityManagerProducer() {
		factory = Persistence.createEntityManagerFactory("PedidoPU");
	}

	/*
	 * Método produzido
	 *
	 * Tempo de vida = REQUISIÇÃO
	 * 
	 * Alterado para DEPENDENT, pois estava dando um bug sinistro
	 */
	@Produces
	@RequestScoped
	public Session createEntityManager() {
		return (Session) factory.createEntityManager();
	}

	/*
	 * Método com gatilho
	 * 
	 * Fecha automaticamente o Manager
	 */
	public void closeEntityManager(@Disposes Session manager) {
		manager.close();
	}
}
