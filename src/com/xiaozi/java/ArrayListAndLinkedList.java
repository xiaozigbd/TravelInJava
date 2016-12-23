package com.xiaozi.java;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/12/23.
 */
public class ArrayListAndLinkedList {
    public static void main(String[] args){
        testInsert(1);
        testInsert(100);
        testInsert(10000);
        testInsert(1000000);
//        testInsert(10000000);
    }

    private static void testInsert(long times){
//        LinkedList loo = new LinkedList();
        long a = System.currentTimeMillis();
//        for(long i=0 ; i< times; i++){
//            loo.add(i);
//        }
//        System.out.println("总次数：" + times);
//        System.out.println("linkedList增加 =" + (System.currentTimeMillis() - a));
//
//        a = System.currentTimeMillis();
//        for(long j =0; j<loo.size(); j++){
//            loo.remove(0);
//        }
//        System.out.println("linkedList删除 =" + (System.currentTimeMillis() - a));

        ArrayList soo = new ArrayList();
        a = System.currentTimeMillis();
        for(long i=0 ; i< times; i++) {
            soo.add(i);
        }
        System.out.println("ArrayList增加 =" + (System.currentTimeMillis() - a));


        a = System.currentTimeMillis();
        for(long j =0; j<soo.size(); j++){
            soo.remove(0);
        }
        System.out.println("ArrayList删除 =" + (System.currentTimeMillis() - a));
        System.out.println("=====================");
    }
}
