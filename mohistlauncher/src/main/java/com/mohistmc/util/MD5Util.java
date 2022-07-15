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

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class MD5Util {

    public static String getMd5(File path) {
        try {
            return String.format("%032x", new BigInteger(1, MessageDigest.getInstance("MD5").digest(Files.readAllBytes(path.toPath())))).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMd5(InputStream is) {
        try {
            return String.format("%032x", new BigInteger(1, new DigestInputStream(is, MessageDigest.getInstance("MD5")).getMessageDigest().digest())).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }
}
