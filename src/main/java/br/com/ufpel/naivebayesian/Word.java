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
public class Word {

    private String word;
    private int spamCount;
    private int notSpamCount;
    private float spamRate;
    private float notSpamRate;
    private float probOfSpam;

    public Word(String word) {
        this.word = word;
    }

    public void countSpam() {
        spamCount++;
    }

    public void countNotSpam() {
        notSpamCount++;
    }

    public void calculateProbability(int totSpam, int totNotSpam) {
        spamRate = spamCount / (float) totSpam;
        notSpamRate = notSpamCount / (float) totNotSpam;

        if (spamRate + notSpamRate > 0) {
            probOfSpam = spamRate / (spamRate + notSpamRate);
        }
        if (probOfSpam < 0.01f) {
            probOfSpam = 0.01f;
        } else if (probOfSpam > 0.99f) {
            probOfSpam = 0.99f;
        }
    }

    public String getWord() {
        return word;
    }

    public float getSpamRate() {
        return spamRate;
    }

    public float getNotSpamRate() {
        return notSpamRate;
    }

    public void setNotSpamRate(float notSpamRate) {
        this.notSpamRate = notSpamRate;
    }

    public float getProbOfSpam() {
        return probOfSpam;
    }

    public void setProbOfSpam(float probOfSpam) {
        this.probOfSpam = probOfSpam;
    }
}
