package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementação da heurística construtiva "Cheapest Insertion" (Inserção Mais Barata)
 * para o Problema do Caixeiro Viajante (TSP).
 *
 * A ideia principal desta heurística é construir iterativamente um tour,
 * começando com duas cidades (a inicial e a mais distante), e depois inserir
 * progressivamente as restantes cidades na posição que cause o menor aumento
 * possível no custo total do percurso.
 *
 * Esta abordagem permite gerar soluções iniciais razoavelmente boas de forma rápida.
 */
public class CheapestInsertion {

    /**
     * Executa o algoritmo de Cheapest Insertion sobre uma lista de cidades.
     *
     * @param cities Lista de cidades a visitar.
     * @return Lista de cidades representando o tour (ciclo fechado).
     */
    public static List<Utils.City> cheapestInsertion(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        // Passo 1: Selecionar a cidade inicial (a primeira da lista)
        Utils.City start = cities.get(0);

        // Passo 2: Encontrar a cidade mais distante da cidade inicial
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

        // Passo 3: Inicializar o tour com duas cidades: start -> farthest -> start
        List<Utils.City> tour = new ArrayList<>();
        tour.add(start);
        tour.add(farthest);
        tour.add(start); // fecha o ciclo

        // Passo 4: Cidades por visitar (todas menos as já inseridas)
        Set<Utils.City> unvisited = new HashSet<>(cities);
        unvisited.remove(start);
        unvisited.remove(farthest);

        // Passo 5: Enquanto houver cidades por inserir
        while (!unvisited.isEmpty()) {
            Utils.City bestCity = null;
            int bestInsertIndex = -1;
            double minIncrease = Double.POSITIVE_INFINITY;

            // Para cada cidade não inserida, calcular o aumento de custo em cada posição possível do tour
            for (Utils.City city : unvisited) {
                for (int i = 0; i < tour.size() - 1; i++) {
                    Utils.City current = tour.get(i);
                    Utils.City next = tour.get(i + 1);
                    double increase = current.distanceTo(city) + city.distanceTo(next) - current.distanceTo(next);
                    if (increase < minIncrease) {
                        minIncrease = increase;
                        bestCity = city;
                        bestInsertIndex = i + 1;
                    }
                }
            }

            // Inserir a cidade na posição ótima encontrada
            tour.add(bestInsertIndex, bestCity);
            unvisited.remove(bestCity);
        }

        // Tour finalizado (ciclo fechado)
        return tour;
    }

    /**
     * Função principal para executar o algoritmo a partir de um ficheiro TSP.
     *
     * @param args Argumentos de linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê a lista de cidades a partir de um ficheiro .tsp
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Executa o algoritmo Cheapest Insertion
        List<Utils.City> tour = cheapestInsertion(cities);

        // Imprime o tour calculado
        System.out.println("Tour encontrado:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }

        // Calcula e imprime o custo total do tour
        double cost = calculatePathCost(tour);
        System.out.println("\nCusto total do tour: " + cost);
    }
}
