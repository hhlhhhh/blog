package com.blog.back.utils;

import java.util.Random;

public class RandomGenerate {

    public static String randomNString(int n){
        Random random = new Random();
        int firstDigit = random.nextInt(n-1) + 1;

        StringBuilder restDigits = new StringBuilder();
        for (int i = 1; i < n; i++) {
            restDigits.append(random.nextInt(10));
        }
        return firstDigit + restDigits.toString();
    }

}
