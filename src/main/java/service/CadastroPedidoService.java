package service;

import java.io.Serializable;
import java.util.Date;
import db.PedidoDAO;
import model.Pedido;
import model.StatusPedido;
import util.jpa.Transactional;

public class CadastroPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transactional
	public Pedido salvar(Pedido pedido) throws NegocioException {
		if (pedido.isNovo()) {
			pedido.setDataCriacao(new Date());
			pedido.setStatus(StatusPedido.ORCAMENTO);
		}
		
		pedido.recalcularValorTotal();
		
		if (pedido.isNaoAlteravel()) {
			throw new NegocioException("Pedido não pode ser alterado no status "
					+ pedido.getStatus().getDescricao() + ".");
		}
		
		if (pedido.getItens().isEmpty()) {
			throw new NegocioException("O pedido deve possuir pelo menos um item.");
		}
		
		if (pedido.isValorTotalNegativo()) {
			throw new NegocioException("Valor total do pedido não pode ser negativo.");
		}
		
		pedido = PedidoDAO.getInstance().guardar(pedido);
		return pedido;
	}
	
}
