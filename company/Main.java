package com.company;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void serialisateInvertedIndex(InvertedIndex bool_search){
        try {
            FileOutputStream fos= new FileOutputStream("collection.out");
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(bool_search);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static InvertedIndex deserialisateInvertedIndex(){
        InvertedIndex bool_search=new InvertedIndex();
        try {
            FileInputStream fis = new FileInputStream("collection.out");
            ObjectInputStream oin = new ObjectInputStream(fis);
            bool_search=(InvertedIndex) oin.readObject();
            return bool_search;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            return bool_search;
        }

    }

    public static void main(String[] args) {
        InvertedIndex bool_search=deserialisateInvertedIndex();
        //bool_search.indexCollection("collection");
        //serialisateInvertedIndex(bool_search);


        Map<String, LinkedList<Integer>> map= bool_search.getIndex();

        //Вывод полученных индексов

        /*for (Map.Entry<String, LinkedList<Integer>> pair : map.entrySet()) {
            System.out.println(pair.getKey() +" " + pair.getValue());
        }*/



        Scanner scanner=new Scanner(System.in);
        String query=scanner.nextLine();
        System.out.println(query + " " + bool_search.executeQuery(query));

        //map.get("caesar").add(32);

    }
}
