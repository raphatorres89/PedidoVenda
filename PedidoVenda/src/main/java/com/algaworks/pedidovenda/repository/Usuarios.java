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

import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Usuario porNome(String nome) {
		try {
			return manager.createQuery("from Usuario where upper(nome) = :nome", Usuario.class)
					.setParameter("nome", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Usuario> vendedores() {
		return this.manager.createQuery("from Usuario", Usuario.class)
				.getResultList();
	}

	public List<Usuario> usuariosFiltrados(String nome) {
		Session session = manager.unwrap(Session.class);
		/*Criteria criteria = session.createCriteria(Usuario.class);
		// where nome like '%joao%'
		if (StringUtils.isNotBlank(nome)) {
			criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));
		}
		return criteria.addOrder(Order.asc("nome")).list();*/
		CriteriaBuilder builder = session.getCriteriaBuilder();
		
		CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
		EntityType<Usuario> type = manager.getMetamodel().entity(Usuario.class);
		Root<Usuario> root = query.from(Usuario.class);
		
		if (StringUtils.isNotBlank(nome)) {
			query.where(builder.like(
				builder.lower(
					root.get(
						type.getDeclaredSingularAttribute("nome", String.class)
					)
				), "%" + nome + "%")
			);
			
			query.orderBy(builder.asc(root.get("nome")));
		}
		return manager.createQuery(query).getResultList();
	}

	public Usuario guardar(Usuario usuario) {
		return manager.merge(usuario);
	}

	public Usuario porId(Long id) {
		return manager.find(Usuario.class, id);
	}

	@Transactional
	public void remover(Usuario usuario) {
		try {
			usuario = porId(usuario.getId());
			manager.remove(usuario);

			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Usuário não pode ser excluído.");
		}
	}

}
