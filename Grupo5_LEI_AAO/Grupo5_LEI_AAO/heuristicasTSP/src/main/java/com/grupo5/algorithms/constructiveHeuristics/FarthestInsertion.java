package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementação da heurística construtiva "Farthest Insertion" (Inserção Mais Distante)
 * para o Problema do Caixeiro Viajante (TSP).
 *
 * Esta abordagem constrói iterativamente um tour, começando com duas cidades
 * (a inicial e a mais distante) e depois insere a cidade mais distante do tour atual
 * na posição que menos aumenta o custo total.
 */
public class FarthestInsertion {

    /**
     * Executa o algoritmo de Farthest Insertion sobre uma lista de cidades.
     *
     * @param cities Lista de cidades a visitar.
     * @return Tour final representado como lista de cidades (ciclo fechado).
     */
    public static List<Utils.City> farthestInsertion(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        // Passo 1: Selecionar a cidade inicial e a cidade mais distante dela
        Utils.City start = cities.get(0);
        Utils.City farthest = null;
        double maxDist = -1;
        for (Utils.City city : cities) {
            if (city == start) continue;
            double d = start.distanceTo(city);
            if (d > maxDist) {
                maxDist = d;
                farthest = city;
            }
        }

        // Passo 2: Inicializar o tour com as duas cidades e fechar o ciclo
        List<Utils.City> tour = new ArrayList<>();
        tour.add(start);
        tour.add(farthest);
        tour.add(start); // fecha o ciclo

        // Passo 3: Conjunto de cidades por inserir
        Set<Utils.City> unvisited = new HashSet<>(cities);
        unvisited.remove(start);
        unvisited.remove(farthest);

        // Passo 4: Repetir até todas as cidades serem inseridas
        while (!unvisited.isEmpty()) {
            Utils.City candidate = null;
            double candidateDistance = -1;

            // Selecionar a cidade mais distante de qualquer cidade já no tour
            for (Utils.City city : unvisited) {
                double minDistance = Double.POSITIVE_INFINITY;
                for (Utils.City tCity : tour) {
                    double d = city.distanceTo(tCity);
                    if (d < minDistance) {
                        minDistance = d;
                    }
                }

                // Mantém a cidade cuja distância mínima ao tour é a maior
                if (minDistance > candidateDistance) {
                    candidateDistance = minDistance;
                    candidate = city;
                }
            }

            // Encontrar a melhor posição para inserir a cidade candidata no tour
            int bestInsertIndex = -1;
            double minIncrease = Double.POSITIVE_INFINITY;
            for (int i = 0; i < tour.size() - 1; i++) {
                Utils.City current = tour.get(i);
                Utils.City next = tour.get(i + 1);
                double increase = current.distanceTo(candidate) + candidate.distanceTo(next) - current.distanceTo(next);
                if (increase < minIncrease) {
                    minIncrease = increase;
                    bestInsertIndex = i + 1;
                }
            }

            // Inserir a cidade na posição ótima
            tour.add(bestInsertIndex, candidate);
            unvisited.remove(candidate);
        }

        return tour;
    }

    /**
     * Ponto de entrada para executar o algoritmo a partir de um ficheiro .tsp.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Ler ficheiro com as cidades
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Executar algoritmo de inserção mais distante
        List<Utils.City> tour = farthestInsertion(cities);

        // Mostrar o tour e o custo
        System.out.println("Farthest Insertion Tour:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
