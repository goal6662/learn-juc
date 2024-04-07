package com.goal.t5.map;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ConcurrentHashMap
 */
@Slf4j(topic = "Map")
public class Demo01 {

    static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws IOException {
        int length = ALPHA.length();
        int count = 200;

        List<String> list = new ArrayList<>(length * count);
        for (int i = 0; i < length; i++) {
            char c = ALPHA.charAt(i);
            for (int j = 0; j < count; j++) {
                list.add(String.valueOf(c));
            }
        }

        Collections.shuffle(list);

        for (int i = 0; i < 26; i++) {
            try (PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            Files.newOutputStream(Paths.get("temp/" + (i + 1) + ".txt"))))) {
                String collect = String.join("\n", list.subList(i * count, (i + 1) * count));
                out.println(collect);
            } catch (Exception ignored) {

            }
        }

    }

}
