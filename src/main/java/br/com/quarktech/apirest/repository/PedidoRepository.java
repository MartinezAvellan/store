package br.com.quarktech.apirest.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.quarktech.apirest.models.Pedido;

public interface PedidoRepository  extends CrudRepository<Pedido, Long> {
	Pedido findById(long id);
}