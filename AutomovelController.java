package automoveis.controller;

import automoveis.model.Automovel;
import automoveis.util.ArquivoUtil;

import java.util.*;
import java.util.stream.Collectors;

public class AutomovelController {
    private final ArrayList<Automovel> lista = new ArrayList<>();
    private final String caminhoArquivo = "automoveis.txt";

    public AutomovelController() {
        carregarArquivo();
    }

    public void inserirAutomovel(Automovel auto) {
        if (consultarPorPlaca(auto.getPlaca()) != null) {
            System.out.println("Erro: Já existe um automóvel com essa placa.");
            return;
        }
        lista.add(auto);
        System.out.println("Automóvel inserido com sucesso!");
    }

    public void removerAutomovel(String placa) {
        Automovel auto = consultarPorPlaca(placa);
        if (auto != null) {
            lista.remove(auto);
            System.out.println("Automóvel removido com sucesso!");
        } else {
            System.out.println("Automóvel não encontrado.");
        }
    }

    public void alterarAutomovel(String placa, String modelo, String marca, int ano, double valor) {
        Automovel auto = consultarPorPlaca(placa);
        if (auto != null) {
            auto.setModelo(modelo);
            auto.setMarca(marca);
            auto.setAno(ano);
            auto.setValor(valor);
            System.out.println("Dados alterados com sucesso!");
        } else {
            System.out.println("Automóvel não encontrado.");
        }
    }

    public Automovel consultarPorPlaca(String placa) {
        return lista.stream().filter(a -> a.getPlaca().equalsIgnoreCase(placa)).findFirst().orElse(null);
    }

    public void listarAutomoveis(String criterio) {
        Comparator<Automovel> comparator;

        switch (criterio.toLowerCase()) {
            case "modelo":
                comparator = Comparator.comparing(Automovel::getModelo);
                break;
            case "marca":
                comparator = Comparator.comparing(Automovel::getMarca);
                break;
            default:
                comparator = Comparator.comparing(Automovel::getPlaca);
        }

        List<Automovel> ordenada = lista.stream().sorted(comparator).collect(Collectors.toList());
        if (ordenada.isEmpty()) {
            System.out.println("Nenhum automóvel cadastrado.");
        } else {
            ordenada.forEach(System.out::println);
        }
    }

    public void salvarArquivo() {
        List<String> linhas = lista.stream().map(Automovel::toCSV).collect(Collectors.toList());
        ArquivoUtil.salvarLinhas(caminhoArquivo, linhas);
    }

    public void carregarArquivo() {
        List<String> linhas = ArquivoUtil.carregarLinhas(caminhoArquivo);
        for (String linha : linhas) {
            lista.add(Automovel.fromCSV(linha));
        }
    }
}
