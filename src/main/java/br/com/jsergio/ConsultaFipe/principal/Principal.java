package br.com.jsergio.ConsultaFipe.principal;

import br.com.jsergio.ConsultaFipe.model.DadosModelos;
import br.com.jsergio.ConsultaFipe.model.DadosTipo;
import br.com.jsergio.ConsultaFipe.model.DadosVeiculo;
import br.com.jsergio.ConsultaFipe.service.Api;
import br.com.jsergio.ConsultaFipe.service.ConverteDados;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL = "https://parallelum.com.br/fipe/api/v1/";
    private Api api = new Api();
    private ConverteDados converteDados = new ConverteDados();

    public void exibeMenus() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("OPÇÕES DE CONSULTA");
            System.out.println("Carro");
            System.out.println("Moto");
            System.out.println("Caminhão");
            System.out.println("Digite o tipo de veículo: ");

            String tipoVeiculo = getTipoVeiculo(scanner);

            if (tipoVeiculo == null) {
                System.out.println("Tipo de veículo inválido");
                return;
            }

            exibeMarcas(tipoVeiculo, scanner);
        }
    }

    private String getTipoVeiculo(Scanner scanner) {
        String tipoVeiculo = scanner.nextLine().toLowerCase();

        if (tipoVeiculo.equalsIgnoreCase("carro")) {
            return "carros";
        } else if (tipoVeiculo.equalsIgnoreCase("moto")) {
            return "motos";
        } else if (tipoVeiculo.equalsIgnoreCase("caminhao")) {
            return "caminhoes";
        } else {
            return null;
        }
    }

    private void exibeMarcas(String tipoVeiculo, Scanner scanner) {
        String json = api.obterDados(URL + tipoVeiculo + "/marcas/");
        List<DadosVeiculo> marcas = converteDados.obterLista(json, DadosVeiculo.class);

        marcas.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(marca -> System.out.println("Cód: " + marca.codigo() + " - " + "Descrição: " + marca.nome()));

        System.out.println("Digite o código da marca: ");
        String codigoMarca = scanner.nextLine();

        exibeModelos(tipoVeiculo, codigoMarca, scanner);
    }

    private void exibeModelos(String tipoVeiculo, String codigoMarca, Scanner scanner) {
        String json = api.obterDados(URL + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/");
        DadosModelos modelos = converteDados.obterDados(json, DadosModelos.class);

        System.out.println("Modelos: ");
        modelos.modelos().stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(modelo -> System.out.println("Cód: " + modelo.codigo() + " - " + "Descrição: " + modelo.nome()));

        System.out.println("Digite um trecho do modelo: ");
        String trechoModelo = scanner.nextLine();
        modelos.modelos().stream()
                .filter(modelo -> modelo.nome().toLowerCase().contains(trechoModelo.toLowerCase()))
                .forEach(modelo -> System.out.println("Cód: " + modelo.codigo() + " - " + "Descrição: " + modelo.nome()));

        System.out.println("Digite o código do modelo: ");
        String codigoModelo = scanner.nextLine();

        System.out.println("Você escolheu o modelo: " + modelos.modelos().stream()
                .filter(modelo -> modelo.codigo().equals(codigoModelo))
                .map(DadosVeiculo::nome)
                .collect(Collectors.joining()));

        exibeAnos(tipoVeiculo, codigoMarca, codigoModelo);
    }

    private void exibeAnos(String tipoVeiculo, String codigoMarca, String codigoModelo) {
        String json = api.obterDados(URL + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/");
        List<DadosVeiculo> codigosAno = converteDados.obterLista(json, DadosVeiculo.class);

        codigosAno.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(ano -> {
                    String jsonAno = api.obterDados(URL + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + ano.codigo());
                    DadosTipo veiculo = converteDados.obterDados(jsonAno, DadosTipo.class);
                    System.out.println("Ano: " + veiculo.AnoModelo() + " - " + "Valor: " + veiculo.Valor());
                });
    }
}