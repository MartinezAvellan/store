package br.com.quarktech.apirest.dto;

import java.io.Serializable;

import org.springframework.validation.Errors;

import lombok.Data;

@Data
public class ErroDTO implements Serializable {

	private static final long serialVersionUID = 3511515170246549730L;
	
	public ErroDTO(final Errors errors) {
		this.setField(errors.getFieldError().getField());
		this.setMessage(errors.getFieldError().getDefaultMessage());
	}
	
	private String field;
	private String message;

}