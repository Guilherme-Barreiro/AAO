package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementação simplificada da heurística de Lin-Kernighan para o Problema do Caixeiro Viajante (TSP).
 *
 * Esta versão aplica apenas melhorias com base em movimentos 2-Opt, onde se trocam dois segmentos
 * do tour para tentar reduzir o custo total. Apesar de simples, esta abordagem já permite
 * eliminar muitas arestas cruzadas e encontrar soluções significativamente melhores.
 */
public class LinKernighanHeuristic {

    /**
     * Executa uma versão simplificada da heurística de Lin-Kernighan,
     * utilizando apenas movimentos 2-Opt até não haver mais melhorias.
     *
     * @param initialTour Tour inicial (deve estar fechado, ou seja, a primeira cidade repetida no fim).
     * @return Tour otimizado com base em melhorias locais.
     */
    public static List<Utils.City> linKernighan(List<Utils.City> initialTour) {
        List<Utils.City> bestTour = new ArrayList<>(initialTour);
        boolean improvement = true;

        // Repete enquanto houver melhorias no tour
        while (improvement) {
            improvement = false;

            // Percorre todos os pares de índices para tentar aplicar 2-Opt
            for (int i = 1; i < bestTour.size() - 2; i++) {
                for (int j = i + 1; j < bestTour.size() - 1; j++) {
                    double delta = - bestTour.get(i - 1).distanceTo(bestTour.get(i))
                            - bestTour.get(j).distanceTo(bestTour.get(j + 1))
                            + bestTour.get(i - 1).distanceTo(bestTour.get(j))
                            + bestTour.get(i).distanceTo(bestTour.get(j + 1));

                    // Se a troca reduzir o custo do tour, aplica a inversão
                    if (delta < -1e-6) { // margem de tolerância para evitar erros numéricos
                        reverseSegment(bestTour, i, j);
                        improvement = true;
                    }
                }
            }
        }

        return bestTour;
    }

    /**
     * Inverte a ordem de um segmento do tour entre os índices i e j (inclusive).
     * Este é o movimento fundamental do 2-Opt.
     *
     * @param tour Lista de cidades que representa o tour.
     * @param i Índice inicial do segmento a inverter.
     * @param j Índice final do segmento a inverter.
     */
    private static void reverseSegment(List<Utils.City> tour, int i, int j) {
        while (i < j) {
            Collections.swap(tour, i, j);
            i++;
            j--;
        }
    }

    /**
     * Método principal para executar a heurística com um ficheiro de entrada.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê as cidades do ficheiro
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Cria um tour inicial simples (ordem original do ficheiro)
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0)); // Fecha o ciclo

        // Aplica a heurística (versão simplificada de Lin-Kernighan baseada em 2-Opt)
        List<Utils.City> improvedTour = linKernighan(initialTour);

        // Imprime o tour resultante
        System.out.println("Lin-Kernighan Heuristic (simplified) Tour:");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();

        // Imprime o custo total do tour
        double cost = calculatePathCost(improvedTour);
        System.out.println("Custo total do tour: " + cost);
    }
}
