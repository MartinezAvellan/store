package br.com.quarktech.apirest.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.quarktech.apirest.dto.MessageDTO;
import br.com.quarktech.apirest.dto.PedidoDTO;
import br.com.quarktech.apirest.models.Pedido;
import br.com.quarktech.apirest.models.Produto;
import br.com.quarktech.apirest.repository.PedidoRepository;
import br.com.quarktech.apirest.repository.ProdutoRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PedidoResource.class)
public class PedidoResourceTest {
	

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private PedidoRepository pedidoRepository;
	
	@MockBean
	private ProdutoRepository produtoRepository;


	@Test
	public void testSalvarPedido() throws Exception {
		
		
		String jsonProduto = "    {\r\n" + "        \"id\": 1,\r\n" + "        \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n" + "        \"apagado\": false\r\n"
				+ "    }";
		

		Produto produto = new Gson().fromJson(jsonProduto, Produto.class);		
        when(produtoRepository.findById(any(Long.class))).thenReturn(Optional.of(produto));
		
		String jsonPedido = "{\r\n" + 
				"    \"id\": 1,\r\n" + 
				"    \"nomeCliente\": \"Hugo aguiar\",\r\n" + 
				"    \"telefone\": \"+55 (11) 98945-0870\",\r\n" + 
				"    \"produto\": [\r\n" + 
				"        {\r\n" + 
				"            \"id\": 1,\r\n" + 
				"            \"descricao\": \"PRODUTO 1\",\r\n" + 
				"            \"sku\": \"SKU-0500-100\",\r\n" + 
				"            \"peso\": 200.00,\r\n" + 
				"            \"altura\": 55.50,\r\n" + 
				"            \"largura\": 12.50,\r\n" + 
				"            \"profundidade\": 56.20,\r\n" + 
				"            \"fabricante\": \"HUGOS COMPANIES\",\r\n" + 
				"            \"preco\": 100.0,\r\n" + 
				"            \"quantidade\": 31,\r\n" + 
				"            \"apagado\": true\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"valorProdutos\": 100.00,\r\n" + 
				"    \"desconto\": 15.00,\r\n" + 
				"    \"valorTotal\": 85.00\r\n" + 
				"}";
		
		Pedido pedido = new Gson().fromJson(jsonPedido, Pedido.class);		
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);	
        
		String jsonBody = "{\r\n" + 
				"   \"nomeCliente\":\"Hugo\",\r\n" + 
				"   \"telefone\":\"+55 (11) 98945-0870\",\r\n" + 
				"   \"produtos\":[\"1\"],\r\n" + 
				"   \"desconto\": 15\r\n" + 
				"}";
		
		PedidoDTO body = new Gson().fromJson(jsonBody, PedidoDTO.class);        

		MvcResult result = mockMvc.perform(post("/store/pedido")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(objectMapper.writeValueAsString(body))
		        .characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andDo(print())
		        .andReturn();
				
        verify(pedidoRepository).save(any(Pedido.class));
        Pedido p = new Gson().fromJson(result.getResponse().getContentAsString(), Pedido.class);
        assertNotNull(p);
        assertNotEquals(new BigDecimal(85.0), p.getValorTotal(), "Desconto nao esta de acordo com o esperado");
	}

	@Test
	public void testListarPedidos() throws Exception {
		
		String jsonPedido = "[{\r\n" + 
				"    \"id\": 1,\r\n" + 
				"    \"nomeCliente\": \"Hugo aguiar\",\r\n" + 
				"    \"telefone\": \"+55 (11) 98945-0870\",\r\n" + 
				"    \"produto\": [\r\n" + 
				"        {\r\n" + 
				"            \"id\": 1,\r\n" + 
				"            \"descricao\": \"PRODUTO 1\",\r\n" + 
				"            \"sku\": \"SKU-0500-100\",\r\n" + 
				"            \"peso\": 200.00,\r\n" + 
				"            \"altura\": 55.50,\r\n" + 
				"            \"largura\": 12.50,\r\n" + 
				"            \"profundidade\": 56.20,\r\n" + 
				"            \"fabricante\": \"HUGOS COMPANIES\",\r\n" + 
				"            \"preco\": 100.0,\r\n" + 
				"            \"quantidade\": 31,\r\n" + 
				"            \"apagado\": true\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"valorProdutos\": 100.00,\r\n" + 
				"    \"desconto\": 15.00,\r\n" + 
				"    \"valorTotal\": 85.00\r\n" + 
				"}]";
		
		Type type = new TypeToken<List<Pedido>>(){}.getType();
		List<Pedido> pedido = new Gson().fromJson(jsonPedido, type);		
        when(pedidoRepository.findAll()).thenReturn(pedido);	
		
		MvcResult result = mockMvc.perform(get("/store/pedidos")
		.characterEncoding("utf-8"))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();

		verify(pedidoRepository).findAll();
        List<Pedido> pedidos = new Gson().fromJson(result.getResponse().getContentAsString(), type);
        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
	}

	@Test
	public void testListarPedidoUnico() throws Exception {
		String jsonPedido = "{\r\n" + 
				"    \"id\": 1,\r\n" + 
				"    \"nomeCliente\": \"Hugo aguiar\",\r\n" + 
				"    \"telefone\": \"+55 (11) 98945-0870\",\r\n" + 
				"    \"produto\": [\r\n" + 
				"        {\r\n" + 
				"            \"id\": 1,\r\n" + 
				"            \"descricao\": \"PRODUTO 1\",\r\n" + 
				"            \"sku\": \"SKU-0500-100\",\r\n" + 
				"            \"peso\": 200.00,\r\n" + 
				"            \"altura\": 55.50,\r\n" + 
				"            \"largura\": 12.50,\r\n" + 
				"            \"profundidade\": 56.20,\r\n" + 
				"            \"fabricante\": \"HUGOS COMPANIES\",\r\n" + 
				"            \"preco\": 100.0,\r\n" + 
				"            \"quantidade\": 31,\r\n" + 
				"            \"apagado\": true\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"valorProdutos\": 100.00,\r\n" + 
				"    \"desconto\": 15.00,\r\n" + 
				"    \"valorTotal\": 85.00\r\n" + 
				"}";
		

		Pedido pedido = new Gson().fromJson(jsonPedido, Pedido.class);		
        when(pedidoRepository.findById(any(Long.class))).thenReturn(Optional.of(pedido));
		
		MvcResult result = mockMvc.perform(get("/store/pedido/1")
		.characterEncoding("utf-8"))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();
		
		verify(pedidoRepository).findById(any(Long.class));
		Pedido pedidos = new Gson().fromJson(result.getResponse().getContentAsString(), Pedido.class);
        assertNotNull(pedidos);
        assertEquals(1L, pedidos.getId());
	}

	@Test
	public void testDeletarPedido() throws Exception {
		String jsonPedido = "{\r\n" + 
				"    \"id\": 1,\r\n" + 
				"    \"nomeCliente\": \"Hugo aguiar\",\r\n" + 
				"    \"telefone\": \"+55 (11) 98945-0870\",\r\n" + 
				"    \"produto\": [\r\n" + 
				"        {\r\n" + 
				"            \"id\": 1,\r\n" + 
				"            \"descricao\": \"PRODUTO 1\",\r\n" + 
				"            \"sku\": \"SKU-0500-100\",\r\n" + 
				"            \"peso\": 200.00,\r\n" + 
				"            \"altura\": 55.50,\r\n" + 
				"            \"largura\": 12.50,\r\n" + 
				"            \"profundidade\": 56.20,\r\n" + 
				"            \"fabricante\": \"HUGOS COMPANIES\",\r\n" + 
				"            \"preco\": 100.0,\r\n" + 
				"            \"quantidade\": 31,\r\n" + 
				"            \"apagado\": true\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"valorProdutos\": 100.00,\r\n" + 
				"    \"desconto\": 15.00,\r\n" + 
				"    \"valorTotal\": 85.00\r\n" + 
				"}";
		

		Pedido pedido = new Gson().fromJson(jsonPedido, Pedido.class);		
        when(pedidoRepository.findById(any(Long.class))).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);	
	
        
        MvcResult result = mockMvc.perform(delete("/store/pedido/1")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
        
        final MessageDTO messageDTO = new Gson().fromJson(result.getResponse().getContentAsString(), MessageDTO.class);
        assertEquals("Pedido apagado", messageDTO.getMessage());
        
	}

	@Test
	public void testAtualizarPedido() throws Exception {
		String jsonProduto = "    {\r\n" + "        \"id\": 1,\r\n" + "        \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n" + "        \"apagado\": false\r\n"
				+ "    }";
		

		Produto produto = new Gson().fromJson(jsonProduto, Produto.class);		
        when(produtoRepository.findById(any(Long.class))).thenReturn(Optional.of(produto));
        
		String jsonPedido = "{\r\n" + 
				"    \"id\": 1,\r\n" + 
				"    \"nomeCliente\": \"Hugo aguiar\",\r\n" + 
				"    \"telefone\": \"+55 (11) 98945-0870\",\r\n" + 
				"    \"produto\": [\r\n" + 
				"        {\r\n" + 
				"            \"id\": 1,\r\n" + 
				"            \"descricao\": \"PRODUTO 1\",\r\n" + 
				"            \"sku\": \"SKU-0500-100\",\r\n" + 
				"            \"peso\": 200.00,\r\n" + 
				"            \"altura\": 55.50,\r\n" + 
				"            \"largura\": 12.50,\r\n" + 
				"            \"profundidade\": 56.20,\r\n" + 
				"            \"fabricante\": \"HUGOS COMPANIES\",\r\n" + 
				"            \"preco\": 100.0,\r\n" + 
				"            \"quantidade\": 31,\r\n" + 
				"            \"apagado\": true\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"valorProdutos\": 100.00,\r\n" + 
				"    \"desconto\": 10.00,\r\n" + 
				"    \"valorTotal\": 90.00\r\n" + 
				"}";
		

		Pedido pedido = new Gson().fromJson(jsonPedido, Pedido.class);		
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        
        Pedido body = new Gson().fromJson(jsonPedido, Pedido.class);
        
		MvcResult result = mockMvc.perform(put("/store/pedido")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(objectMapper.writeValueAsString(body))
		        .characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andDo(print())
		        .andReturn();
			
        verify(pedidoRepository).save(any(Pedido.class));
        Pedido p = new Gson().fromJson(result.getResponse().getContentAsString(), Pedido.class);
        assertNotNull(p);
        assertNotEquals(new BigDecimal(90.0), p.getValorTotal(), "Desconto nao esta de acordo com o esperado");        
        
	}

}
