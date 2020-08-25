package br.com.quarktech.apirest.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="PEDIDO")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 2941962746798519816L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@NotBlank(message = "nome cliente e obrigatorio")
	private String nomeCliente;
	
	@NotBlank(message = "telefone e obrigatorio")
	private String telefone;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Produto> produtos;
	
	@NotNull
	private BigDecimal valorProdutos;
	
	@NotNull
	private int desconto;
	
	@NotNull
	private BigDecimal valorTotal;
	
}