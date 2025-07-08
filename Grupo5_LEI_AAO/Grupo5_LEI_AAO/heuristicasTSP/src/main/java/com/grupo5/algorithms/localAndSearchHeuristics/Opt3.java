package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa uma versão simplificada da heurística 3-Opt para o Problema do Caixeiro Viajante (TSP).
 *
 * Esta implementação utiliza a estratégia de **first improvement**:
 * assim que encontra uma reordenação que reduz o custo do tour, aplica-a imediatamente,
 * sem procurar por outras opções possivelmente melhores.
 */
public class Opt3 {

    /**
     * Aplica a heurística 3-Opt repetidamente enquanto encontrar melhorias.
     * Usa a estratégia **first improvement**: aplica a primeira melhoria válida que encontra.
     *
     * @param tour Tour inicial (deve estar fechado: a primeira cidade aparece no fim).
     * @return Tour melhorado após aplicar reordenações 3-Opt.
     */
    public static List<Utils.City> opt3(List<Utils.City> tour) {
        boolean improvement = true;
        double bestDistance = calculatePathCost(tour);

        // Continua enquanto encontrar melhorias
        while (improvement) {
            improvement = false;

            // Percorre trios de índices (i < j < k) para definir dois segmentos a inverter
            for (int i = 1; i < tour.size() - 5; i++) {
                for (int j = i + 2; j < tour.size() - 3; j++) {
                    for (int k = j + 2; k < tour.size() - 1; k++) {
                        // Aplica uma troca 3-Opt e calcula o novo custo
                        List<Utils.City> newTour = threeOptSwap(tour, i, j, k);
                        double newDistance = calculatePathCost(newTour);

                        // Estratégia de FIRST IMPROVEMENT:
                        // aplica imediatamente a primeira troca que melhora o custo
                        if (newDistance < bestDistance) {
                            tour = newTour;
                            bestDistance = newDistance;
                            improvement = true;
                            // quebra implícita: reinicia o ciclo externo
                        }
                    }
                }
            }
        }
        return tour;
    }

    /**
     * Realiza uma troca 3-Opt simples:
     * inverte o segmento [i, j) e depois o segmento [j, k).
     * Esta é uma das múltiplas possíveis variações do 3-Opt.
     *
     * @param tour Tour atual.
     * @param i Início do primeiro segmento.
     * @param j Fim do primeiro segmento / início do segundo.
     * @param k Fim do segundo segmento.
     * @return Novo tour com os dois segmentos invertidos.
     */
    private static List<Utils.City> threeOptSwap(List<Utils.City> tour, int i, int j, int k) {
        List<Utils.City> newTour = new ArrayList<>();

        // Mantém o segmento inicial [0, i)
        newTour.addAll(tour.subList(0, i));

        // Inverte o segmento [i, j)
        List<Utils.City> segment1 = new ArrayList<>(tour.subList(i, j));
        Collections.reverse(segment1);
        newTour.addAll(segment1);

        // Inverte o segmento [j, k)
        List<Utils.City> segment2 = new ArrayList<>(tour.subList(j, k));
        Collections.reverse(segment2);
        newTour.addAll(segment2);

        // Mantém o segmento restante [k, fim)
        newTour.addAll(tour.subList(k, tour.size()));

        return newTour;
    }

    /**
     * Ponto de entrada para aplicar a heurística 3-Opt sobre um conjunto de cidades lido de ficheiro.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê o ficheiro .tsp com as cidades
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Tour inicial: ordem original + cidade inicial no fim (ciclo fechado)
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0));

        System.out.println("Tour inicial (comprimento: " + calculatePathCost(initialTour) + "):");
        for (Utils.City city : initialTour) {
            System.out.print(city + " ");
        }
        System.out.println();

        // Aplica o algoritmo 3-Opt
        List<Utils.City> improvedTour = opt3(initialTour);
        System.out.println("Tour melhorado pelo 3-Opt (comprimento: " + calculatePathCost(improvedTour) + "):");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
    }
}
