package com.fsm.dscatalog.services.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.fsm.dscatalog.dto.UserInsertDTO;
import com.fsm.dscatalog.dto.UserUpdateDTO;
import com.fsm.dscatalog.entities.User;
import com.fsm.dscatalog.repositories.UserRepository;
import com.fsm.dscatalog.resources.exception.FieldMessage;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	@Autowired
	UserRepository repository;
	
	@Autowired
	HttpServletRequest request;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}
	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		@SuppressWarnings("unchecked")
		var uriVars =(Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Long userId = Long.parseLong(uriVars.get("id"));
		User user = repository.findByEmail(dto.getEmail());
		
		if (user != null && user.getId() != userId) {
			list.add(new FieldMessage("email","email ja existe"));
		}
				
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getField())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
