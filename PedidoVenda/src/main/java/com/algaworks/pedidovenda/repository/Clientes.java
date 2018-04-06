package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.filter.ClienteFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Clientes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Cliente guardar(Cliente cliente) {
		return this.manager.merge(cliente);
	}
	
	@Transactional
	public void remover(Cliente cliente) {
		try {
			cliente = porId(cliente.getId());
			// marca objeto para exclusão
			manager.remove(cliente);
			
			// tudo que tiver pendente de execução será feito com o flush
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Cliente não pode ser excluído.");
		}
	}
	
	public Cliente porId(Long id) {
		return manager.find(Cliente.class, id);
	}

	public List<Cliente> porNome(String nome) {
		try {
			return manager.createQuery("from Cliente where upper(nome) like :nome", Cliente.class)
					.setParameter("nome", "%" + nome.toUpperCase() + "%").getResultList();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	public Cliente porDocumento(String documentoReceitaFederal) {
		try {
			return manager.createQuery("from Cliente where upper(documentoReceitaFederal) = :documentoReceitaFederal", Cliente.class)
					.setParameter("documentoReceitaFederal", documentoReceitaFederal).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public List<Cliente> filtrados(ClienteFilter filtro) {
		Session session = (Session) manager;
		/*Criteria criteria = session.createCriteria(Cliente.class);*/
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		EntityType<Cliente> type = manager.getMetamodel().entity(Cliente.class);
		Root<Cliente> root = query.from(Cliente.class);
		
		if (StringUtils.isNotBlank(filtro.getDocumentoReceitaFederal())) {
			query.where(builder.equal(
				builder.lower(
					root.get(
						/*type.getDeclaredSingularAttribute("documentoReceitaFederal", String.class)*/
						type.getDeclaredSingularAttribute("doc_receita_federal", String.class)
					)
				), "%" + filtro.getDocumentoReceitaFederal() + "%")
			);
			
			query.orderBy(builder.asc(root.get("nome")));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			query.where(builder.like(
				builder.lower(
					root.get(
						type.getDeclaredSingularAttribute("nome", String.class)
					)
				), "%" + filtro.getNome() + "%")
			);
			
			query.orderBy(builder.asc(root.get("nome")));
		}
		
		return manager.createQuery(query).getResultList();
		
		/*if (StringUtils.isNotBlank(filtro.getDocumentoReceitaFederal())) {
			criteria.add(Restrictions.eq("documentoReceitaFederal", filtro.getDocumentoReceitaFederal()));
		}

		// where nome like '%joao%'

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		return criteria.addOrder(Order.asc("nome")).list();*/
	}

}
