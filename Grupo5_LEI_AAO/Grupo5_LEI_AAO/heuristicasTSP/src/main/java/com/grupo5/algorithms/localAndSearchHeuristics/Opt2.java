package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementação da heurística 2-Opt para o Problema do Caixeiro Viajante (TSP).
 *
 * A 2-Opt é uma técnica clássica de otimização local que tenta melhorar um tour
 * removendo dois segmentos e reconectando-os de forma a eliminar cruzamentos e
 * reduzir o custo total.
 */
public class Opt2 {

    /**
     * Realiza uma troca 2-Opt: inverte o segmento de cidades entre os índices i e k.
     *
     * @param tour Tour atual (lista de cidades).
     * @param i Índice inicial da inversão.
     * @param k Índice final da inversão.
     * @return Novo tour com o segmento [i..k] invertido.
     */
    public static List<Utils.City> twoOptSwap(List<Utils.City> tour, int i, int k) {
        List<Utils.City> newTour = new ArrayList<>();

        // 1. Copia o início do tour até à posição i-1 (sem alterações)
        for (int c = 0; c < i; c++) {
            newTour.add(tour.get(c));
        }

        // 2. Inverte o segmento de i até k
        for (int c = k; c >= i; c--) {
            newTour.add(tour.get(c));
        }

        // 3. Copia o resto do tour (depois de k)
        for (int c = k + 1; c < tour.size(); c++) {
            newTour.add(tour.get(c));
        }

        return newTour;
    }

    /**
     * Aplica a heurística 2-Opt iterativamente até não haver mais melhorias.
     *
     * @param tour Tour inicial (ciclo fechado).
     * @return Tour melhorado com base em otimizações locais.
     */
    public static List<Utils.City> twoOpt(List<Utils.City> tour) {
        int size = tour.size();
        boolean improvement = true;
        List<Utils.City> bestTour = new ArrayList<>(tour);
        double bestDistance = calculatePathCost(bestTour);

        // Repetir enquanto existirem melhorias
        while (improvement) {
            improvement = false;

            // Testar todas as trocas possíveis entre pares de segmentos
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
     * Executa o algoritmo 2-Opt com dados lidos de um ficheiro .tsp.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê o ficheiro .tsp
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Tour inicial: ordem original + cidade inicial no fim (ciclo fechado)
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0));

        System.out.println("Distância do tour inicial: " + calculatePathCost(initialTour));

        // Aplica a heurística 2-Opt
        List<Utils.City> improvedTour = twoOpt(initialTour);
        System.out.println("Distância do tour melhorado: " + calculatePathCost(improvedTour));

        // Mostra o tour melhorado
        System.out.println("Tour melhorado (2-Opt):");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();

        double cost = calculatePathCost(improvedTour);
        System.out.println("Custo total do tour: " + cost);
    }
}
