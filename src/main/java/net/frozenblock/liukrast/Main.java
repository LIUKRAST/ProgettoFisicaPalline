package main.java.net.frozenblock.liukrast;

import java.util.ArrayList;

public class Main implements Colors {
    public static void main(String[] args) {
        int k = 20; // N palline
        int t = 1000; // T tempo
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < k; i++) {
            list.add(i + 1);
        }

        for(int i = 0; i < t; i++) {
            int rndm1 = (int)Math.ceil(Math.random() * k / 2); // Numero random tra 1 e N/2
            int rndm2 = (int)Math.ceil(Math.random() * k / 2) + k/2; // Numero random tra N/2 e N
            int num1 = list.get(rndm1 - 1); // Prende il primo numero da scambiare
            int num2 = list.get(rndm2 - 1); // Prende il secondo numero da scambiare

            list.set(rndm2 - 1, num1); // Setta il primo numero nella posizione del secondo
            list.set(rndm1 -1, num2); // Setta il secondo numero nella posizione del primo
            StringBuilder displayList = new StringBuilder("{");
            for (int j = 0; j < list.size(); j++) {
                int integer = list.get(j);
                String color = ANSI_BLUE;
                if(integer > k/2) {
                    color = ANSI_RED;
                }
                displayList.append(color).append(formatNum(integer, k));
                if(j != list.size() - 1) {
                    displayList.append(", ");
                }
            }

            double left = 0;
            for(int l = 0; l < k/2; l++) {
                int num = list.get(l);
                if(num > k/2) {
                    left++;
                }
            }
            double right = 0;
            for(int l = 0; l < k/2; l++) {
                int num = list.get(l + k/2);
                if(num > k/2) {
                    right++;
                }
            }

            double leftP = left/((float)k/2);
            double rightP = right/((float)k/2);

            String colornum1 = ANSI_BLUE;
            if(num1 > k/2) {
                colornum1 = ANSI_RED;
            }
            String colornum2 = ANSI_BLUE;
            if(num2 > k/2) {
                colornum2 = ANSI_RED;
            }

            displayList.append(ANSI_RESET + "}");

            System.out.println("[LOG]: " + "[" + ANSI_CYAN + "Tempo:" + formatNum(i, t) + ANSI_RESET + "], [Scambio: " + colornum1 + formatNum(num1, k) + ANSI_RESET + " con " + colornum2 + formatNum(num2, k) + ANSI_RESET + "], [Risultato: " + displayList + "]; Percentuale Calore: [ Sinistra: " + leftP*100 + "%; Destra: " + rightP*100 + "%]");
        }
    }

    public static String formatNum(int k, int max) {
        int length = String.valueOf(max).length(); // Prende il numero di cifre di un numero, in poche parole lo trasforma in un testo per poi vedere quanti caratteri ha
        int outLenght = String.valueOf(k).length();
        if(length > outLenght) {
            return "0".repeat(length - outLenght) + k;
        }
        return String.valueOf(k);
    }
}
