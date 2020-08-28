package br.com.quarktech.apirest.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import br.com.quarktech.apirest.models.Produto;
import br.com.quarktech.apirest.repository.ProdutoRepository;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProdutoResource.class)
public class ProdutoResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProdutoRepository produtoRepository;

	@Test
	public void testSalvarProduto() throws Exception {
		
		String json = "    {\r\n" + " \"id\": 1,\r\n" + " \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n" + "        \"apagado\": false\r\n"
				+ "    }";
		
		Produto produto = new Gson().fromJson(json, Produto.class);		
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);	
        
		Produto body = new Gson().fromJson(json, Produto.class);
		
		MvcResult result = mockMvc.perform(post("/store/produto")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body))
        .characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andDo(print())
        .andReturn();
		
        verify(produtoRepository).save(any(Produto.class));
        Produto p = new Gson().fromJson(result.getResponse().getContentAsString(), Produto.class);
        assertNotNull(p);
	}

	@Test
	public void testListarProdutos() throws Exception {
		
		String json = "    [{\r\n" + "        \"id\": 1,\r\n" + "        \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n" + "        \"apagado\": false\r\n"
				+ "        }, {\r\n" + "        \"id\": 2,\r\n" + "        \"descricao\": \"PRODUTO 2\",\r\n"
				+ "        \"sku\": \"SKU-0500-200\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n"
				+ "        \"apagado\": false\r\n }]";
		
		Type type = new TypeToken<List<Produto>>(){}.getType();
		List<Produto> produto = new Gson().fromJson(json, type);		
        when(produtoRepository.findAll()).thenReturn(produto);	
		
		MvcResult result = mockMvc.perform(get("/store/produtos")
		.characterEncoding("utf-8"))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();

		verify(produtoRepository).findAll();
        List<Produto> produtos = new Gson().fromJson(result.getResponse().getContentAsString(), type);
        assertNotNull(produtos);
	}

	@Test
	public void testListarProdutoUnico() throws Exception {
		
		String json = "    {\r\n" + "        \"id\": 1,\r\n" + "        \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n" + "        \"apagado\": false\r\n"
				+ "    }";
		

		Produto produto = new Gson().fromJson(json, Produto.class);		
        when(produtoRepository.findById(any(Long.class))).thenReturn(Optional.of(produto));
		
		MvcResult result = mockMvc.perform(get("/store/produto/1")
		.characterEncoding("utf-8"))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();
		
		verify(produtoRepository).findById(any(Long.class));
        Produto produtos = new Gson().fromJson(result.getResponse().getContentAsString(), Produto.class);
        assertNotNull(produtos);
        assertEquals(1L, produtos.getId());
	}

	@Test
	public void testDeletarProduto()  throws Exception {
		
		String json = "    {\r\n" + "        \"id\": 1,\r\n" + "        \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n" + "        \"apagado\": false\r\n"
				+ "    }";
		

		Produto findProduto = new Gson().fromJson(json, Produto.class);		
        when(produtoRepository.findById(any(Long.class))).thenReturn(Optional.of(findProduto));
		
		String jsonSave = "    {\r\n" + "        \"id\": 1,\r\n" + "        \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 3,\r\n" + "        \"apagado\": true\r\n"
				+ "    }";
		
		Produto produto = new Gson().fromJson(jsonSave, Produto.class);	
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);	
        
        MvcResult result = mockMvc.perform(delete("/store/produto/1")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
        
        final MessageDTO messageDTO = new Gson().fromJson(result.getResponse().getContentAsString(), MessageDTO.class);
        assertEquals("Produto removido", messageDTO.getMessage());
        
	}

	@Test
	public void testAtualizarProduto()  throws Exception {
		String json = "    {\r\n" + "        \"id\": 1,\r\n" + "        \"descricao\": \"PRODUTO 1\",\r\n"
				+ "        \"sku\": \"SKU-0500-100\",\r\n" + "        \"peso\": 200.00,\r\n"
				+ "        \"altura\": 55.50,\r\n" + "        \"largura\": 12.50,\r\n"
				+ "        \"profundidade\": 56.20,\r\n" + "        \"fabricante\": \"HUGOS COMPANIES\",\r\n"
				+ "        \"preco\": 100.00,\r\n" + "        \"quantidade\": 5,\r\n" + "        \"apagado\": false\r\n"
				+ "    }";
		
		Produto produto = new Gson().fromJson(json, Produto.class);		
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);	
		
		Produto body = new Gson().fromJson(json, Produto.class);
		
		MvcResult result = mockMvc.perform(put("/store/produto")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body))
        .characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andDo(print())
        .andReturn();
		
        verify(produtoRepository).save(any(Produto.class));
        Produto p = new Gson().fromJson(result.getResponse().getContentAsString(), Produto.class);
        assertEquals(body.getQuantidade(), p.getQuantidade());	
	}

}
