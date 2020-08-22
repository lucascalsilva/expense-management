package com.mobnova.expense_mgt.number;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class NumberUtil
{
    public int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min.");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
