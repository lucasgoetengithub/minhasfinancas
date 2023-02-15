package com.lgoeten.minhasfinancas.service;

import com.lgoeten.minhasfinancas.exceptions.ErroAutenticacaoException;
import com.lgoeten.minhasfinancas.exceptions.RegraNegocioException;
import com.lgoeten.minhasfinancas.model.entity.Usuario;
import com.lgoeten.minhasfinancas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (!usuario.isPresent()) {
            throw new ErroAutenticacaoException("Usuario náo encontrado");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacaoException("Senha inválida.");
        }

        return usuario.get();
    }

    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    public void validarEmail(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Email já usado.");
        }
    }


}
