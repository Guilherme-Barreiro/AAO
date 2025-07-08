package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa uma heurística genérica k-Opt para o Problema do Caixeiro Viajante (TSP).
 *
 * Dependendo do valor de k:
 * - Para k = 2: aplica 2-opt com estratégia first improvement.
 * - Para k = 3: delega para a implementação já existente de 3-opt.
 * - Para k >= 4: aplica uma abordagem simplificada baseada na inversão de múltiplos segmentos.
 */
public class OptK {

    /**
     * Aplica a heurística k-Opt ao tour fornecido.
     *
     * @param tour Tour inicial (com ciclo fechado).
     * @param k    Número de arestas a trocar (grau da otimização).
     * @return Tour melhorado.
     */
    public static List<Utils.City> optK(List<Utils.City> tour, int k) {
        if (tour.size() < k + 1) return tour; // não há arestas suficientes

        boolean improvement = true;
        double bestDistance = calculatePathCost(tour);

        while (improvement) {
            improvement = false;

            // Para k = 2, aplica 2-Opt (estratégia de first improvement)
            if (k == 2) {
                for (int i = 1; i < tour.size() - 2; i++) {
                    for (int j = i + 1; j < tour.size() - 1; j++) {
                        List<Utils.City> newTour = twoOptSwap(tour, i, j);
                        double newDistance = calculatePathCost(newTour);
                        if (newDistance < bestDistance) {
                            tour = newTour;
                            bestDistance = newDistance;
                            improvement = true;
                        }
                    }
                }
            }
            // Para k = 3, usa a implementação da heurística 3-Opt (que já é iterativa)
            else if (k == 3) {
                tour = Opt3.opt3(tour);
                bestDistance = calculatePathCost(tour);
                improvement = false; // Opt3 já faz iteração interna
            }
            // Para k >= 4, usa abordagem simplificada baseada em cortes e inversões
            else {
                List<Integer> cutIndices = chooseKIndices(tour.size(), k);
                List<Utils.City> newTour = performKOptSwap(tour, cutIndices);
                double newDistance = calculatePathCost(newTour);
                if (newDistance < bestDistance) {
                    tour = newTour;
                    bestDistance = newDistance;
                    improvement = true;
                }
            }
        }

        return tour;
    }

    /**
     * Gera k índices uniformemente espaçados no tour (evita extremos).
     *
     * @param tourSize Tamanho do tour.
     * @param k        Número de cortes desejados.
     * @return Lista com os índices dos cortes.
     */
    private static List<Integer> chooseKIndices(int tourSize, int k) {
        List<Integer> indices = new ArrayList<>();
        int gap = (tourSize - 2) / k; // -2 para evitar incluir a cidade inicial/final
        for (int i = 1; i < tourSize - 1 && indices.size() < k; i += gap) {
            indices.add(i);
        }
        return indices;
    }

    /**
     * Inverte os segmentos entre os cortes fornecidos, simulando uma reconexão k-opt simplificada.
     *
     * @param tour        Tour original.
     * @param cutIndices  Índices onde serão feitos os cortes.
     * @return Novo tour com segmentos invertidos.
     */
    private static List<Utils.City> performKOptSwap(List<Utils.City> tour, List<Integer> cutIndices) {
        List<Utils.City> newTour = new ArrayList<>();
        int start = 0;

        // Para cada segmento entre os cortes, inverte e adiciona
        for (int cut : cutIndices) {
            List<Utils.City> segment = new ArrayList<>(tour.subList(start, cut));
            Collections.reverse(segment);
            newTour.addAll(segment);
            start = cut;
        }

        // Inverte o segmento final
        List<Utils.City> lastSegment = new ArrayList<>(tour.subList(start, tour.size()));
        Collections.reverse(lastSegment);
        newTour.addAll(lastSegment);

        return newTour;
    }

    /**
     * Implementação auxiliar da troca 2-Opt.
     *
     * @param tour Tour original.
     * @param i    Índice inicial do segmento a inverter.
     * @param j    Índice final.
     * @return Novo tour com segmento [i, j] invertido.
     */
    private static List<Utils.City> twoOptSwap(List<Utils.City> tour, int i, int j) {
        List<Utils.City> newTour = new ArrayList<>();
        newTour.addAll(tour.subList(0, i));

        List<Utils.City> reversedSegment = new ArrayList<>(tour.subList(i, j + 1));
        Collections.reverse(reversedSegment);
        newTour.addAll(reversedSegment);

        newTour.addAll(tour.subList(j + 1, tour.size()));
        return newTour;
    }

    /**
     * Executa a heurística k-Opt com um valor de k definido, imprime o custo e o tour final.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Tour inicial com ciclo fechado
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0));

        System.out.println("Comprimento do tour inicial: " + calculatePathCost(initialTour));

        // Valor de k para a heurística (podes alterar para 2, 3, 4, etc.)
        int k = 4;
        List<Utils.City> improvedTour = optK(initialTour, k);

        System.out.println("Comprimento do tour após " + k + "-Opt: " + calculatePathCost(improvedTour));
        System.out.println("Tour:");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
    }
}
