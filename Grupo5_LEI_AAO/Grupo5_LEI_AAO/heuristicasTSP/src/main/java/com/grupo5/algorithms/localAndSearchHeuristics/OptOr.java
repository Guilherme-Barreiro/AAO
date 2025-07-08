package com.grupo5.algorithms.localAndSearchHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa a heurística Or-Opt para otimização de tours no Problema do Caixeiro Viajante (TSP).
 *
 * A Or-Opt tenta melhorar o tour movendo subsequências consecutivas de 1 a 3 cidades
 * para novas posições no tour. É uma técnica de otimização local leve, mas eficaz,
 * usada muitas vezes após uma heurística construtiva (como Nearest Neighbor ou MST).
 *
 * Esta implementação usa a estratégia de **first improvement** — aplica a primeira melhoria encontrada.
 */
public class OptOr {

    /** Tamanho máximo da subsequência a mover (tipicamente entre 1 e 3 cidades) */
    private static final int MAX_SEGMENT_LENGTH = 3;

    /**
     * Executa a heurística Or-Opt sobre um tour fechado.
     * Tenta mover pequenos segmentos de cidades para novas posições e aceita a primeira melhoria.
     *
     * @param tour Tour inicial (deve estar fechado: a primeira cidade repetida no fim).
     * @return Tour melhorado, se houver.
     */
    public static List<Utils.City> orOpt(List<Utils.City> tour) {
        boolean improvement = true;
        double bestDistance = calculatePathCost(tour);

        // Continua enquanto forem encontradas melhorias
        while (improvement) {
            improvement = false;

            outer:
            for (int i = 1; i < tour.size() - 1; i++) { // ignora posição inicial/final
                for (int len = 1; len <= MAX_SEGMENT_LENGTH && (i + len) < tour.size(); len++) {

                    // Define o segmento de cidades a mover
                    List<Utils.City> segment = new ArrayList<>(tour.subList(i, i + len));

                    // Remove o segmento do tour
                    List<Utils.City> tempTour = new ArrayList<>(tour);
                    for (int k = 0; k < len; k++) {
                        tempTour.remove(i);
                    }

                    // Tenta inserir o segmento em todas as posições válidas
                    for (int j = 1; j < tempTour.size(); j++) {
                        if (j == i || j == i - 1) continue; // ignorar posições idênticas

                        List<Utils.City> newTour = new ArrayList<>(tempTour);
                        newTour.addAll(j, segment); // insere o segmento

                        // Fecha o ciclo se necessário
                        if (!newTour.get(0).equals(newTour.get(newTour.size() - 1))) {
                            newTour.set(newTour.size() - 1, newTour.get(0));
                        }

                        double newDistance = calculatePathCost(newTour);

                        // Estratégia FIRST IMPROVEMENT: aplica a primeira melhoria que encontrar
                        if (newDistance < bestDistance) {
                            tour = newTour;
                            bestDistance = newDistance;
                            improvement = true;
                            break outer; // reinicia a busca após melhoria
                        }
                    }
                }
            }
        }

        return tour;
    }

    /**
     * Executa a heurística Or-Opt com base num ficheiro .tsp e imprime o resultado.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê o ficheiro com as cidades
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");
        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Cria tour inicial (ordem original + ciclo fechado)
        List<Utils.City> initialTour = new ArrayList<>(cities);
        initialTour.add(cities.get(0));

        System.out.println("Comprimento do tour inicial: " + calculatePathCost(initialTour));

        // Aplica Or-Opt
        List<Utils.City> improvedTour = orOpt(initialTour);
        System.out.println("Comprimento do tour após Or-Opt: " + calculatePathCost(improvedTour));

        System.out.println("Tour:");
        for (Utils.City city : improvedTour) {
            System.out.print(city + " ");
        }
        System.out.println();
    }
}
