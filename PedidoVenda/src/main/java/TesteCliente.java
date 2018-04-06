import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.model.TipoPessoa;


public class TesteCliente {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
		EntityManager manager = factory.createEntityManager();
		
		EntityTransaction trx = manager.getTransaction();
		trx.begin();
		
		Cliente cliente = new Cliente();
		cliente.setNome("Raphael Torres");
		cliente.setEmail("raphael.torres@hardlink.com.br");
		cliente.setDocumentoReceitaFederal("123.123.123-12");
		cliente.setTipo(TipoPessoa.FISICA);
		
		Endereco endereco = new Endereco();
		endereco.setLogradouro("Rua Marau");
		endereco.setNumero("153");
		endereco.setCidade("Cachoeirinha");
		endereco.setUf("RS");
		endereco.setCep("94950-250");
		endereco.setCliente(cliente);
		
		cliente.getEnderecos().add(endereco);
		
		manager.merge(cliente);
		
		trx.commit();
	}
	
}
