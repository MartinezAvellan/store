package br.com.quarktech.apirest.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.quarktech.apirest.models.Produto;
import br.com.quarktech.apirest.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/store")
@Api(value="API REST Produtos")
public class ProdutoResource {
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@PostMapping("/produto")
	@ApiOperation(value = "Criar um produto", notes = "Deve ser passado os dados do produto para ser criado", response = Produto.class)
	public Produto salvarProduto(@RequestBody @Valid Produto produto) {
		return produtoRepository.save(produto);
	}	
	
	@ApiOperation(value="Retorna uma lista de Produtos")
	@GetMapping("/produtos")
	public List<Produto> listarProdutos(){
		return (List<Produto>) produtoRepository.findAll();
	}
	
	@ApiOperation(value="Retorna um produto unico")
	@GetMapping("/produto/{id}")
	public Produto listarProdutoUnico(@PathVariable(value="id") long id){
		return produtoRepository.findById(id);
	}
	
	@DeleteMapping("/produto")
	@ApiOperation(value = "Remover um produto", notes = "Deve ser passado os dados do produto para ser removido", response = Produto.class)
	public void deletarProduto(@RequestBody @Valid Produto produto) {
		produtoRepository.delete(produto);
	}
	
	@PutMapping("/produto")
	@ApiOperation(value = "Atualizar um produto", notes = "Deve ser passado os dados do produto para ser atualizado", response = Produto.class)
	public Produto atualizarProduto(@RequestBody @Valid Produto produto) {
		return produtoRepository.save(produto);
	}
	 
}
