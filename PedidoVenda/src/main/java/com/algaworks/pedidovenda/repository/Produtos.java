package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Produtos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Transactional
	public void remover(Produto produto) {
		try {
			produto = porId(produto.getId());
			// marca objeto para exclusão
			manager.remove(produto);

			// tudo que tiver pendente de execução será feito com o flush
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído.");
		}
	}

	public Produto guardar(Produto produto) {
		return manager.merge(produto);
	}

	public Produto porSku(String sku) {
		try {
			return manager.createQuery("from Produto where upper(sku) = :sku", Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	
	public List<Produto> filtrados(ProdutoFilter filtro) {
		// construtor
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		// consultor
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);
		// lista de atributos
		List<Predicate> predicates = new ArrayList<>();
		
		// entidade - FROM
		Root<Produto> produtoRoot = criteriaQuery.from(Produto.class);
		// JOIN produto e categoria
		Fetch<Produto, Categoria> categoriaJoin = produtoRoot.fetch("categoria", JoinType.INNER);
		// JOIN categoriaPai e categoria
		categoriaJoin.fetch("categoriaPai", JoinType.INNER);
		
		// atributos
		if (StringUtils.isNotBlank(filtro.getSku())) {
			predicates.add(builder.equal(produtoRoot.get("sku"), filtro.getSku()));
		}
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(produtoRoot.get("nome")), 
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		// SELECT
		criteriaQuery.select(produtoRoot);
		// WHERE
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		// ORDER BY
		criteriaQuery.orderBy(builder.asc(produtoRoot.get("id")));
		
		TypedQuery<Produto> query = manager.createQuery(criteriaQuery);
		
		return query.getResultList();
	}

	public Produto porId(Long id) {
		return manager.find(Produto.class, id);
	}

	public List<Produto> porNome(String nome) {
		return manager.createQuery("from Produto where upper(nome) like :nome", Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

}
