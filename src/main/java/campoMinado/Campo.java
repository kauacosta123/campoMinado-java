package campoMinado;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class Campo {

    public static boolean estaVazio(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static String removerEspacos(String texto) {
        int inicio = 0;
        int fim = texto.length() - 1;

        while (inicio <= fim && texto.charAt(inicio) == ' ') {
            inicio++;
        }
        while (fim >= inicio && texto.charAt(fim) == ' ') {
            fim--;
        }

        if (inicio > fim) {
            return "";
        }

        return texto.substring(inicio, fim + 1);
    }

    public static boolean soDigitos(String texto) {
        if (texto.length() == 0) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            boolean digito = texto.charAt(i) >= '0' && texto.charAt(i) <= '9';
            if (!digito) {
                return false;
            }
        }
        return true;
    }

    public static int lerInteiro(int min, int max) {
        Scanner leia = new Scanner(System.in);
        int valor = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            String entrada = removerEspacos(leia.nextLine());

            if (estaVazio(entrada)) {
                System.out.print("  Entrada vazia! Digite entre " + min + " e " + max + ": ");
                continue;
            }

            if (!soDigitos(entrada)) {
                System.out.print("  Apenas numeros! Entre " + min + " e " + max + ": ");
                continue;
            }

            valor = 0;
            for (int i = 0; i < entrada.length(); i++) {
                valor = valor * 10 + (entrada.charAt(i) - '0');
            }

            boolean dentroDoIntervalo = valor >= min && valor <= max;
            if (dentroDoIntervalo) {
                entradaValida = true;
            } else {
                System.out.print("  Invalido! Digite entre " + min + " e " + max + ": ");
            }
        }

        return valor;
    }

    public static int menu() {
        System.out.println("===============================================");
        System.out.println("|                                             |");
        System.out.println("|          1 - Iniciar Jogo                   |");
        System.out.println("|          2 - Como Funciona                  |");
        System.out.println("|                                             |");
        System.out.println("===============================================");
        System.out.print("O que deseja fazer: ");
        return lerInteiro(1, 2);
    }

    public static int[][] getTamanhos() {
        System.out.println("Qual opcao voce quer jogar: ");
        System.out.println("1- Facil (6x6) ; 2- Medio (9x9) ; 3- Dificil (12x12)");
        int op = lerInteiro(1, 3);

        int tamanho;
        if (op == 1) {
            tamanho = 6;
        } else if (op == 2) {
            tamanho = 9;
        } else {
            tamanho = 12;
        }

        return new int[tamanho][tamanho];
    }

    public static int getBombas(int[][] matriz) {
        int bombas;

        if (matriz.length == 6) {
            bombas = 8;
        } else if (matriz.length == 9) {
            bombas = 16;
        } else {
            bombas = 22;
        }

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = 0;
            }
        }

        return bombas;
    }

    public static void inicioJogo(int[][] matriz, int bombas) {
        System.out.printf("  ");
        for (int i = 0; i < matriz.length; i++) {
            System.out.printf(" %2d", i);
        }
        System.out.println();

        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("%2d ", i);
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("[ ]");
            }
            System.out.println();
        }

        Random rand = new Random();
        for (int cont = 0; cont < bombas; cont++) {
            int lin = rand.nextInt(matriz.length);
            int col = rand.nextInt(matriz.length);
            while (matriz[lin][col] == 1) {
                lin = rand.nextInt(matriz.length);
                col = rand.nextInt(matriz.length);
            }
            matriz[lin][col] = 1;
        }
    }

    public static boolean mainJogo(int[][] matriz, int bomba) {
        boolean perdeu = false;
        int cont = 0;
        boolean[][] aberto = new boolean[matriz.length][matriz.length];
        int celulasSemBomba = matriz.length * matriz.length - bomba;

        do {
            System.out.println("Escolha a linha (0 a " + (matriz.length - 1) + ")");
            int linha = lerInteiro(0, matriz.length - 1);

            System.out.println("Escolha a Coluna (0 a " + (matriz.length - 1) + ")");
            int col = lerInteiro(0, matriz.length - 1);

            while (aberto[linha][col]) {
                System.out.println("Posicao ja selecionada. Escolha novamente.");
                System.out.println("Escolha a linha (0 a " + (matriz.length - 1) + ")");
                linha = lerInteiro(0, matriz.length - 1);
                System.out.println("Escolha a Coluna (0 a " + (matriz.length - 1) + ")");
                col = lerInteiro(0, matriz.length - 1);
            }

            if (matriz[linha][col] == 1) {
                System.out.println("BOOM! Voce perdeu!");
                perdeu = true;
                break;
            } else {
                aberto[linha][col] = true;

                // Limpa tela
                for (int k = 0; k < 25; k++) System.out.println();

                System.out.printf("  ");
                for (int i = 0; i < matriz.length; i++) {
                    System.out.printf(" %3d", i);
                }
                System.out.println();

                for (int i = 0; i < matriz.length; i++) {
                    System.out.printf("%2d ", i);
                    for (int j = 0; j < matriz[i].length; j++) {
                        if (aberto[i][j]) {
                            System.out.printf("[%2d]", contarBombas(matriz, i, j));
                        } else {
                            System.out.printf("[  ]");
                        }
                    }
                    System.out.println();
                }

                cont++;
            }

            if (cont == celulasSemBomba) {
                System.out.println("Parabens!! Voce ganhou!!!");
                break;
            }

        } while (cont < celulasSemBomba);

        // Revela todas as bombas ao final
        System.out.printf("  ");
        for (int j = 0; j < matriz.length; j++) {
            System.out.printf("%3d ", j);
        }
        System.out.println();

        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("%2d ", i);
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == 1) {
                    System.out.printf("[💣]");
                } else {
                    System.out.printf("[  ]");
                }
            }
            System.out.println();
        }

        return perdeu;
    }

    public static int contarBombas(int[][] matriz, int linha, int col) {
        int contB = 0;
        int maxL = matriz.length - 1;
        int maxC = matriz[0].length - 1;

        if (linha == 0 && col == 0) {
            contB += matriz[0][1] + matriz[1][0] + matriz[1][1];

        } else if (linha == 0 && col == maxC) {
            contB += matriz[0][maxC - 1] + matriz[1][maxC] + matriz[1][maxC - 1];

        } else if (linha == maxL && col == 0) {
            contB += matriz[maxL][1] + matriz[maxL - 1][0] + matriz[maxL - 1][1];

        } else if (linha == maxL && col == maxC) {
            contB += matriz[maxL][maxC - 1] + matriz[maxL - 1][maxC] + matriz[maxL - 1][maxC - 1];

        } else if (linha == 0) {
            contB += matriz[0][col - 1] + matriz[0][col + 1]
                   + matriz[1][col - 1] + matriz[1][col] + matriz[1][col + 1];

        } else if (linha == maxL) {
            contB += matriz[maxL][col - 1] + matriz[maxL][col + 1]
                   + matriz[maxL - 1][col - 1] + matriz[maxL - 1][col] + matriz[maxL - 1][col + 1];

        } else if (col == 0) {
            contB += matriz[linha - 1][0] + matriz[linha + 1][0]
                   + matriz[linha - 1][1] + matriz[linha][1] + matriz[linha + 1][1];

        } else if (col == maxC) {
            contB += matriz[linha - 1][maxC] + matriz[linha + 1][maxC]
                   + matriz[linha - 1][maxC - 1] + matriz[linha][maxC - 1] + matriz[linha + 1][maxC - 1];

        } else {
            contB += matriz[linha - 1][col + 1] + matriz[linha - 1][col] + matriz[linha - 1][col - 1]
                   + matriz[linha + 1][col + 1] + matriz[linha + 1][col] + matriz[linha + 1][col - 1]
                   + matriz[linha][col + 1]      + matriz[linha][col - 1];
        }

        return contB;
    }

    public static void main(String[] args) throws Exception {
        Scanner leia = new Scanner(System.in);
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        int op;
        do {
            op = menu();
            if (op == 2) {
                System.out.println("\nComo o jogo funciona:");
                System.out.println("1 - Grade de quadradinhos com bombas escondidas em alguns deles");
                System.out.println("2 - Escolha uma posicao para revelar — numero ou bomba");
                System.out.println("3 - O numero mostra quantas bombas ha nos quadrados vizinhos");
                System.out.println("4 - Use a logica dos numeros para deduzir onde as bombas estao");
                System.out.println("5 - Vence quem revelar todos os quadrados sem bomba sem explodir nenhuma");
                System.out.println("\nPressione Enter para voltar ao menu...");
                leia.nextLine();
            }
        } while (op == 2);

        int[][] matriz = getTamanhos();
        int bombas = getBombas(matriz);
        inicioJogo(matriz, bombas);
        mainJogo(matriz, bombas);
    }
}
