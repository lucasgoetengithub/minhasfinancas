package com.lgoeten.minhasfinancas.api.resource;

import com.lgoeten.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.lgoeten.minhasfinancas.api.dto.LancamentoDTO;
import com.lgoeten.minhasfinancas.exceptions.RegraNegocioException;
import com.lgoeten.minhasfinancas.model.entity.Lancamento;
import com.lgoeten.minhasfinancas.model.entity.Usuario;
import com.lgoeten.minhasfinancas.model.enums.StatusLancamento;
import com.lgoeten.minhasfinancas.model.enums.TipoLancamento;
import com.lgoeten.minhasfinancas.service.LancamentoService;
import com.lgoeten.minhasfinancas.service.UsuarioService;
import jdk.jshell.Snippet;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO) {
        try {
            Lancamento entidade = converter(lancamentoDTO);
            entidade = lancamentoService.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO lancamentoDTO) {
           return lancamentoService.findById(id).map( entity -> {
               try {
                   Lancamento lancamento = converter(lancamentoDTO);
                   lancamento.setId(entity.getId());
                   lancamentoService.atualizar(lancamento);
                   return ResponseEntity.ok(lancamento);
               } catch (RegraNegocioException e) {
                   return ResponseEntity.badRequest().body(e.getMessage());
               }
            }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return lancamentoService.findById(id).map(entidade -> {
            lancamentoService.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet( () -> new ResponseEntity("Lancamento náo encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity find(@RequestParam(value="descricao", required = false) String descricao,
                               @RequestParam(value="mes", required = false) Integer mes,
                               @RequestParam(value="ano", required = false) Integer ano,
                               @RequestParam(value="usuario") Long usuarioId) {
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(descricao);
        lancamento.setMes(mes);
        lancamento.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(usuarioId);
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario não encontrado para o Id informado.");
        } else {
            lancamento.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentoList = lancamentoService.buscar(lancamento);
        return ResponseEntity.ok(lancamentoList);
    }

    @PutMapping("/{id}/atualiza-tatus")
    public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO atualizaStatusDTO) {

        return lancamentoService.findById(id).map(entity -> {
            StatusLancamento statusLancamento = StatusLancamento.valueOf(atualizaStatusDTO.getStatus());
            if (statusLancamento == null) {
                return ResponseEntity.badRequest().body("Não foi possivel atualizar o status do lançamento, envie um status válido.");
            }

            try {
                entity.setStatus(statusLancamento);
                lancamentoService.atualizar(entity);

                return ResponseEntity.ok(entity);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    private LancamentoDTO converter(Lancamento lancamento) {
        return LancamentoDTO.builder()
                .id(lancamento.getId())
                .descricao(lancamento.getDescricao())
                .valor(lancamento.getValor())
                .mes(lancamento.getMes())
                .ano(lancamento.getAno())
                .status(lancamento.getStatus().name())
                .tipo(lancamento.getTipo().name())
                .usuario(lancamento.getUsuario().getId())
                .build();

    }

    private Lancamento converter(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado.") );

        lancamento.setUsuario(usuario);

        if(dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }

        if(dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }


}
