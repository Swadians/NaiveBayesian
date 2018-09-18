/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.naivebayesian;

/**
 *
 * @author WeslenSchiavon
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Bayes {

    private Map<String, Word> words;
    private BufferedWriter out;

    public Bayes() {
        this.words = Collections.synchronizedMap(new HashMap<>());
    }

    public void treinar(String file, String rotuloSpam) throws IOException {
        AtomicInteger totalSpamCount = new AtomicInteger();
        AtomicInteger totalHamCount = new AtomicInteger();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = in.readLine();
            while (line != null) {
                if (!line.isEmpty()) {
                    String rotulo = line.split("\t")[0];
                    String mensagem = line.split("\t")[1];

                    Stream.of(mensagem.split(" ")).parallel().forEach(word -> {
                        word = word.replaceAll("\\W", "")
                                .toLowerCase();
                        Word w = null;
                        if (words.containsKey(word)) {
                            w = words.get(word);
                        } else {
                            w = new Word(word);
                            words.put(word, w);
                        }
                        if (!rotulo.equals(rotuloSpam)) {
                            w.countNotSpam();
                            totalHamCount.incrementAndGet();
                        } else if (rotulo.equals(rotuloSpam)) {
                            w.countSpam();
                            totalSpamCount.incrementAndGet();
                        }
                    });
                }
                line = in.readLine();
            }
        }

        words.keySet().parallelStream().forEach(key -> {
            words.get(key).calculateProbability(totalSpamCount.get(), totalHamCount.get());
        });

    }

    public void predizer(String file, float limiarSpam) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            this.out = new BufferedWriter(new FileWriter("predicoes.txt"));
            String line = in.readLine();
            while (line != null) {
                if (!line.isEmpty()) {
                    List<Word> mensagens = makeWordList(line);
                    boolean isSpam = calcularBayes(mensagens, limiarSpam);
                    if (isSpam == true) {
                        this.out.write("spam");
                    } else if (isSpam == false) {
                        this.out.write("NotSpam");
                    }
                }
                this.out.newLine();
                line = in.readLine();
            }
            this.out.close();
        }
    }

    public boolean calcularBayes(List<Word> mensagem, float limiarSpam) {
        float probabilityOfPositiveProduct = 1.0f;
        float probabilityOfNegativeProduct = 1.0f;
        for (int i = 0; i < mensagem.size(); i++) {
            Word word = mensagem.get(i);
            probabilityOfPositiveProduct *= word.getProbOfSpam();
            probabilityOfNegativeProduct *= (1.0f - word.getProbOfSpam());
        }
        float probOfSpam = probabilityOfPositiveProduct / (probabilityOfPositiveProduct + probabilityOfNegativeProduct);
        if (probOfSpam > limiarSpam) {
            return true;
        }
        return false;

    }

    private List<Word> makeWordList(String mensagem) {
        List<Word> wordList = Collections.synchronizedList(new ArrayList<>());

        Stream.of(mensagem.split(" ")).parallel().forEach(novaPalavra -> {
            novaPalavra = novaPalavra.replaceAll("\\W", "")
                    .toLowerCase();
            Word palavra = null;
            if (words.containsKey(novaPalavra)) {
                palavra = words.get(novaPalavra);
            } else {
                palavra = new Word(novaPalavra);
                palavra.setProbOfSpam(0.40f);
            }
            wordList.add(palavra);
        });
        return wordList;
    }

}
