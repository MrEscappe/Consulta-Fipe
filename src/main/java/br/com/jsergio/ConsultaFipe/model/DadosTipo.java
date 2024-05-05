package br.com.jsergio.ConsultaFipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTipo(String Valor, String Marca, String Modelo, String AnoModelo) {
}
