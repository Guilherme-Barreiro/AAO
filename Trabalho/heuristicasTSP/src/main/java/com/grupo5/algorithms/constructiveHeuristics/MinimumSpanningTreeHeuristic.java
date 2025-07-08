package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Classe que implementa a heurística baseada em Árvores Geradoras Mínimas (MST) para o problema do caixeiro viajante.
 */
public class MinimumSpanningTreeHeuristic {

    /**
     * Constrói a MST usando o algoritmo de Prim e retorna um mapa que relaciona cada cidade com a lista de suas vizinhas na árvore.
     *
     * @param cities Lista de cidades a serem conectadas.
     * @return Mapa representando a MST, onde cada cidade está ligada às suas vizinhas.
     */
    public static Map<Utils.City, List<Utils.City>> buildMST(List<Utils.City> cities) {
        Map<Utils.City, List<Utils.City>> mst = new HashMap<>();
        if (cities.isEmpty()) {
            return mst;
        }

        /* Inicializa o mapa da MST com listas vazias para cada cidade */
        for (Utils.City city : cities) {
            mst.put(city, new ArrayList<>());
        }

        Set<Utils.City> inMST = new HashSet<>();
        Utils.City start = cities.get(0);
        inMST.add(start);

        /* Enquanto nem todas as cidades estiverem na MST */
        while (inMST.size() < cities.size()) {
            Utils.City bestFrom = null;
            Utils.City bestTo = null;
            double minDistance = Double.POSITIVE_INFINITY;

            /* Percorre as cidades já na MST */
            for (Utils.City u : inMST) {
                /* Percorre as cidades fora da MST e procura a aresta de menor peso */
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

            /* Adiciona a aresta encontrada na MST */
            if (bestFrom != null && bestTo != null) {
                mst.get(bestFrom).add(bestTo);
                mst.get(bestTo).add(bestFrom);
                inMST.add(bestTo);
            }
        }

        return mst;
    }

    /**
     * Realiza uma travessia em pré-ordem (DFS) na MST para gerar um tour aproximado do TSP.
     *
     * @param mst   Mapa da árvore geradora mínima.
     * @param start Cidade inicial da travessia.
     * @return Lista de cidades representando o tour.
     */
    public static List<Utils.City> preorderTraversal(Map<Utils.City, List<Utils.City>> mst, Utils.City start) {
        List<Utils.City> tour = new ArrayList<>();
        Set<Utils.City> visited = new HashSet<>();
        dfs(start, mst, visited, tour);

        /* Fecha o ciclo voltando para a cidade inicial */
        tour.add(start);
        return tour;
    }

    /**
     * Função auxiliar para DFS na árvore geradora mínima.
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
     * Heurística MST para TSP: constrói a MST e, a partir dela, gera um tour via DFS.
     *
     * @param cities Lista de cidades a serem visitadas.
     * @return Tour resultante da heurística.
     */
    public static List<Utils.City> mstHeuristic(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();
        Map<Utils.City, List<Utils.City>> mst = buildMST(cities);
        return preorderTraversal(mst, cities.get(0));
    }

    /**
     * Método principal para executar a heurística com um ficheiro de entrada.
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        List<Utils.City> tour = mstHeuristic(cities);
        System.out.println("Tour obtido pela MST Heuristic:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
