package br.com.quarktech.apirest.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.quarktech.apirest.dto.MessageDTO;
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
	
	static Logger log = LoggerFactory.getLogger(PedidoResource.class);
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	PedidoRepository pedidoRepository;

	@PostMapping("/pedido")
	@ApiOperation(value = "Criar um pedido", notes = "Deve ser passado os dados do pedido e produtos para ser criado", response = Pedido.class)
	public ResponseEntity<Object> salvarPedido(@RequestBody @Valid final PedidoDTO pedidoDTO, final Errors errors) {
		log.info("salvando pedido...");
		try {		
		    if (errors.hasErrors()) {
				   return new ResponseEntity<>(new MessageDTO(errors), HttpStatus.BAD_REQUEST);
			    }
			    final Pedido pedido = new Pedido();
			    BeanUtils.copyProperties(pedidoDTO, pedido);
			    final List<Produto> produtos = new ArrayList<>();
			    for (Object id : pedidoDTO.getProdutos()) {
			    	Optional<Produto> optional = produtoRepository.findById(Long.valueOf(String.valueOf(id)));
			    	if (optional.isPresent()) {
			    		final Produto produto = optional.get();
			    		final int quantidade = produto.getQuantidade();
			    		if(quantidade == 0) {
				    		final MessageDTO messageDTO = new MessageDTO();
				    		messageDTO.setField("produtos");
				    		messageDTO.setMessage("Nao foi possivel criar seu pedido, produto id: " + id + " sem estoque");
				    		log.info(messageDTO.getMessage());
				    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			    		} else {
			    			produto.setQuantidade(quantidade - 1);
			    		}
			    		produtos.add(produto);
			    		pedido.setValorProdutos(pedido.getValorProdutos().add(produto.getPreco()));
			    	} else {
			    		final MessageDTO messageDTO = new MessageDTO();
			    		messageDTO.setField("produtos");
			    		messageDTO.setMessage("Nao foi possivel criar seu pedido, produto inexistente para o id: " + id);
			    		log.info(messageDTO.getMessage());
			    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			    	}
			    }
			    pedido.setValorTotal(pedido.getValorProdutos());
			    pedido.setProduto(produtos);
				final Pedido save = pedidoRepository.save(pedido);
				return new ResponseEntity<>(save, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}			
	}	
	
	@ApiOperation(value="Retorna uma lista de pedidos")
	@GetMapping("/pedidos")
	public ResponseEntity<Object> listarPedidos(){ 
		log.info("listando pedidos...");
		try {		
			return new ResponseEntity<>((List<Pedido>) pedidoRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}			
	}
	
	@ApiOperation(value="Retorna um pedido unico")
	@GetMapping("/pedido/{id}")
	public ResponseEntity<Object> listarPedidoUnico(@PathVariable(value="id") long id){
		log.info("pesquisando pedido pelo id: " + id);
		try {			
			final Optional<Pedido> optional = pedidoRepository.findById(Long.valueOf(id));
			if (!optional.isPresent()) {
				final MessageDTO messageDTO = new MessageDTO();
				messageDTO.setMessage("Pedido inexistente para o id: " + id);
				log.info(messageDTO.getMessage());
				return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(optional.get(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}	
	}
	
	@DeleteMapping("/pedido/{id}")
	@ApiOperation(value = "Remover um produto", notes = "Deve ser passado os dados do pedido para ser removido", response = Pedido.class)
	public ResponseEntity<Object> deletarPedido(@PathVariable(value="id") long id) {	
		log.info("removendo pedido: " + id);
		final MessageDTO messageDTO = new MessageDTO();
		try {			
			final Optional<Pedido> optional = pedidoRepository.findById(Long.valueOf(id));
			if (!optional.isPresent()) {
				messageDTO.setMessage("Pedido inexistente para o id: " + id);
				log.info(messageDTO.getMessage());
				return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			} else {
				final Pedido pedido = optional.get();
				pedido.getProduto().clear();
				pedidoRepository.delete(pedido);
				messageDTO.setMessage("Pedido apagado");
				log.info(messageDTO.getMessage());
				return new ResponseEntity<>(messageDTO, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}			
	}
	
	@PutMapping("/pedido")
	@ApiOperation(value = "Atualizar um pedido", notes = "Deve ser passado os dados do pedido para ser atualizado", response = Pedido.class)
	public ResponseEntity<Object> atualizarPedido(@RequestBody @Valid Pedido pedido) {
		log.info("atualizando pedido...");
		try {			
			final List<Produto> produtos = new ArrayList<>();
			for (final Produto p : pedido.getProduto()) {
				produtos.add(produtoRepository.findById(p.getId()));
			}
			pedido.setProduto(produtos);
			return new ResponseEntity<>(pedidoRepository.save(pedido), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}		
	}
	
}
