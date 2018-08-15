package managedbeans;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import model.Pedido;
import service.CancelamentoPedidoService;
import service.NegocioException;
import util.jsf.FacesUtil;

@ManagedBean
@RequestScoped
public class CancelamentoPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	
	public void cancelarPedido(Pedido pedido) {
		try {
			pedido = new CancelamentoPedidoService().cancelar(pedido);
			//this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(pedido));
			FacesUtil.addInfoMessage("Pedido cancelado com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
}
