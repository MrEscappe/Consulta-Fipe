package br.com.jsergio.ConsultaFipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosVeiculo(@JsonAlias("Cód: ") String codigo,
                            @JsonAlias("Descrição: ") String nome) {
}
