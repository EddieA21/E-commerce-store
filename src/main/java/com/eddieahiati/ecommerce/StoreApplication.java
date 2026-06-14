package com.eddieahiati.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.*;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StoreApplication.class, args);
    }

    public static void groupAnagrams(List<String> words) {
        Map<String, List<String>> map = new HashMap<>();

        for(var word : words) {
            var n = word.toCharArray();
            Arrays.sort(n);
            String sorted = new String(n);
            if(!map.containsKey(sorted)) {
                List<String> newList = new ArrayList<>();
                newList.add(word);
                map.put(sorted, newList);
            }
            else{
                var m = map.get(sorted);
                m.add(word);
            }
        }
        for(var keys : map.entrySet()) {
            var list = keys.getValue();
            System.out.println(list);
        }
    }
}