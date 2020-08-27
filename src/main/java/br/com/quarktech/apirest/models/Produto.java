package br.com.quarktech.apirest.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="PRODUTO")
public class Produto implements Serializable {
	
	private static final long serialVersionUID = 1155907789634934190L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotBlank(message = "Descricao e obrigatoria e string")
	private String descricao;
	
	@NotBlank(message = "SKU e obrigatoria e string")
	private String sku;
	
	@NotNull(message = "Peso e obrigatorio e decimal")
	private BigDecimal peso;
	
	@NotNull(message = "Altura e obrigatorio e decimal")
	private BigDecimal altura;
	
	@NotNull(message = "Largura e obrigatorio e decimal")
	private BigDecimal largura;
	
	@NotNull(message = "Profundidade e obrigatorio e decimal")
	private BigDecimal profundidade;
	
	@NotNull(message = "Fabricante e obrigatorio e string")
	private String fabricante;
	
	@NotNull(message = "Preco e obrigatorio e secimal")
	private BigDecimal preco;
	
	@NotNull(message = "Quantidade e obrigatorio e inteiro")
	private int quantidade;
	
	private boolean apagado = false;	
    
}