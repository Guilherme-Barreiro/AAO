package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementação simplificada da heurística de Lin-Kernighan, usando movimentos de 2-Opt iterativos.
 */
public class LinKernighanHeuristic {

    /**
     * Um exemplo simplificado que aplica melhorias inspiradas no Lin-Kernighan
     * usando movimentos de 2-Opt iterativos.
     *
     * @param initialTour Tour inicial (deve estar fechado: a primeira cidade repetida no fim).
     * @return Tour melhorado.
     */
    public static List<Utils.City> linKernighan(List<Utils.City> initialTour) {
        List<Utils.City> bestTour = new ArrayList<>(initialTour);
        boolean improvement = true;

        while (improvement) {
            improvement = false;

            /* Tenta todos os possíveis movimentos 2-Opt (troca de segmentos) */
            for (int i = 1; i < bestTour.size() - 2; i++) {
                for (int j = i + 1; j < bestTour.size() - 1; j++) {
                    double delta = - bestTour.get(i - 1).distanceTo(bestTour.get(i))
                            - bestTour.get(j).distanceTo(bestTour.get(j + 1))
                            + bestTour.get(i - 1).distanceTo(bestTour.get(j))
                            + bestTour.get(i).distanceTo(bestTour.get(j + 1));

                    /* Se houver melhoria significativa */
                    if (delta < -1e-6) {
                        reverseSegment(bestTour, i, j);
                        improvement = true;
                    }
                }
            }
        }

        return bestTour;
    }

    /**
     * Método auxiliar para inverter um segmento do tour (opera a troca 2-opt).
     *
     * @param tour Lista de cidades representando o tour.
     * @param i Índice inicial do segmento.
     * @param j Índice final do segmento.
     */
    private static void reverseSegment(List<Utils.City> tour, int i, int j) {
        while (i < j) {
            Collections.swap(tour, i, j);
            i++;
            j--;
        }
    }

    /**
     * Ponto de entrada do programa. Lê o ficheiro TSP, aplica a heurística e imprime o tour.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        /* Lê as cidades do ficheiro TSP */
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        /* Para iniciar, cria um tour simples (por exemplo, usando a ordem natural das cidades) */
        List<Utils.City> initialTour = new ArrayList<>(cities);

        /* Fecha o ciclo: adiciona a cidade inicial no fim */
        initialTour.add(cities.get(0));

        /* Aplica a heurística (neste exemplo, uma melhoria iterativa tipo 2-opt) */
        List<Utils.City> improvedTour = linKernighan(initialTour);

        System.out.println("Lin-Kernighan Heuristic (simplified) Tour:");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(improvedTour);
        System.out.println("Custo total do tour: " + cost);
    }
}
