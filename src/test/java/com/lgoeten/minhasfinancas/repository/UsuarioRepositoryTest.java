package com.lgoeten.minhasfinancas.repository;

import com.lgoeten.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void existsEmailTest() {
        Usuario usuario =  Usuario.builder().nome("usuario").email("usuario@email.com").build();
        usuarioRepository.save(usuario);

        boolean result = usuarioRepository.existsByEmail("usuario@email.com");

        Assertions.assertThat(result).isTrue();
    }

}
