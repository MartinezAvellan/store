package br.com.quarktech.apirest.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.quarktech.apirest.models.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {
	Produto findById(long id);
}