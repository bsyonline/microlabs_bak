/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.sort;

/**
 * @author rolex
 * @since 2019
 */
public class BubbleSort {

    public void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {3, 2, 5, 7, 1, 23, 28, 99};
        BubbleSort sort = new BubbleSort();
        sort.bubbleSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
