package com.algaworks.pedidovenda.util.cdi;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CDIServiceLocator {

	private static BeanManager getBeanManager() {
		try {
			InitialContext ic = new InitialContext();
			return (BeanManager) ic.lookup("java:comp/env/BeanManager");
		} catch (NamingException n) {
			throw new RuntimeException("Não pôde encontrar BeanManager no JNDI.");
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> claz) {
		BeanManager bm = getBeanManager();
		Set<Bean<?>> beans = (Set<Bean<?>>) bm.getBeans(claz);

		if (beans == null || beans.isEmpty()) {
			return null;
		}

		Bean<T> bean = (Bean<T>) beans.iterator().next();

		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		T o = (T) bm.getReference(bean, claz, ctx);

		return o;
	}
}
