package com.grupo5.algorithms.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Classe utilitária para leitura de instâncias TSP e manipulação de cidades.
 */
public class Utils {

    /**
     * Classe para representar uma cidade com coordenadas 2D.
     */
    public static class City {
        int id;
        double x, y;

        /**
         * Construtor da cidade.
         *
         * @param id Identificador da cidade.
         * @param x  Coordenada X.
         * @param y  Coordenada Y.
         */
        public City(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        /**
         * Calcula a distância Euclidiana entre esta cidade e outra cidade.
         *
         * @param other A outra cidade.
         * @return Distância entre as duas cidades.
         */
        public double distanceTo(City other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }

    /**
     * Lê um ficheiro .tsp no formato TSPLIB e retorna uma lista de cidades.
     *
     * @param fileName Caminho para o ficheiro .tsp.
     * @return Lista de cidades extraída do ficheiro.
     */
    public static List<City> readTSPFile(String fileName) {
        List<City> cities = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            boolean start = false;
            for (String line : lines) {
                line = line.trim();
                if (line.equals("NODE_COORD_SECTION")) {
                    start = true;
                    continue;
                }
                if (line.equals("EOF")) {
                    break;
                }
                if (start) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3) {
                        int id = Integer.parseInt(parts[0]);
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        cities.add(new City(id, x, y));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    /**
     * Calcula o custo total de um tour, somando a distância entre cidades consecutivas.
     *
     * @param tour Lista de cidades representando o tour (deve estar fechado).
     * @return Custo total do tour.
     */
    public static double calculatePathCost(List<Utils.City> tour) {
        double total = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            total += tour.get(i).distanceTo(tour.get(i + 1));
        }
        return total;
    }
}
