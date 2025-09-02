package com.senai.oficina_teste_swagger;

import com.senai.oficina_teste_swagger.application.dto.ServicoDTO;
import com.senai.oficina_teste_swagger.application.service.ServicoAppService;
import com.senai.oficina_teste_swagger.domain.entity.Servico;
import com.senai.oficina_teste_swagger.domain.exception.EntidadeNaoEncontradaException;
import com.senai.oficina_teste_swagger.domain.exception.ValidacaoException;
import com.senai.oficina_teste_swagger.domain.repository.ServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OficinaTesteSwaggerApplicationTests {

    @Mock
    private ServicoRepository repository;

    @InjectMocks
    private ServicoAppService service;

    @Test
    void deveSalvarServicoValido() {
        ServicoDTO dto = new ServicoDTO(null, "Revisão", 120.0,
                LocalDate.now(), LocalDate.now().plusDays(10));

        Servico entidade = dto.toEntity();

        when(repository.save(any())).thenReturn(entidade);

        ServicoDTO salvo = service.salvar(dto);

        assertNotNull(salvo);
        assertEquals("Revisão", salvo.descricao());
        verify(repository).save(any());
    }

    @Test
    void deveLancarValidacaoExceptionSePrecoForMenorQue50() {
        ServicoDTO dto = new ServicoDTO(null, "Revisão", 30.0,
                LocalDate.now(), LocalDate.now().plusDays(10));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> service.salvar(dto));
        assertEquals("Preço mínimo do serviço deve ser R$ 50,00", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarValidacaoExceptionSeDuracaoMaiorQue30Dias() {
        ServicoDTO dto = new ServicoDTO(null, "Revisão", 100.0,
                LocalDate.now(), LocalDate.now().plusDays(31));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> service.salvar(dto));
        assertEquals("Duração do serviço não pode exceder 30 dias", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveBuscarServicoPorId() {
        Servico servico = new Servico(1L, "Alinhamento", 90.0,
                LocalDate.now(), LocalDate.now().plusDays(2));

        when(repository.findById(1L)).thenReturn(Optional.of(servico));
        ServicoDTO resultado = service.buscarPorId(1L);
        assertEquals("Alinhamento", resultado.descricao());
        verify(repository).findById(1L);
    }

    @Test
    void deveLancarEntidadeNaoEncontradaExceptionAoBuscarIdInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.buscarPorId(99L));
        assertEquals("Serviço com ID 99 não encontrado.", ex.getMessage());
    }

    @Test
    void deveAtualizarServicoComSucesso() {
        Servico existente = new Servico(1L, "Troca de óleo", 100.0,
                LocalDate.now(), LocalDate.now().plusDays(2));

        ServicoDTO dtoAtualizado = new ServicoDTO(null, "Troca de filtro", 150.0,
                LocalDate.now(), LocalDate.now().plusDays(3));

        Servico salvo = dtoAtualizado.toEntity();
        salvo.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(salvo);

        ServicoDTO resultado = service.atualizar(1L, dtoAtualizado);

        assertEquals("Troca de filtro", resultado.descricao());
        assertEquals(150.0, resultado.preco());
        verify(repository).save(any());
    }

    @Test
    void deveDeletarServicoExistente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deletar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deveLancarEntidadeNaoEncontradaExceptionAoDeletarServicoInexistente() {
        when(repository.existsById(99L)).thenReturn(false);

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.deletar(99L));
        assertEquals("Serviço com ID 99 não encontrado.", ex.getMessage());
    }
}
