package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa a heurística Or-Opt para otimização de tours no problema do caixeiro viajante.
 */
public class OptOr {

    /** Tamanho máximo da subsequência a mover (1 a 3) */
    private static final int MAX_SEGMENT_LENGTH = 3;

    /**
     * Aplica a heurística Or-Opt para melhorar o tour.
     *
     * @param tour Tour inicial (lista de cidades com ciclo fechado).
     * @return Tour melhorado, se houver redução no custo total.
     */
    public static List<Utils.City> orOpt(List<Utils.City> tour) {
        boolean improvement = true;
        double bestDistance = calculatePathCost(tour);

        /* Continuar enquanto houver melhoria */
        while (improvement) {
            improvement = false;

            outer:
            for (int i = 1; i < tour.size() - 1; i++) { // evita o primeiro e o último (ciclo fechado)
                for (int len = 1; len <= MAX_SEGMENT_LENGTH && (i + len) < tour.size(); len++) {

                    /* Define a subsequência a mover */
                    List<Utils.City> segment = new ArrayList<>(tour.subList(i, i + len));

                    /* Cria um tour sem o segmento */
                    List<Utils.City> tempTour = new ArrayList<>(tour);
                    for (int k = 0; k < len; k++) {
                        tempTour.remove(i);
                    }

                    /* Tenta reinserir o segmento em todas as possíveis posições */
                    for (int j = 1; j < tempTour.size(); j++) {
                        if (j == i || j == i - 1) continue; // posição sem mudança

                        List<Utils.City> newTour = new ArrayList<>(tempTour);
                        newTour.addAll(j, segment);

                        /* Fecha o ciclo se não estiver fechado */
                        if (!newTour.get(0).equals(newTour.get(newTour.size() - 1))) {
                            newTour.set(newTour.size() - 1, newTour.get(0));
                        }

                        double newDistance = calculatePathCost(newTour);
                        if (newDistance < bestDistance) {
                            tour = newTour;
                            bestDistance = newDistance;
                            improvement = true;
                            break outer; // Reinicia a busca após melhoria
                        }
                    }
                }
            }
        }

        return tour;
    }
    
    /**
     * Executa a heurística Or-Opt num conjunto de cidades lido de ficheiro.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        /* Cria um tour inicial simples: a ordem das cidades + ciclo fechado */
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0)); // fechar o ciclo

        System.out.println("Comprimento do tour inicial: " + calculatePathCost(initialTour));

        /* Aplica o Or-Opt para melhorar o tour */
        List<Utils.City> improvedTour = orOpt(initialTour);
        System.out.println("Comprimento do tour após Or-Opt: " + calculatePathCost(improvedTour));

        System.out.println("Tour:");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
    }
}
