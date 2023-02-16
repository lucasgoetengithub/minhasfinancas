package com.lgoeten.minhasfinancas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import com.lgoeten.minhasfinancas.api.dto.UsuarioDTO;
import com.lgoeten.minhasfinancas.exceptions.ErroAutenticacaoException;
import com.lgoeten.minhasfinancas.exceptions.RegraNegocioException;
import com.lgoeten.minhasfinancas.model.entity.Usuario;
import com.lgoeten.minhasfinancas.service.LancamentoService;
import com.lgoeten.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {


    @Autowired
    private UsuarioService service;

    @Autowired
    private LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity salvar( @RequestBody UsuarioDTO dto ) {

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha()).build();

        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/autenticar")
    public ResponseEntity authenticar( @RequestBody UsuarioDTO dto ) {
        try {
            Usuario usuario = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuario);
        } catch (ErroAutenticacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity obterSaldo( @PathVariable("id") Long id ) {
        Optional<Usuario> usuario = service.obterPorId(id);

        if(!usuario.isPresent()) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }

}
