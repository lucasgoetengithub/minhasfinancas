package com.lgoeten.minhasfinancas.service;

import com.lgoeten.minhasfinancas.exceotions.RegraNegocioException;
import com.lgoeten.minhasfinancas.model.entity.Usuario;
import com.lgoeten.minhasfinancas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario autenticar(String email, String senha) {

        return null;
    }

    public Usuario salvarUsuario(Usuario usuario) {

        return null;
    }

    public void validarEmail(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Email já usado.");
        }
    }


}
