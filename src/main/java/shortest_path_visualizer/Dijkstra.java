package shortest_path_visualizer;

import java.util.ArrayList;

public class Dijkstra {
    private char[][] karttamatriisi;
    private int [][] solmuMatriisi;
    private ArrayList<Integer>[] verkko;

    public Dijkstra(char[][] karttamatriisi) {
        this.karttamatriisi = karttamatriisi;
        this.solmuMatriisi = new int[karttamatriisi.length][karttamatriisi[0].length];
    }

    public void haeLyhinReitti() {
        
    }

    public void initVerkko() {
        int solmut = karttamatriisi.length * karttamatriisi[0].length;
        this.verkko = new ArrayList[solmut + 1];

        int solmunumero = 1;
        for (int i = 0; i < karttamatriisi.length; i ++) {
            for (int j = 0; j < karttamatriisi[0].length; j ++) {
                solmuMatriisi[i][j] = solmunumero;
                solmunumero ++;
            }
        }
        for (int i = 0; i < karttamatriisi.length; i ++) {
            for (int j = 0; j < karttamatriisi[0].length; j ++) {
                verkko[solmuMatriisi[i][j]] = haeNaapurisolmut(j ,i);
            }
        }
    }

    public void printSolmumatriisi() {
        for (int i = 0; i < solmuMatriisi.length; i ++) {
            for (int j = 0; j < solmuMatriisi[0].length; j ++) {
                System.out.print(solmuMatriisi[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printKaaret() {
        for (int i = 1; i < verkko.length; i ++) {
            System.out.println("Solmu: " + i);
            for (int j = 0; j < verkko[i].size(); j ++) {
                System.out.print(verkko[i].get(j) + " ");
            }
            System.out.println();
        }

    }

    private ArrayList<Integer> haeNaapurisolmut(int currentX, int currentY) {
        ArrayList<Integer> naapurit = new ArrayList<>();

        if (karttamatriisi[currentY][currentX] != 'T') {
            // Vasen yläkulma
            if (currentX == 0 && currentY == 0) {
                checkSouth(currentX, currentY, naapurit);
                checkEast(currentX, currentY, naapurit);
            }
            // Oikea yläkulma
            else if (currentX == karttamatriisi[0].length - 1 && currentY == 0) {
                checkSouth(currentX, currentY, naapurit);
                checkWest(currentX, currentY, naapurit);
            }
            // Vasen alakulma
            else if (currentX == 0 && currentY == karttamatriisi.length - 1) {
                checkNorth(currentX, currentY, naapurit);
                checkWest(currentX, currentY, naapurit);
            }
            // Oikea alakulma
            else if (currentX == karttamatriisi[0].length -1 && currentY == karttamatriisi.length - 1) {
                checkNorth(currentX, currentY, naapurit);
                checkWest(currentX, currentY, naapurit);
            }
            // Vasen reuna
            else if (currentX == 0) {
                checkNorth(currentX, currentY, naapurit);
                checkSouth(currentX, currentY, naapurit);
                checkEast(currentX, currentY, naapurit);
            }
            // Oikea reuna
            else if (currentX == karttamatriisi[0].length - 1) {
                checkNorth(currentX, currentY, naapurit);
                checkSouth(currentX, currentY, naapurit);
                checkWest(currentX, currentY, naapurit);
            }
            // Alareuna
            else if (currentY == karttamatriisi.length - 1) {
                checkNorth(currentX, currentY, naapurit);
                checkWest(currentX, currentY, naapurit);
                checkEast(currentX, currentY, naapurit);
            }
            // Yläreuna
            else if (currentY == 0) {
                checkSouth(currentX, currentY, naapurit);
                checkWest(currentX, currentY, naapurit);
                checkEast(currentX, currentY, naapurit);
            }
            // "Keskellä"
            else {
                checkNorth(currentX, currentY, naapurit);
                checkSouth(currentX, currentY, naapurit);
                checkWest(currentX, currentY, naapurit);
                checkEast(currentX, currentY, naapurit);
            }
        }
        return naapurit;
    }

    private void checkSouth(int currentX, int currentY, ArrayList<Integer> naapurit) {
        if (karttamatriisi[currentY + 1][currentX] != 'T') {
            naapurit.add(solmuMatriisi[currentY + 1][currentX]);
        }
    }

    private void checkNorth(int currentX, int currentY, ArrayList<Integer> naapurit) {
        if (karttamatriisi[currentY - 1][currentX] != 'T') {
            naapurit.add(solmuMatriisi[currentY - 1][currentX ]);
        }
    }

    private void checkEast(int currentX, int currentY, ArrayList<Integer> naapurit) {
        if (karttamatriisi[currentY][currentX + 1] != 'T') {
            naapurit.add(solmuMatriisi[currentY][currentX + 1]);
        }
    }

    private void checkWest(int currentX, int currentY, ArrayList<Integer> naapurit) {
        if (karttamatriisi[currentY][currentX - 1] != 'T') {
            naapurit.add(solmuMatriisi[currentY][currentX - 1]);
        }
    }
}
