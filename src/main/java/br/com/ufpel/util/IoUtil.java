/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author WeslenSchiavon
 */
public class IoUtil {

    public static float getPercentualFalsosPositivos(String file, String tagSpam) {
        float contador = 0;
        int qtd = 0;
        List<String> respostas = IoUtil.getWords(file);
        List<String> predicoes = IoUtil.getWords("predicoes.txt");

        for (int i = 0; i < respostas.size(); i++) {
            if (!respostas.get(i).equals(tagSpam)) {
                qtd++;
                if (predicoes.get(i).equals(tagSpam)) {
                    contador++;
                }
            }
        }

        return contador / respostas.size();
    }

    public static float getPercentualFalsosNegativos(String file, String tagSpam) {
        float contador = 0;
        int qtd = 0;
        List<String> respostas = IoUtil.getWords(file);
        List<String> predicoes = IoUtil.getWords("predicoes.txt");

        for (int i = 0; i < respostas.size(); i++) {
            if (respostas.get(i).equals(tagSpam)) {
                qtd++;
                if (!predicoes.get(i).equals(tagSpam)) {
                    contador++;
                }
            }
        }

        return contador / qtd;
    }

    private static List<String> getWords(String file) {
        try {
            List<String> palavras = new ArrayList<>();
            Scanner sc = new Scanner(new File(file));

            while (sc.hasNext()) {
                String line = sc.nextLine().trim();

                if (!line.isEmpty()) {
                    palavras.add(line);
                }
            }
            return palavras;

        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado!!! " + ex);
        }
        return new ArrayList<>();
    }
}
