package br.com.quarktech.apirest.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.quarktech.apirest.dto.ErroDTO;
import br.com.quarktech.apirest.dto.PedidoDTO;
import br.com.quarktech.apirest.models.Pedido;
import br.com.quarktech.apirest.models.Produto;
import br.com.quarktech.apirest.repository.PedidoRepository;
import br.com.quarktech.apirest.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/store")
@Api(value="API REST Pedidos")
public class PedidoResource {
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	PedidoRepository pedidoRepository;

	@PostMapping("/pedido")
	@ApiOperation(value = "Criar um pedido", notes = "Deve ser passado os dados do pedido e produtos para ser criado", response = Pedido.class)
	public ResponseEntity<Object> salvarPedido(@RequestBody @Valid final PedidoDTO pedidoDTO, final Errors errors) {
	    if (errors.hasErrors()) {
		   return new ResponseEntity<>(new ErroDTO(errors), HttpStatus.BAD_REQUEST);
	    }
	    final Pedido pedido = new Pedido();
	    BeanUtils.copyProperties(pedidoDTO, pedido);
	    final List<Produto> produtos = new ArrayList<>();
	    for (Object id : pedidoDTO.getProdutos()) {
	    	Optional<Produto> produto = produtoRepository.findById(Long.valueOf(String.valueOf(id)));
	    	if (produto.isPresent()) {
	    		produtos.add(produto.get());
	    	}
	    }
	    pedido.setValorProdutos(new BigDecimal("100.0"));
	    pedido.setValorTotal(new BigDecimal("50.5"));
	    pedido.setProdutos(produtos);
		final Pedido save = pedidoRepository.save(pedido);
		return new ResponseEntity<>(save, HttpStatus.OK);
	}	
	
	@ApiOperation(value="Retorna uma lista de pedidos")
	@GetMapping("/pedidos")
	public List<Pedido> listarPedidos(){ 
		return (List<Pedido>) pedidoRepository.findAll();
	}
	
	@ApiOperation(value="Retorna um pedido unico")
	@GetMapping("/pedido/{id}")
	public Pedido listarPedidoUnico(@PathVariable(value="id") long id){
		return pedidoRepository.findById(id);
	}
	
	@DeleteMapping("/pedido")
	@ApiOperation(value = "Remover um produto", notes = "Deve ser passado os dados do pedido para ser removido", response = Pedido.class)
	public void deletarPedido(@RequestBody @Valid Pedido pedido) {
		pedidoRepository.delete(pedido);
	}
	
	@PutMapping("/pedido")
	@ApiOperation(value = "Atualizar um pedido", notes = "Deve ser passado os dados do pedido para ser atualizado", response = Pedido.class)
	public Pedido atualizarPedido(@RequestBody @Valid Pedido pedido) {
		return pedidoRepository.save(pedido);
	}
	
}
