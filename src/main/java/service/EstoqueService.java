package service;

import java.io.Serializable;

import javax.inject.Inject;

import db.PedidoDAO;
import model.ItemPedido;
import model.Pedido;
import util.jpa.Transactional;

public class EstoqueService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transactional
	public void baixarItensEstoque(Pedido pedido) throws NegocioException {
		pedido = PedidoDAO.getInstance().porId(pedido.getId());
		
		for (ItemPedido item : pedido.getItens()) {
			item.getProduto().baixarEstoque(item.getQuantidade());
		}
	}

	public void retornarItensEstoque(Pedido pedido) {
		pedido = PedidoDAO.getInstance().porId(pedido.getId());
		
		for (ItemPedido item : pedido.getItens()) {
			item.getProduto().adicionarEstoque(item.getQuantidade());
		}
	}
	
}
