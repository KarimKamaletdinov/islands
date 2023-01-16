package com.agrogames.islandsofwar.common;

public class Arr {
    public static int indexOf(int[] array, int item){
        for (int i = 0, arrayLength = array.length; i < arrayLength; i++) {
            int at = array[i];
            if (at == item) return i;
        }
        return  -1;
    }
}
