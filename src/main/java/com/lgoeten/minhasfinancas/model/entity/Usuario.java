package com.lgoeten.minhasfinancas.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usuario", schema = "financas")
@Data
@Builder
public class Usuario {

    @Id
    @Column( name="id" )
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column(name="nome")
    private String nome;

    @Column(name="email")
    private String email;

    @Column(name="senha")
    private String senha;


}