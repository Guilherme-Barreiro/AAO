package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa uma heurística para o Problema do Caixeiro Viajante (TSP),
 * baseada na construção de uma Árvore Geradora Mínima (MST) seguida de uma
 * travessia em pré-ordem (DFS).
 *
 * A ideia é aproveitar a MST para obter uma aproximação eficiente e simples,
 * garantindo que todas as cidades estão ligadas com custo reduzido.
 */
public class MinimumSpanningTreeHeuristic {

    /**
     * Constrói uma Árvore Geradora Mínima (MST) usando o algoritmo de Prim.
     *
     * @param cities Lista de cidades a ligar.
     * @return Mapa (grafo) que representa a MST, onde cada cidade está ligada às suas vizinhas.
     */
    public static Map<Utils.City, List<Utils.City>> buildMST(List<Utils.City> cities) {
        Map<Utils.City, List<Utils.City>> mst = new HashMap<>();
        if (cities.isEmpty()) {
            return mst;
        }

        // Inicializa o mapa da MST com listas de vizinhos vazias
        for (Utils.City city : cities) {
            mst.put(city, new ArrayList<>());
        }

        // Conjunto de cidades já incluídas na MST
        Set<Utils.City> inMST = new HashSet<>();
        Utils.City start = cities.get(0);
        inMST.add(start);

        // Enquanto houver cidades fora da MST
        while (inMST.size() < cities.size()) {
            Utils.City bestFrom = null;
            Utils.City bestTo = null;
            double minDistance = Double.POSITIVE_INFINITY;

            // Procura a aresta de menor custo entre a MST atual e uma cidade fora dela
            for (Utils.City u : inMST) {
                for (Utils.City v : cities) {
                    if (inMST.contains(v)) continue;
                    double distance = u.distanceTo(v);
                    if (distance < minDistance) {
                        minDistance = distance;
                        bestFrom = u;
                        bestTo = v;
                    }
                }
            }

            // Adiciona a aresta à MST
            if (bestFrom != null && bestTo != null) {
                mst.get(bestFrom).add(bestTo);
                mst.get(bestTo).add(bestFrom);
                inMST.add(bestTo);
            }
        }

        return mst;
    }

    /**
     * Realiza uma travessia em profundidade (DFS) na MST a partir da cidade inicial,
     * visitando cada cidade uma vez, e depois volta ao ponto de partida.
     *
     * @param mst   Mapa da árvore geradora mínima.
     * @param start Cidade inicial da travessia.
     * @return Lista de cidades representando o tour aproximado (ciclo fechado).
     */
    public static List<Utils.City> preorderTraversal(Map<Utils.City, List<Utils.City>> mst, Utils.City start) {
        List<Utils.City> tour = new ArrayList<>();
        Set<Utils.City> visited = new HashSet<>();
        dfs(start, mst, visited, tour);

        // Fecha o ciclo voltando à cidade inicial
        tour.add(start);
        return tour;
    }

    /**
     * Função auxiliar para realizar DFS recursivo na MST.
     *
     * @param current Cidade atual da DFS.
     * @param mst     Mapa da MST.
     * @param visited Conjunto de cidades já visitadas.
     * @param tour    Lista parcial do tour a ser construída.
     */
    private static void dfs(Utils.City current, Map<Utils.City, List<Utils.City>> mst,
                            Set<Utils.City> visited, List<Utils.City> tour) {
        visited.add(current);
        tour.add(current);

        for (Utils.City neighbor : mst.get(current)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, mst, visited, tour);
            }
        }
    }

    /**
     * Heurística principal baseada em MST: constrói a MST e gera o tour via DFS.
     *
     * @param cities Lista de cidades a visitar.
     * @return Tour obtido pela heurística baseada em MST.
     */
    public static List<Utils.City> mstHeuristic(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        Map<Utils.City, List<Utils.City>> mst = buildMST(cities);
        return preorderTraversal(mst, cities.get(0));
    }

    /**
     * Função principal para executar a heurística com base num ficheiro de entrada.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê a lista de cidades do ficheiro TSP
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Executa a heurística baseada em MST
        List<Utils.City> tour = mstHeuristic(cities);

        // Imprime o tour e o seu custo total
        System.out.println("Tour obtido pela MST Heuristic:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();

        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
