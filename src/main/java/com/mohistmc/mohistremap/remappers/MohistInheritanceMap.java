/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.mohistremap.remappers;

import com.google.common.base.Joiner;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.specialsource.InheritanceMap;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.InheritanceProvider;

/**
 *
 * @author pyz
 * @date 2019/7/4 9:44 AM
 */
public class MohistInheritanceMap extends InheritanceMap {
    protected final List<String> emptyList = new ImmutableList.Builder().build();
    protected final Map<String, List<String>> _inheritanceMap = new HashMap<>();

    /**
     * Generate an inheritance map for the given classes
     */
    @Override
    public void generate(InheritanceProvider inheritanceProvider, Collection<String> classes) {
        for (String className : classes) {
            Collection<String> parents = inheritanceProvider.getParents(className);

            if (parents == null) {
                System.out.println("No inheritance information found for " + className);
            } else {
                ArrayList<String> filteredParents = new ArrayList<>();

                // Include only classes requested
                for (String parent : parents) {
                    if (classes.contains(parent)) {
                        filteredParents.add(parent);
                    }
                }

                // If there are parents we care about, add to map
                if (!filteredParents.isEmpty()) {
                    setParents(className, filteredParents);
                }
            }
        }
    }

    @Override
    public void save(PrintWriter writer) {
        List<String> classes = new ArrayList<>(_inheritanceMap.keySet());
        Collections.sort(classes);

        for (String className : classes) {
            writer.print(className);
            writer.print(' ');

            List<String> parents = getParents(className);
            writer.println(Joiner.on(' ').join(parents));
        }
    }

    @Override
    public void load(BufferedReader reader, BiMap<String, String> classMap) throws IOException {
        String line;

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");

            if (tokens.length < 2) {
                throw new IOException("Invalid inheritance map file line: " + line);
            }

            String className = tokens[0];
            List<String> parents = Arrays.asList(tokens).subList(1, tokens.length);

            if (classMap == null) {
                setParents(className, new ArrayList<>(parents));
            } else {
                String remappedClassName = JarRemapper.mapTypeName(className, /*packageMap*/ null, classMap, /*defaultIfUnmapped*/ null);
                if (remappedClassName == null) {
                    throw new IOException("Inheritance map input class not remapped: " + className);
                }

                ArrayList<String> remappedParents = new ArrayList<>();
                for (String parent : parents) {
                    String remappedParent = JarRemapper.mapTypeName(parent, /*packageMap*/ null, classMap, /*defaultIfUnmapped*/ null);
                    if (remappedParent == null) {
                        throw new IOException("Inheritance map parent class not remapped: " + parent);
                    }

                    remappedParents.add(remappedParent);
                }

                setParents(remappedClassName, remappedParents);
            }
        }
    }

    @Override
    public boolean hasParents(String className) {
        return _inheritanceMap.containsKey(className);
    }

    @Override
    public List<String> getParents(String className) {
        return _inheritanceMap.get(className);
    }

    @Override
    public void setParents(String className, Collection<String> parents) {
        if (parents == null || parents.isEmpty()) {
            _inheritanceMap.put(className, emptyList);
        } else {
            _inheritanceMap.put(className, new ImmutableList.Builder().addAll(parents).build());
        }
    }

    @Override
    public int size() {
        return _inheritanceMap.size();
    }
}
