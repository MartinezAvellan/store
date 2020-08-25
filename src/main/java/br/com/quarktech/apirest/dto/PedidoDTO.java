package br.com.quarktech.apirest.dto;

import java.io.Serializable;
import java.util.List;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;


@Data
public class PedidoDTO implements Serializable {

	private static final long serialVersionUID = 3499412525193272963L;
	
	@JsonProperty(access = Access.READ_ONLY)
	private long id;

	@NotNull(message = "{nome.not.empty}")
	private String nomeCliente;
	
	@NotNull(message = "{telefone.not.empty}")
	private String telefone;
	
	@NotNull(message = "{produtos.not.empty}")
	private List<Object> produtos;
	
	@NotNull(message = "{desconto.not.empty}")
	@PositiveOrZero(message = "{desconto.positive}")
	private int desconto;
	
	@JsonProperty(access = Access.READ_ONLY)
	private BigDecimal valorProdutos;
	
	@JsonProperty(access = Access.READ_ONLY)
	private BigDecimal valorTotal;
	
}