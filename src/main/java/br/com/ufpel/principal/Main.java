/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.principal;

import br.com.ufpel.naivebayesian.Bayes;
import br.com.ufpel.util.IoUtil;
import java.io.IOException;

/**
 *
 * @author WeslenSchiavon
 */
public class Main {

    public static void main(String[] args) {
        Bayes run = new Bayes();
        try {
            run.treinar(args[0], "spam");
            run.predizer(args[1], 0.075f);

            System.out.println("Falsos negativos: " + IoUtil.getPercentualFalsosNegativos("RespostasTeste.txt", "spam") * 100 + "%");
            System.out.println("Falsos positivos: " + IoUtil.getPercentualFalsosPositivos("RespostasTeste.txt", "spam") * 100 + "%");

        } catch (IOException ex) {
            System.out.println("Erro ao ler o arquivo!!" + ex);
        }
    }
}
