package br.com.quarktech.apirest.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="PEDIDO")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 2941962746798519816L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String nomeCliente;
	private String telefone;
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Produto> produto;
	private BigDecimal valorProdutos = new BigDecimal(0);
	private BigDecimal desconto = new BigDecimal(0);
	private BigDecimal valorTotal = new BigDecimal(0);
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = this.valorProdutos.subtract(this.desconto.multiply(this.valorProdutos).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

}