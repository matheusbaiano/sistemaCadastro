package managedbeans;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import model.Pedido;
import service.EmissaoPedidoService;
import service.NegocioException;
import util.jsf.FacesUtil;

@ManagedBean
@SessionScoped
public class EmissaoPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	
	public void emitirPedido(Pedido pedido) {
		System.out.println(pedido.getId()+" id do pedido");
		pedido.removerItemVazio();
		
		try {
			pedido = new EmissaoPedidoService().emitir(pedido);
			//this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(pedido));
			
			FacesUtil.addInfoMessage("Pedido emitido com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		} finally {
			pedido.adicionarItemVazio();
		}
	}
	
}
