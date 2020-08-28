package br.com.quarktech.apirest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;


@Data
public class PedidoDTO implements Serializable {

	private static final long serialVersionUID = 3499412525193272963L;
	
	@NotNull(message = "O campo nomeCliente nao pode ser vazio")
	private String nomeCliente;
	
	@NotNull(message = "O campo telefone nao pode ser vazio")
	private String telefone;
	
	@NotNull(message = "O campo lista de produtos nao pode ser vazia")
	private List<Object> produtos;
	
	@NotNull(message = "O campo desconto nao pode ser vazio")
	@PositiveOrZero(message = "O campo desconto tem de ser um valor positivo")
	private BigDecimal desconto;
	
}