package model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ClienteDataModel extends ListDataModel<Cliente> implements SelectableDataModel<Cliente>{
	public ClienteDataModel(){}
	public ClienteDataModel(List<Cliente> data){
		super(data);
	}
	@Override
	public Cliente getRowData(String rowKey) {
		List<Cliente> clientes = (List<Cliente>) getWrappedData();
		for (Cliente cli : clientes) {
			String key = cli.getId() + cli.getNome();
			if(key.equals(rowKey)){
				return cli;				
			}
		}
		return null;
	}
	@Override
	public Object getRowKey(Cliente cli) {		
		return (cli.getId() + cli.getNome());
	}
}