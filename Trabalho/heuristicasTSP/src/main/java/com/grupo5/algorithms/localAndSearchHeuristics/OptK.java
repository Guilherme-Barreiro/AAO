package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementação genérica da heurística k-Opt para o problema do caixeiro viajante.
 */
public class OptK {

    /**
     * Aplica a heurística k‑Opt ao tour fornecido.
     *
     * @param tour o tour inicial (lista de cidades com ciclo fechado).
     * @param k    o número de arestas a serem trocadas (k-opt).
     * @return o tour melhorado, se houver.
     */
    public static List<Utils.City> optK(List<Utils.City> tour, int k) {
        if (tour.size() < k + 1) return tour; // não há arestas suficientes

        boolean improvement = true;
        double bestDistance = calculatePathCost(tour);

        while (improvement) {
            improvement = false;

            /* Se k for 2 ou 3, podemos delegar para métodos especializados */
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
            } else if (k == 3) {
                /* Para k = 3, reutiliza a implementação de Opt3 */
                tour = Opt3.opt3(tour);
                bestDistance = calculatePathCost(tour);
                improvement = false; // opt3 já é iterativo
            } else {
                /* Para k >= 4, usa uma abordagem simplificada: escolhe cortes e inverte segmentos */
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
     * Escolhe k índices uniformemente distribuídos no tour (excluindo o primeiro e o último).
     *
     * @param tourSize Tamanho total do tour.
     * @param k        Número de cortes.
     * @return Lista de índices de corte.
     */
    private static List<Integer> chooseKIndices(int tourSize, int k) {
        List<Integer> indices = new ArrayList<>();
        int gap = (tourSize - 2) / k; // -2 para não considerar o primeiro e o último (ciclo fechado)
        for (int i = 1; i < tourSize - 1 && indices.size() < k; i += gap) {
            indices.add(i);
        }
        return indices;
    }

    /**
     * Realiza uma reconexão simples invertendo os segmentos entre os pontos de corte.
     *
     * @param tour        Tour atual.
     * @param cutIndices  Índices dos cortes no tour.
     * @return Novo tour após inversões.
     */
    private static List<Utils.City> performKOptSwap(List<Utils.City> tour, List<Integer> cutIndices) {
        List<Utils.City> newTour = new ArrayList<>();
        int start = 0;

        /* Para cada índice de corte, inverte o segmento e adiciona ao novo tour */
        for (int cut : cutIndices) {
            List<Utils.City> segment = new ArrayList<>(tour.subList(start, cut));
            Collections.reverse(segment);
            newTour.addAll(segment);
            start = cut;
        }

        /* Adiciona e inverte o segmento restante */
        List<Utils.City> lastSegment = new ArrayList<>(tour.subList(start, tour.size()));
        Collections.reverse(lastSegment);
        newTour.addAll(lastSegment);

        return newTour;
    }

    /**
     * Implementação auxiliar do 2-opt (utilizado quando k = 2).
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
     * Executa a heurística k-Opt com um valor k fixo e imprime o resultado.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        /* Cria um tour inicial simples (ex: ordem original + ciclo fechado) */
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0)); // fechar o ciclo

        System.out.println("Comprimento do tour inicial: " + calculatePathCost(initialTour));

        /* Define o valor de k para k‑opt (ex: 4) */
        int k = 4;
        List<Utils.City> improvedTour = optK(initialTour, k);

        System.out.println("Comprimento do tour após " + k + "-opt: " + calculatePathCost(improvedTour));
        System.out.println("Tour:");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
    }
}
