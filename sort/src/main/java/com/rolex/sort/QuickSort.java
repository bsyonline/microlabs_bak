/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.sort;

/**
 * @author rolex
 * @since 2019
 */
public class QuickSort {

    public void quickSort(int[] arr, int start, int end) {
        int low = start;
        int high = end;
        int p = arr[low];

        while (low < high) {
            if (p < arr[high]) {
                high--;
                continue;
            }
            if (p > arr[low]) {
                low++;
                continue;
            }
            swap(arr, low, high);
        }
        if (low - 1 > start) {
            quickSort(arr, start, low - 1);
        }
        if (high + 1 < end) {
            quickSort(arr, high + 1, end);
        }
    }

    private void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    public static void main(String[] args) {
//        int[] arr = {3, 2, 5, 7, 1, 23, 28, 99};
        int[] arr = {5, 4, 1, 3, 7, 2, 6};
        QuickSort sort = new QuickSort();
        sort.quickSort(arr, 0, arr.length - 1);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
