package br.com.quarktech.apirest.resources;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import br.com.quarktech.apirest.models.Produto;
import br.com.quarktech.apirest.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/store")
@Api(value="API REST Produtos")
public class ProdutoResource {
	
	static Logger log = LoggerFactory.getLogger(ProdutoResource.class);
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@PostMapping("/produto")
	@ApiOperation(value = "Criar um produto", notes = "Deve ser passado os dados do produto para ser criado", response = Produto.class)
	public ResponseEntity<Object> salvarProduto(@RequestBody @Valid Produto produto) {
		log.info("salvando produtos...");
		try {			
			return new ResponseEntity<>(produtoRepository.save(produto), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}
	}	
	
	@ApiOperation(value="Retorna uma lista de Produtos")
	@GetMapping("/produtos")
	public ResponseEntity<Object> listarProdutos(){
		log.info("listando produtos...");
		try {			
			return new ResponseEntity<>((List<Produto>) produtoRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value="Retorna um produto unico")
	@GetMapping("/produto/{id}")
	public ResponseEntity<Object> listarProdutoUnico(@PathVariable(value="id") long id){
		log.info("listar produto por id: " + id);
		try {			
			final Optional<Produto> produto = produtoRepository.findById(Long.valueOf(id));
			if (!produto.isPresent()) {
				final MessageDTO messageDTO = new MessageDTO();
				messageDTO.setMessage("Produto inexistente para o id: " + id);
				log.info(messageDTO.getMessage());
				return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(produto.get(), HttpStatus.OK);			
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping("/produto/{id}")
	@ApiOperation(value = "Remover um produto", notes = "Deve ser passado o id do produto para ser removido logicamente", response = Produto.class)
	public ResponseEntity<Object> deletarProduto(@PathVariable(value="id") long id) {
		log.info("apagando produtos pelo id: " + id);
		try {			
			final MessageDTO messageDTO = new MessageDTO();
			final Optional<Produto> optional = produtoRepository.findById(Long.valueOf(id));
			if (!optional.isPresent()) {
				messageDTO.setMessage("Produto inexistente para o id: " + id);
				log.info(messageDTO.getMessage());
				return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			} else {
				final Produto produto = optional.get();
				produto.setApagado(true);
				produtoRepository.save(produto);
				messageDTO.setMessage("Produto removido");
				log.info(messageDTO.getMessage());
				return new ResponseEntity<>(messageDTO, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}			
	}
	
	@PutMapping("/produto")
	@ApiOperation(value = "Atualizar um produto", notes = "Deve ser passado os dados do produto para ser atualizado", response = Produto.class)
	public ResponseEntity<Object> atualizarProduto(@RequestBody @Valid Produto produto) {
		log.info("atualizando produto...");
		try {			
			return new ResponseEntity<>(produtoRepository.save(produto), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			final MessageDTO messageDTO = new MessageDTO();
    		messageDTO.setMessage("Ops, tivemos um contratempo, contate o administrador do sistema");
    		log.info(messageDTO.getMessage());
    		return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
		}			
	}
	 
}