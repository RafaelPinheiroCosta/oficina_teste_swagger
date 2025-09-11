package com.senai.oficina_teste_swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.senai.oficina_teste_swagger.application.dto.ServicoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServicoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void deveCadastrarServicoValido() throws Exception{
        var dto =new ServicoDTO(
                null,
                "Troca de pastilha",
                150.0,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );
        System.out.println(objectMapper.writeValueAsString(dto));

        mockMvc.perform(
                post("/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Troca de pastilha"));

    }

    @Test
    void deveRetornarErroSePrecoForInvalido() throws Exception{
        var dto =new ServicoDTO(
                null,
                "Troca de pastilha",
                40.0,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        mockMvc.perform(
                        post("/servicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content( objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Preço mínimo do serviço deve ser R$ 50,00"));
    }

    @Test
    void deveAtualizarServico() throws  Exception{
        var dto =new ServicoDTO(
                null,
                "Troca de pastilha",
                150.0,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        var salvo = mockMvc.perform(post("/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        var servicoSalvo = objectMapper.readValue(salvo, ServicoDTO.class);

        var atualizado = new ServicoDTO(
                null,
                "Troca de pastilha de Freio",
                150.0,
                servicoSalvo.dataInicio(),
                servicoSalvo.dataFim()
        );

        mockMvc.perform(
                put("/servicos/"+servicoSalvo.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Troca de pastilha de Freio"));

    }


    @Test
    void deveDeletarServico() throws Exception {
        var dto = new ServicoDTO(null, "Limpeza de bico", 80.0,
                LocalDate.now(), LocalDate.now().plusDays(1));
        var salvo = mockMvc.perform(post("/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        var servico = objectMapper.readValue(salvo, ServicoDTO.class);

        mockMvc.perform(delete("/servicos/" + servico.id()))
                .andExpect(status().isNoContent());
    }

}
