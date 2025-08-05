package com.senai.oficina_teste_swagger.application.service;

import com.senai.oficina_teste_swagger.domain.entity.Servico;
import com.senai.oficina_teste_swagger.domain.exception.RegraDeNegocioException;
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

    public Servico salvar(Servico servico) {
        validar(servico);
        return repository.save(servico);
    }

    public List<Servico> listar() {
        return repository.findAll();
    }

    public Servico buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Serviço não encontrado."));
    }

    public Servico atualizar(Long id, Servico servicoAtualizado) {
        Servico existente = buscarPorId(id);
        servicoAtualizado.setId(existente.getId());
        validar(servicoAtualizado);
        return repository.save(servicoAtualizado);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RegraDeNegocioException("Serviço não encontrado.");
        }
        repository.deleteById(id);
    }

    private void validar(Servico servico) {
        if (servico.getPreco() < 50)
            throw new RegraDeNegocioException("Preço mínimo do serviço deve ser R$ 50,00");

        long dias = ChronoUnit.DAYS.between(servico.getDataInicio(), servico.getDataFim());
        if (dias > 30)
            throw new RegraDeNegocioException("Duração do serviço não pode exceder 30 dias");
    }
}

