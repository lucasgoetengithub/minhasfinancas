package com.lgoeten.minhasfinancas.service;

import com.lgoeten.minhasfinancas.exceptions.RegraNegocioException;
import com.lgoeten.minhasfinancas.model.entity.Lancamento;
import com.lgoeten.minhasfinancas.model.enums.StatusLancamento;
import com.lgoeten.minhasfinancas.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class LancamentoService {

    @Autowired
    LancamentoRepository lancamentoRepository;

    @Transactional
    public Lancamento salvar(Lancamento lancamento){
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public Lancamento atualizar(Lancamento lancamento){
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        lancamentoRepository.delete(lancamento);
    }

    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        Example example = Example.of( lancamentoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) );

        return lancamentoRepository.findAll(example);
    }

    public void atualizarLancamento(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    public void validar(Lancamento lancamento) {
        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma Descrição válida.");
        }

        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe um Mês válido.");
        }

        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4 ) {
            throw new RegraNegocioException("Informe um Ano válido.");
        }

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um Usuário.");
        }

        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
            throw new RegraNegocioException("Informe um Valor válido.");
        }

        if(lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de Lançamento.");
        }
    }
}
