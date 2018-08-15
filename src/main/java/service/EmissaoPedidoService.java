package service;

import java.io.Serializable;

import javax.inject.Inject;

import db.PedidoDAO;
import model.Pedido;
import model.StatusPedido;
import util.jpa.Transactional;

public class EmissaoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Transactional
	public Pedido emitir(Pedido pedido) throws NegocioException {
		pedido = new CadastroPedidoService().salvar(pedido);
		
		if (pedido.isNaoEmissivel()) {
			throw new NegocioException("Pedido não pode ser emitido com status "
					+ pedido.getStatus().getDescricao() + ".");
		}
		
		new EstoqueService().baixarItensEstoque(pedido);
		
		pedido.setStatus(StatusPedido.EMITIDO);
		
		pedido = PedidoDAO.getInstance().guardar(pedido);
		
		return pedido;
	}
	
}
