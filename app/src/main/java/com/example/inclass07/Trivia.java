package com.example.inclass07;

import java.io.Serializable;
import java.util.Arrays;

public class Trivia implements Serializable  {
    String id;
    String text;
    String image;
    String[] choice;
    String answer;

    public Trivia() {
    }

    @Override
    public String toString() {
        return "Trivia{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", choice=" + Arrays.toString(choice) +
                ", answer='" + answer + '\'' +
                '}';
    }
}
