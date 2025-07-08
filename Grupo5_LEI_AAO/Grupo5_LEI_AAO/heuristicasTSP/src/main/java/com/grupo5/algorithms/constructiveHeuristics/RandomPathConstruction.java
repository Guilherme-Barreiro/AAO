package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa a heurística de construção de caminho aleatório (Random Path Construction)
 * para o Problema do Caixeiro Viajante (TSP).
 *
 * Esta abordagem consiste simplesmente em gerar um tour aleatório,
 * embaralhando a lista de cidades e fechando o ciclo ao regressar à cidade inicial.
 *
 * Apesar de ser uma solução muito básica e geralmente má, é útil como baseline
 * para comparar com heurísticas mais elaboradas.
 */
public class RandomPathConstruction {

    /**
     * Gera um tour aleatório sobre a lista de cidades.
     *
     * @param cities Lista de cidades a visitar.
     * @return Lista com as cidades organizadas num tour aleatório (ciclo fechado).
     */
    public static List<Utils.City> randomPathTour(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        // Cria uma cópia da lista original para não a modificar
        List<Utils.City> tour = new ArrayList<>(cities);

        // Embaralha aleatoriamente a ordem das cidades
        Collections.shuffle(tour, new Random());

        // Fecha o ciclo adicionando a cidade inicial no final
        tour.add(tour.get(0));

        return tour;
    }

    /**
     * Executa o algoritmo com base num ficheiro .tsp, imprime o tour e o seu custo.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê a lista de cidades do ficheiro
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Gera e imprime um tour aleatório
        List<Utils.City> tour = randomPathTour(cities);
        System.out.println("Random Path Construction Tour:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();

        // Calcula e imprime o custo do tour
        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
