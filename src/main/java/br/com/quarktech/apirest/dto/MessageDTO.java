package br.com.quarktech.apirest.dto;

import java.io.Serializable;

import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDTO implements Serializable {

	private static final long serialVersionUID = 3511515170246549730L;
	
	public MessageDTO() {}
	public MessageDTO(final Errors errors) {
		this.setField(errors.getFieldError().getField());
		this.setMessage(errors.getFieldError().getDefaultMessage());
	}
	
	private String field;
	private String message;

}