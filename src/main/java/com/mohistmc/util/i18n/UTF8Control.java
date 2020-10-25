package com.mohistmc.util.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * DESC:
 * CleanUser com.feshfans.spring
 * feshfans 2018/5/11 18:46
 * email: feshfans@163.com
 **/
public class UTF8Control extends ResourceBundle.Control {
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        String bundleName = toBundleName(baseName, locale);
        ResourceBundle bundle = null;
        if (format.equals("java.class")) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends ResourceBundle> bundleClass
                        = (Class<? extends ResourceBundle>) loader.loadClass(bundleName);

                // If the class isn't a ResourceBundle subclass, throw a
                // ClassCastException.
                if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
                    bundle = bundleClass.getConstructor().newInstance();
                } else {
                    throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException ignored) {
            }
        } else if (format.equals("java.properties")) {
            final String resourceName = toResourceName0(bundleName);
            if (resourceName == null) {
                return null;
            }
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream;
            try {
                stream = AccessController.doPrivileged(
                        (PrivilegedExceptionAction<InputStream>) () -> {
                            InputStream is = null;
                            if (reloadFlag) {
                                URL url = classLoader.getResource(resourceName);
                                if (url != null) {
                                    URLConnection connection = url.openConnection();
                                    if (connection != null) {
                                        // Disable caches to get fresh data for
                                        // reloading.
                                        connection.setUseCaches(false);
                                        is = connection.getInputStream();
                                    }
                                }
                            } else {
                                is = classLoader.getResourceAsStream(resourceName);
                            }
                            return is;
                        });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
        } else {
            throw new IllegalArgumentException("unknown format: " + format);
        }
        return bundle;
    }

    private String toResourceName0(String bundleName) {
        // application protocol check
        if (bundleName.contains("://")) {
            return null;
        } else {
            return toResourceName(bundleName, "properties");
        }
    }

}
