/*
 * Mohist - MohistMC
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HasteUtils {
    public static String pasteMohist(String text) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://haste.mohistmc.com/documents").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Hastebin Java Api");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(text);
        wr.flush();
        wr.close();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = rd.readLine();
        rd.close();
        return "https://haste.mohistmc.com/" + response.substring(response.indexOf(":") + 2, response.length() - 2);
    }

    public static String pasteUbuntu(String text) throws IOException {
        return pasteUbuntu("", text);
    }

    public static String pasteUbuntu(String name, String text) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://paste.ubuntu.com/").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Hastebin Java Api");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes("poster=" + name + "&syntax=text&expiration=month&content=" + text);
        wr.flush();
        wr.close();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        for (Object s : rd.lines().toArray()) {
            response.append(s);
        }
        rd.close();
        String result = response.toString();
        //<a class="pturl" href="/p/STqDZF6qQP/plain/">Download as text</a>
        String s = "<a class=\"pturl\" href=\"";
        return "https://paste.ubuntu.com" + result.substring(result.indexOf(s) + s.length(), result.indexOf("plain/\">Download as text</a>"));
    }
}
