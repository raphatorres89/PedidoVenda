package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.filter.PedidoFilter;

public class Pedidos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	
	public List<Pedido> filtrados(PedidoFilter filtro) {
		// cria o builder
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		// cria a base da consulta
		CriteriaQuery<Pedido> criteriaQuery = builder.createQuery(Pedido.class);
		// cria a lista de argumentos
		List<Predicate> predicates = new ArrayList<>();
		
		// seta a raiz (entidade) - FROM
		Root<Pedido> pedidoRoot = criteriaQuery.from(Pedido.class);
		
		// faz o join com a categoria
		Join<Pedido, Usuario> vendedorJoin = pedidoRoot.join("vendedor", JoinType.INNER);
		Join<Pedido, Cliente> clienteJoin = pedidoRoot.join("cliente", JoinType.INNER);
		
		if (filtro.getNumeroDe() != null) {
			// id deve ser maior ou igual (ge = greater or equals) a filtro.numeroDe
			predicates.add(builder.ge(pedidoRoot.get("id"), filtro.getNumeroDe()));
		}
		if (filtro.getNumeroAte() != null) {
			// id deve ser menor ou igual (le = lower or equal) a filtro.numeroDe
			predicates.add(builder.le(pedidoRoot.get("id"), filtro.getNumeroAte()));
		}
		if (filtro.getDataCriacaoDe() != null) {
			predicates.add(builder.greaterThan(pedidoRoot.get("dataCriacao"), filtro.getDataCriacaoDe()));
		}
		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThan(pedidoRoot.get("dataCriacao"), filtro.getDataCriacaoAte()));
		}
		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			predicates.add(builder.like(builder.lower(clienteJoin.get("nome")), 
					"%" + filtro.getNomeCliente().toLowerCase() + "%"));
		}
		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {
			predicates.add(builder.like(builder.lower(vendedorJoin.get("nome")), 
					"%" + filtro.getNomeVendedor().toLowerCase() + "%"));
		}
		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			predicates.add(pedidoRoot.get("status").in(filtro.getStatuses()));
		}
		
		// SELECT FROM root(entidade)
		criteriaQuery.select(pedidoRoot);
		// WHERE
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		// ORDER BY
		criteriaQuery.orderBy(builder.asc(pedidoRoot.get("id")));
		
		// manda consultar
		TypedQuery<Pedido> query = manager.createQuery(criteriaQuery);
		
		return query.getResultList();
	}

	public Pedido guardar(Pedido pedido) {
		return manager.merge(pedido);
	}

	public Pedido porId(Long id) {
		return manager.find(Pedido.class, id);
	}

}
