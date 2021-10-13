package com.fsm.dscatalog.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidateError extends StandardError{

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> erros = new ArrayList<>();

	public List<FieldMessage> getErros() {
		return erros;
	}
	
	public void addFieldMessage(String field, String message) {
		erros.add(new FieldMessage(field,message));
	}

}
