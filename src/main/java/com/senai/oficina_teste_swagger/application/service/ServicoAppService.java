package com.senai.oficina_teste_swagger.application.service;

import com.senai.oficina_teste_swagger.application.dto.ServicoDTO;
import com.senai.oficina_teste_swagger.domain.entity.Servico;
import com.senai.oficina_teste_swagger.domain.exception.EntidadeNaoEncontradaException;
import com.senai.oficina_teste_swagger.domain.exception.ValidacaoException;
import com.senai.oficina_teste_swagger.domain.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ServicoAppService {

    private final ServicoRepository repository;

    public ServicoAppService(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoDTO salvar(ServicoDTO dto) {
        Servico servico = dto.toEntity();
        servico.validar();
        return ServicoDTO.fromEntity(repository.save(servico));
    }

    public List<ServicoDTO> listar() {
        return repository.findAll()
                .stream()
                .map(ServicoDTO::fromEntity)
                .toList();
    }

    public ServicoDTO buscarPorId(Long id) {
        return ServicoDTO.fromEntity(
                repository.findById(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço com ID " + id + " não encontrado."))
        );
    }

    public ServicoDTO atualizar(Long id, ServicoDTO dtoAtualizado) {
        Servico existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço com ID " + id + " não encontrado."));

        Servico atualizado = dtoAtualizado.toEntity();
        atualizado.setId(existente.getId());

        atualizado.validar();
        return ServicoDTO.fromEntity(repository.save(atualizado));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Serviço não encontrado.");
        }
        repository.deleteById(id);
    }

}
