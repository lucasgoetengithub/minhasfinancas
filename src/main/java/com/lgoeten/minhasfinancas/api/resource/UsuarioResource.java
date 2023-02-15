package com.lgoeten.minhasfinancas.api.resource;

import java.math.BigDecimal;

import com.lgoeten.minhasfinancas.api.dto.UsuarioDTO;
import com.lgoeten.minhasfinancas.exceptions.ErroAutenticacaoException;
import com.lgoeten.minhasfinancas.exceptions.RegraNegocioException;
import com.lgoeten.minhasfinancas.model.entity.Usuario;
import com.lgoeten.minhasfinancas.service.UsuarioService;
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
@RequiredArgsConstructor
public class UsuarioResource {


    private final UsuarioService service;

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

}
