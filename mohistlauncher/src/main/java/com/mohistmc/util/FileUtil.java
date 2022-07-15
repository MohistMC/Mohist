/*
 * MohistMC
 * Copyright (C) 2018-2022.
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

package com.mohistmc.util;

import com.mohistmc.MohistMCStart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Shawiiz_z
 * @version 0.1
 * @date 09/05/2022 22:07
 */
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
