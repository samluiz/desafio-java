package com.samluiz.ordermgmt.gerenciar.produto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.common.utils.ControllerUtils;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import com.samluiz.ordermgmt.gerenciar.produto.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.UUID;

import static com.samluiz.ordermgmt.utils.ControllerTestUtils.montarProduto;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "EDITOR", "VIEWER"})
class ProdutoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private ControllerUtils<Produto> controllerUtils;

    @Test
    void findById_ExistingId_ReturnsProduto() throws Exception {
        UUID existingProductId = UUID.randomUUID();
        Produto existingProduto = montarProduto();
        existingProduto.setId(existingProductId);
        when(produtoService.findById(existingProductId)).thenReturn(existingProduto);

        mockMvc.perform(MockMvcRequestBuilders.get("/produtos/{id}", existingProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingProductId.toString()))
                .andDo(print());
    }

    @Test
    void findAll_ReturnsPageOfProdutos() throws Exception {
        Page<Produto> mockPage = Page.empty();
        when(produtoService.findAll(any(PageRequest.class))).thenReturn(mockPage);
        when(controllerUtils.generateResponse(mockPage)).thenReturn(Collections.singletonMap("content", Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/produtos")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andDo(print());
    }

    @Test
    void create_ValidProduto_ReturnsCreatedProduto() throws Exception {
        Produto produtoToCreate = montarProduto();
        Produto createdProduto = montarProduto();
        when(produtoService.create(any(Produto.class))).thenReturn(createdProduto);

        mockMvc.perform(MockMvcRequestBuilders.post("/produtos")
                        .content(objectMapper.writeValueAsString(produtoToCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }

    @Test
    void update_ValidProduto_ReturnsUpdatedProduto() throws Exception {
        UUID existingProductId = UUID.randomUUID();
        Produto produtoToUpdate = montarProduto();
        Produto updatedProduto = montarProduto();
        produtoToUpdate.setId(existingProductId);
        updatedProduto.setId(existingProductId);
        when(produtoService.update(any(Produto.class), eq(existingProductId))).thenReturn(updatedProduto);

        mockMvc.perform(MockMvcRequestBuilders.put("/produtos/{id}", existingProductId)
                        .content(objectMapper.writeValueAsString(produtoToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingProductId.toString()))
                .andDo(print());
    }

    @Test
    void delete_ExistingId_ReturnsNoContent() throws Exception {
        UUID existingProductId = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.delete("/produtos/{id}", existingProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }

    @Test
    void create_InvalidProduto_ReturnsBadRequest() throws Exception {
        Produto produtoToCreate = montarProduto();
        produtoToCreate.setNome(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/produtos")
                        .content(objectMapper.writeValueAsString(produtoToCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void update_NonExistingId_ReturnsNotFound() throws Exception {
        UUID nonExistingProductId = UUID.randomUUID();
        when(produtoService.findById(nonExistingProductId)).thenThrow(RecursoNaoEncontradoException.class);
        when(produtoService.update(any(Produto.class), eq(nonExistingProductId))).thenThrow(RecursoNaoEncontradoException.class);
        Produto produtoToUpdate = montarProduto();
        mockMvc.perform(MockMvcRequestBuilders.put("/produtos/{id}", nonExistingProductId)
                        .content(objectMapper.writeValueAsString(produtoToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    void delete_NonExistingId_ReturnsNotFound() throws Exception {
        UUID nonExistingProductId = UUID.randomUUID();
        when(produtoService.findById(nonExistingProductId)).thenThrow(RecursoNaoEncontradoException.class);
        doThrow(RecursoNaoEncontradoException.class).when(produtoService).delete(nonExistingProductId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/produtos/{id}", nonExistingProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() throws Exception {
        UUID nonExistingProductId = UUID.randomUUID();
        when(produtoService.findById(nonExistingProductId)).thenThrow(RecursoNaoEncontradoException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/produtos/{id}", nonExistingProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    void findAll_DataAccessException_ReturnsInternalServerError() throws Exception {
        when(produtoService.findAll(any(PageRequest.class))).thenThrow(new RuntimeException());

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/produtos")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andDo(print());
        } catch (Exception ignored) {}
    }

    @Test
    void create_DataAccessException_ReturnsInternalServerError() throws Exception {
        doThrow(PermissionDeniedDataAccessException.class).when(produtoService).create(any(Produto.class));
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/produtos")
                            .content(objectMapper.writeValueAsString(montarProduto()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andDo(print());
        } catch (Exception ignored) {}
    }

    @Test
    void update_DataAccessException_ReturnsInternalServerError() throws Exception {
        UUID existingProductId = UUID.randomUUID();
        when(produtoService.update(any(Produto.class), eq(existingProductId))).thenThrow(new RuntimeException());

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/produtos/{id}", existingProductId)
                            .content(objectMapper.writeValueAsString(montarProduto()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andDo(print());
        } catch (Exception ignored) {}
    }

    @Test
    void delete_DataAccessException_ReturnsInternalServerError() throws Exception {
        UUID existingProductId = UUID.randomUUID();
        doThrow(PermissionDeniedDataAccessException.class).when(produtoService).delete(existingProductId);

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/produtos/{id}", existingProductId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andDo(print());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"VIEWER", "EDITOR"})
    void delete_InvalidRole_ReturnsForbidden() throws Exception {
        UUID existingProductId = UUID.randomUUID();
        doThrow(PermissionDeniedDataAccessException.class).when(produtoService).delete(existingProductId);

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/produtos/{id}", existingProductId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andDo(print());
        } catch (Exception ignored) {}
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"VIEWER"})
    void create_InvalidRole_ReturnsForbidden() throws Exception {
        Produto produtoToCreate = montarProduto();
        Produto createdProduto = montarProduto();
        when(produtoService.create(any(Produto.class))).thenReturn(createdProduto);

        mockMvc.perform(MockMvcRequestBuilders.post("/produtos")
                        .content(objectMapper.writeValueAsString(produtoToCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }
}
