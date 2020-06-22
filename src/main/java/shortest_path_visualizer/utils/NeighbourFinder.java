package shortest_path_visualizer.utils;

public class NeighbourFinder {
  private char[][] karttamatriisi;
  private Node[][] solmuMatriisi;
  private int iNaapurilista;

  public NeighbourFinder(char[][] karttamatriisi, Node[][] solmuMatriisi) {
    this.karttamatriisi = karttamatriisi;
    this.solmuMatriisi = solmuMatriisi;
    this.iNaapurilista = 0;
  }

  /**
   * Metodi hakee matriisikartan solmulle kaikki naapurisolmut, eli solmut jotka eivät ole esteitä.
   * Tarkasteltavan solmun x- ja y-koordinaatit annettu parametreina.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @return lista naapurisolmuista
   */

  public Node[] haeNaapurisolmut(int currentX, int currentY) {
    Node[] naapurit = new Node[8];
    iNaapurilista = 0;

    if (karttamatriisi[currentY][currentX] != '@') {
      // Vasen yläkulma
      if (currentX == 0 && currentY == 0) {
        checkEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouthEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
      }
      // Oikea yläkulma
      else if (currentX == karttamatriisi[0].length - 1 && currentY == 0) {
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkSouthWest(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // Vasen alakulma
      else if (currentX == 0 && currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkNorthEast(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
      }
      // Oikea alakulma
      else if (currentX == karttamatriisi[0].length - 1 &&
          currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
        checkNorthWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // Vasen reuna
      else if (currentX == 0) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkNorthEast(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouthEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
      }
      // Oikea reuna
      else if (currentX == karttamatriisi[0].length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkSouthWest(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
        checkNorthWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // Alareuna
      else if (currentY == karttamatriisi.length - 1) {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkNorthEast(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
        checkNorthWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // Yläreuna
      else if (currentY == 0) {
        checkEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouthEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkSouthWest(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
      }
      // "Keskellä"
      else {
        checkNorth(currentX, currentY, naapurit, iNaapurilista);
        checkNorthEast(currentX, currentY, naapurit, iNaapurilista);
        checkEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouthEast(currentX, currentY, naapurit, iNaapurilista);
        checkSouth(currentX, currentY, naapurit, iNaapurilista);
        checkSouthWest(currentX, currentY, naapurit, iNaapurilista);
        checkWest(currentX, currentY, naapurit, iNaapurilista);
        checkNorthWest(currentX, currentY, naapurit, iNaapurilista);
      }
    }
    return naapurit;
  }

  /**
   * Tarkistaa onko naapurisolmuja etelässä.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkSouth(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY + 1][currentX] != '@') {
      naapurit[indeksi] = solmuMatriisi[currentY + 1][currentX];
      iNaapurilista ++;
    }
  }

  /**
   * Tarkistaa onko naapurisolmuja pohjoisessa.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkNorth(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY - 1][currentX] != '@') {
      naapurit[indeksi] = solmuMatriisi[currentY - 1][currentX];
      iNaapurilista ++;
    }
  }

  private void checkNorthEast(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY - 1][currentX + 1] != '@') {
    //&& (karttamatriisi[currentY - 1][currentX] != '@' && karttamatriisi[currentY][currentX + 1] != '@')) {
      naapurit[indeksi] = solmuMatriisi[currentY - 1][currentX + 1];
      iNaapurilista ++;
    }
  }

  private void checkNorthWest(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY - 1][currentX - 1] != '@') {
    //&& (karttamatriisi[currentY - 1][currentX] != '@' && karttamatriisi[currentY][currentX - 1] != '@')) {
      naapurit[indeksi] = solmuMatriisi[currentY - 1][currentX -1];
      iNaapurilista ++;
    }
  }

  /**
   * Tarkistaa onko naapurisolmuja idässä.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkEast(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY][currentX + 1] != '@') {
      naapurit[indeksi] = solmuMatriisi[currentY][currentX + 1];
      iNaapurilista ++;
    }
  }

  private void checkSouthEast(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY + 1][currentX + 1] != '@') {
    //&& (karttamatriisi[currentY + 1][currentX] != '@' && karttamatriisi[currentY][currentX + 1] != '@')) {
      naapurit[indeksi] = solmuMatriisi[currentY + 1][currentX + 1];
      iNaapurilista ++;
    }
  }

  /**
   * Tarkistaa onko naapurisolmuja lännessä.
   *
   * @param currentX solmun sarake matriisissa
   * @param currentY solmun rivi matriisissa
   * @param naapurit lista, johon naapurisolmut lisätään
   */

  private void checkWest(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY][currentX - 1] != '@') {
      naapurit[indeksi] = solmuMatriisi[currentY][currentX - 1];
      iNaapurilista ++;
    }
  }

  private void checkSouthWest(int currentX, int currentY, Node[] naapurit, int indeksi) {
    if (karttamatriisi[currentY + 1][currentX - 1] != '@') { //&&
        //(karttamatriisi[currentY + 1][currentX] != '@' && karttamatriisi[currentY][currentX - 1] != '@')) {
      naapurit[indeksi] = solmuMatriisi[currentY + 1][currentX - 1];
      iNaapurilista ++;
    }
  }
}
