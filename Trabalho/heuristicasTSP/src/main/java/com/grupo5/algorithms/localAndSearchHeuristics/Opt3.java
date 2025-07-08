package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa a heurística 3-Opt para otimização de tours no problema do caixeiro viajante (TSP).
 */
public class Opt3 {

    /**
     * Aplica o 3-Opt ao tour enquanto houver melhoria.
     *
     * @param tour Tour inicial (deve estar fechado).
     * @return Tour melhorado após aplicar 3-Opt.
     */
    public static List<Utils.City> opt3(List<Utils.City> tour) {
        boolean improvement = true;
        double bestDistance = calculatePathCost(tour);

        while (improvement) {
            improvement = false;

            /* Percorre os índices do tour; ignoramos o primeiro e o último (fechamento do ciclo) */
            for (int i = 1; i < tour.size() - 5; i++) {
                for (int j = i + 2; j < tour.size() - 3; j++) {
                    for (int k = j + 2; k < tour.size() - 1; k++) {
                        /* Testa uma reordenação 3-Opt (exemplo: inverte dois segmentos) */
                        List<Utils.City> newTour = threeOptSwap(tour, i, j, k);
                        double newDistance = calculatePathCost(newTour);
                        if (newDistance < bestDistance) {
                            tour = newTour;
                            bestDistance = newDistance;
                            improvement = true;
                        }
                    }
                }
            }
        }
        return tour;
    }

    /**
     * Realiza uma reordenação 3-Opt: inverte o segmento entre i e j e também entre j e k.
     * Esta é uma das várias possíveis reordenações 3-Opt.
     *
     * @param tour Tour atual.
     * @param i Início do primeiro segmento a inverter.
     * @param j Fim do primeiro e início do segundo segmento.
     * @param k Fim do segundo segmento.
     * @return Novo tour após aplicar a troca.
     */
    private static List<Utils.City> threeOptSwap(List<Utils.City> tour, int i, int j, int k) {
        List<Utils.City> newTour = new ArrayList<>();

        /* Mantém o segmento [0, i) */
        newTour.addAll(tour.subList(0, i));

        /* Inverte o segmento [i, j) */
        List<Utils.City> segment1 = new ArrayList<>(tour.subList(i, j));
        Collections.reverse(segment1);
        newTour.addAll(segment1);

        /* Inverte o segmento [j, k) */
        List<Utils.City> segment2 = new ArrayList<>(tour.subList(j, k));
        Collections.reverse(segment2);
        newTour.addAll(segment2);

        /* Adiciona o restante do tour [k, end) */
        newTour.addAll(tour.subList(k, tour.size()));

        return newTour;
    }

    /**
     * Executa a heurística 3-Opt a partir de um ficheiro de cidades TSP.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        /* Carrega as cidades usando o utilitário definido em Utils */
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        /* Usa uma solução inicial simples; pode ser a ordem original */
        List<Utils.City> initialTour = new ArrayList<>(cities);

        /* Para fechar o ciclo: */
        initialTour.add(cities.get(0));

        System.out.println("Tour inicial (comprimento: " + calculatePathCost(initialTour) + "):");
        for (Utils.City city : initialTour) {
            System.out.print(city + " ");
        }
        System.out.println();

        /* Aplica a melhoria via 3-Opt */
        List<Utils.City> improvedTour = opt3(initialTour);
        System.out.println("Tour melhorado pelo 3-Opt (comprimento: " + calculatePathCost(improvedTour) + "):");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
    }
}
