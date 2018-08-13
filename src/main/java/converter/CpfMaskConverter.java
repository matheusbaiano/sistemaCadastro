package converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("cpfMask")
public class CpfMaskConverter implements Converter {
	
	Pattern CPF_PATTERN = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
	String CPF_REPLACEMENT = "$1.$2.$3-$4";
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null) {
			value = value.replaceAll("[\\.\\-/]", "");
		}
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String resultado = value.toString();
		
		Matcher cpfMatcher = CPF_PATTERN.matcher(resultado);
        
        if (cpfMatcher.matches()) {
            resultado = cpfMatcher.replaceAll(CPF_REPLACEMENT);
        }
		
		return resultado;	
	}

}