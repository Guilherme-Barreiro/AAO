package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementação da heurística 2-Opt para melhoria de tours no problema do caixeiro viajante (TSP).
 */
public class Opt2 {

    /**
     * Realiza a troca 2-Opt: inverte a ordem dos elementos entre as posições i e k (inclusive).
     *
     * @param tour Tour atual.
     * @param i Índice inicial da inversão.
     * @param k Índice final da inversão.
     * @return Novo tour com o segmento invertido.
     */
    public static List<Utils.City> twoOptSwap(List<Utils.City> tour, int i, int k) {
        List<Utils.City> newTour = new ArrayList<>();

        /* 1. Copia o segmento do início até i-1 */
        for (int c = 0; c < i; c++) {
            newTour.add(tour.get(c));
        }

        /* 2. Inverte a ordem do segmento entre i e k */
        for (int c = k; c >= i; c--) {
            newTour.add(tour.get(c));
        }

        /* 3. Copia o restante do tour (de k+1 até o fim) */
        for (int c = k + 1; c < tour.size(); c++) {
            newTour.add(tour.get(c));
        }

        return newTour;
    }

    /**
     * Aplica a heurística 2-Opt, tentando melhorar o tour até que não haja mais melhorias.
     *
     * @param tour Tour inicial.
     * @return Tour otimizado.
     */
    public static List<Utils.City> twoOpt(List<Utils.City> tour) {
        int size = tour.size();
        boolean improvement = true;
        List<Utils.City> bestTour = new ArrayList<>(tour);
        double bestDistance = calculatePathCost(bestTour);

        while (improvement) {
            improvement = false;

            /* Tenta trocar cada par de segmentos possíveis */
            for (int i = 1; i < size - 2; i++) {
                for (int k = i + 1; k < size - 1; k++) {
                    List<Utils.City> newTour = twoOptSwap(bestTour, i, k);
                    double newDistance = calculatePathCost(newTour);
                    if (newDistance < bestDistance) {
                        bestTour = newTour;
                        bestDistance = newDistance;
                        improvement = true;
                    }
                }
            }
        }

        return bestTour;
    }

    /**
     * Executa o 2-Opt a partir de um ficheiro TSP e imprime o resultado.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        /* Lê as cidades a partir do ficheiro .tsp (localizado em src/main/resources) */
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        /* Define um tour inicial simples: a lista de cidades na ordem de leitura com ciclo fechado */
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0)); // fecha o ciclo

        System.out.println("Distância do tour inicial: " + calculatePathCost(initialTour));

        /* Aplica o 2-Opt para melhorar o tour */
        List<Utils.City> improvedTour = twoOpt(initialTour);
        System.out.println("Distância do tour melhorado: " + calculatePathCost(improvedTour));

        /* Imprime o tour melhorado */
        System.out.println("Tour melhorado (2-Opt):");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(improvedTour);
        System.out.println("Custo total do tour: " + cost);
    }
}
