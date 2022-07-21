package com.mohistmc.util;

import com.mohistmc.MohistMCStart;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FileUtil {

    public static List<String> readFileFromJar(String path) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(MohistMCStart.class.getClassLoader().getResourceAsStream(path))))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line);
            return lines;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
