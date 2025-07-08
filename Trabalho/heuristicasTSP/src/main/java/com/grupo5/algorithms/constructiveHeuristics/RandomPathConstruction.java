package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa a heurística de construção de caminho aleatório para o problema do caixeiro viajante (TSP).
 */
public class RandomPathConstruction {

    /**
     * Gera um tour aleatório embaralhando a lista de cidades.
     *
     * @param cities Lista de cidades a serem visitadas.
     * @return Tour aleatório, começando e terminando na mesma cidade.
     */
    public static List<Utils.City> randomPathTour(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        /* Cria uma nova lista para não alterar a original */
        List<Utils.City> tour = new ArrayList<>(cities);

        /* Embaralha as cidades de forma aleatória */
        Collections.shuffle(tour, new Random());

        /* Fecha o ciclo: adiciona a cidade inicial no fim do tour */
        tour.add(tour.get(0));

        return tour;
    }

    /**
     * Executa a heurística com base no ficheiro TSP de entrada.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        List<Utils.City> tour = randomPathTour(cities);
        System.out.println("Random Path Construction Tour:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
