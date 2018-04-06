import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.EnderecoEntrega;
import com.algaworks.pedidovenda.model.FormaPagamento;
import com.algaworks.pedidovenda.model.ItemPedido;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.model.Usuario;

public class TestePedido {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
		EntityManager manager = factory.createEntityManager();

		EntityTransaction trx = manager.getTransaction();
		trx.begin();
		
		Cliente cliente = manager.find(Cliente.class, 1L);
		Produto produto = manager.find(Produto.class, 1L);
		Usuario vendedor = manager.find(Usuario.class, 1L);
		
		EnderecoEntrega entrega = new EnderecoEntrega();
		entrega.setCep("94950-250");
		entrega.setCidade("Cachoeirinha");
		entrega.setComplemento("Casa");
		entrega.setLogradouro("Rua Maraú");
		entrega.setNumero("153");
		entrega.setUf("Rio Grande do Sul");

		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setDataCriacao(new Date());
		pedido.setDataEntrega(new Date());
		pedido.setEnderecoEntrega(entrega);
		pedido.setFormaPagamento(FormaPagamento.DINHEIRO);
		pedido.setObservacao("Entregar entre as 8:00 e 18:00");
		pedido.setStatus(StatusPedido.EMITIDO);
		pedido.setValorDesconto(new BigDecimal(0));
		pedido.setValorFrete(new BigDecimal(0));
		pedido.setValorTotal(new BigDecimal(30.00));
		pedido.setVendedor(vendedor);
		
		ItemPedido item = new ItemPedido();
		item.setPedido(pedido);
		item.setProduto(produto);
		item.setQuantidade(1);
		item.setValorUnitario(new BigDecimal(10.00));
		
		pedido.getItens().add(item);
		
		manager.persist(pedido);
		
		trx.commit();
	}
}
