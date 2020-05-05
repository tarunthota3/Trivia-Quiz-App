package com.example.inclass07;

public class ScoreData {
    String questionId;
    boolean score;

    public ScoreData() {
    }

    @Override
    public String toString() {
        return "ScoreData{" +
                "questionId='" + questionId + '\'' +
                ", score=" + score +
                '}';
    }
}
