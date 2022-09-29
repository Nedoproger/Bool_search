package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.Serializable;

public class InvertedIndex implements Serializable {
    private List<String> documents;
    private Map<String, LinkedList<Integer>> index;
    public InvertedIndex(){
        documents=new LinkedList<String>();
        index=new HashMap<String,LinkedList<Integer>>();
    }
    public void indexDocument(String path){
        File document=new File(path);
        if (!documents.contains(document.getAbsolutePath())){
            documents.add(document.getAbsolutePath());
        }
        try {
            Scanner in_doc = new Scanner(document);
            int index_current_doc=documents.indexOf(document.getAbsolutePath());
            while(in_doc.hasNext()){
                String word=in_doc.next();
                word=word.replaceAll("[^A-Za-zА-Яа-я0-9]","");
                if (word.equals("")){
                    continue;
                }
                word=word.toLowerCase();
                if(!index.containsKey(word)){
                    index.put(word,new LinkedList<Integer>());
                    index.get(word).add(index_current_doc);
                }
                else if (!index.get(word).contains(index_current_doc)){
                        index.get(word).add(index_current_doc);
                }
            }
        }
        catch (FileNotFoundException error){
            System.out.println(error.getMessage());
        }
    }
    public Map<String, LinkedList<Integer>> getIndex(){
        return index;
    }
    public int getIndexSize(){
        return index.size();
    }
    public List<String> getDocuments(){
        return documents;
    }
    public void indexCollection(String path){
        File folder=new File(path);
        for (File i: folder.listFiles()){
            if (i.isDirectory()){
                indexCollection(i.getAbsolutePath());
            }
            indexDocument(i.getAbsolutePath());
        }
    }
    private LinkedList<Integer> getInserection (LinkedList<Integer> list1,LinkedList<Integer> list2 ){
        LinkedList<Integer> result=new LinkedList<>();
        Iterator<Integer> iter1;//итератор для первого списка
        Iterator<Integer> iter2;//итератор для второго списка
        int item1,item2;
        if (list1.isEmpty()||list2.isEmpty()) {
            return result;
        }
        iter1 = list1.listIterator();
        iter2 = list2.listIterator();
        item1=iter1.next();
        item2=iter2.next();
        boolean flag;
        do {
            if (item1==item2){
                result.add(item1);
                flag=iter1.hasNext()&& iter2.hasNext();
                if (flag) {
                    item1 = iter1.next();
                    item2 = iter2.next();
                }
            }
            else if(item1<item2){
                flag=iter1.hasNext();
                if (flag) {
                    item1 = iter1.next();
                }
            }
            else{
                flag=iter2.hasNext();
                if (flag){
                    item2=iter2.next();
                }
            }
        } while (flag);


        return result;
    }

    private LinkedList<Integer> getUnion(LinkedList<Integer> list1, LinkedList<Integer> list2){
        LinkedList<Integer> result=new LinkedList<>();
        Iterator<Integer> iter1;
        Iterator<Integer> iter2;
        //проверка на пустоту списков
        //Если один список пустой, то объединением является второй список
        if (list1.isEmpty()){
            return list2;
        }
        if(list2.isEmpty()){
            return list1;
        }
        iter1 = list1.listIterator();
        iter2 = list2.listIterator();
        int item1=iter1.next();
        int item2=iter2.next();
        boolean flag=true;
        do {
            if (item1==item2){
                result.add(item1);
                flag=iter1.hasNext()&&iter2.hasNext();
                if (flag){
                    item1=iter1.next();
                    item2=iter2.next();
                }
            }
            else if (item1<item2){
                result.add(item1);
                flag= iter1.hasNext();
                if (flag){
                    item1=iter1.next();
                }
                else {
                    result.add(item2);
                }
            }
            else{
                result.add(item2);
                flag=iter2.hasNext();
                if (flag){
                    item2=iter2.next();
                }
                else{
                    result.add(item1);
                }
            }
        }while (flag);

        while(iter1.hasNext()){
            result.add(iter1.next());
        }
        while (iter2.hasNext()){
            result.add(iter2.next());
        }
        return result;
    }

    public LinkedList<Integer> executeQuery(String query){
        String [] words;
        //Флаг для определения: конъюнкция или дизъюнкция
        //По умолчанию конъюнкция =true
        boolean is_conjunction=true;

        if (query.indexOf(" AND ")!=-1) {
            words = query.toLowerCase().split(" and ");
        }
        else if (query.indexOf(" OR ")!=-1){
            words = query.toLowerCase().split(" or ");
            is_conjunction=false;
        }
        else{
            words=new String[1];
            words[0]=query.toLowerCase();
        }
        LinkedList<Integer> result;
        //Если такого слова не существует, то передаем пустой список
        result = index.get(words[0]) == null ? new LinkedList<>() : index.get(words[0]);
        for (int i=1;i<words.length;i++){
            //Если такого слова не существует, то в функции передаем пустой список
            LinkedList<Integer> second_list=index.get(words[i])==null ? new LinkedList<>() : index.get(words[i]);
            if (is_conjunction){
                result = getInserection(result,second_list);
            }
            else{
                result=getUnion(result,second_list);
            }
        }
        return result;
    }
}
