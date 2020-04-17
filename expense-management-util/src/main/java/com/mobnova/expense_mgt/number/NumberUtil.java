package com.mobnova.expense_mgt.number;

import java.util.Random;

public class NumberUtil
{
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min.");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
