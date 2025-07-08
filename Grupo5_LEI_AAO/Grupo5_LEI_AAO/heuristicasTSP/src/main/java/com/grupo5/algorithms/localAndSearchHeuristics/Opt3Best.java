package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa a heurística 3-Opt com a estratégia **Best Improvement**.
 * Em cada iteração, avalia todas as trocas possíveis e aplica **a que melhora mais** o tour.
 */
public class Opt3Best {

    /**
     * Executa a heurística 3-Opt usando a estratégia de Best Improvement.
     *
     * @param tour Tour inicial (deve estar fechado).
     * @return Tour melhorado.
     */
    public static List<Utils.City> opt3BestImprovement(List<Utils.City> tour) {
        boolean improvement = true;

        while (improvement) {
            improvement = false;
            double bestDelta = 0;
            List<Utils.City> bestTour = null;

            // Percorre trios de índices para testar trocas
            for (int i = 1; i < tour.size() - 5; i++) {
                for (int j = i + 2; j < tour.size() - 3; j++) {
                    for (int k = j + 2; k < tour.size() - 1; k++) {
                        List<Utils.City> newTour = threeOptSwap(tour, i, j, k);
                        double delta = calculatePathCost(tour) - calculatePathCost(newTour);

                        // Guarda a melhor melhoria encontrada
                        if (delta > bestDelta) {
                            bestDelta = delta;
                            bestTour = newTour;
                        }
                    }
                }
            }

            // Aplica a melhor troca, se houver
            if (bestTour != null) {
                tour = bestTour;
                improvement = true;
            }
        }

        return tour;
    }

    /**
     * Troca 3-Opt: inverte os segmentos [i, j) e [j, k).
     *
     * @param tour Tour atual.
     * @param i Início do 1.º segmento.
     * @param j Início do 2.º segmento.
     * @param k Fim do 2.º segmento.
     * @return Novo tour com os dois segmentos invertidos.
     */
    private static List<Utils.City> threeOptSwap(List<Utils.City> tour, int i, int j, int k) {
        List<Utils.City> newTour = new ArrayList<>();

        newTour.addAll(tour.subList(0, i));

        List<Utils.City> segment1 = new ArrayList<>(tour.subList(i, j));
        Collections.reverse(segment1);
        newTour.addAll(segment1);

        List<Utils.City> segment2 = new ArrayList<>(tour.subList(j, k));
        Collections.reverse(segment2);
        newTour.addAll(segment2);

        newTour.addAll(tour.subList(k, tour.size()));
        return newTour;
    }

    /**
     * Executa a versão Best Improvement com base num ficheiro TSP.
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0)); // fecha o ciclo

        System.out.println("Distância do tour inicial: " + calculatePathCost(initialTour));

        List<Utils.City> improvedTour = opt3BestImprovement(initialTour);
        System.out.println("Distância do tour após Best 3-Opt: " + calculatePathCost(improvedTour));

        System.out.println("Tour:");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
    }
}
