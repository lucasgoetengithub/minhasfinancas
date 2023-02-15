package com.lgoeten.minhasfinancas.service;

import com.lgoeten.minhasfinancas.model.entity.Lancamento;
import com.lgoeten.minhasfinancas.model.enums.StatusLancamento;
import com.lgoeten.minhasfinancas.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoService {

    @Autowired
    LancamentoRepository lancamentoRepository;

    public Lancamento salvar(Lancamento lancamento){
        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Lancamento lancamento){

    }

    public void deletar(Lancamento lancamento) {

    }

    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {

    }

    public void atualizarLancamento(Lancamento lancamento, StatusLancamento status) {

    }
}
