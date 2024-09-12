package edu.escuelaing.arem.ASE.app;

import java.util.ArrayList;
import java.math.*;

public class BoubleSort {


    public static void BoubleSort1(ArrayList<Double> array) {

        Boolean isOrdened = Boolean.FALSE;
        int max = array.size();

        for (int i = 0; i < max-1 ; i++) {
            System.out.println("Pre" + array);
            System.out.println(i);
            System.out.println(i+1);
            if (array.get(i) > array.get(i + 1)) {
                Double number = array.get(i + 1);
                array.set(i + 1, array.get(i));
                array.set(i, number);
                i=0;
            }

        }
    }

    public static void main(){
        ArrayList<Double> arrayList = new ArrayList<Double>(9);
        arrayList.add(4.5);
        arrayList.add(8.8);
        arrayList.add(1.0);
        arrayList.add(6.4);
        arrayList.add(0.1);
        arrayList.add(0.6);
        BoubleSort1(arrayList);
    }
}

