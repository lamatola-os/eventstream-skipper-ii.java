package com.eventstream.googlepubsub.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Util {
    public static void writeToFile(String file, Map<Integer, Long> data) throws IOException {
        var out = new FileOutputStream(file);
        out.write(("iteration" + "," + "time taken in millis" + "\n").getBytes());
        data.entrySet().forEach(entry -> {
            try {
                out.write((entry.getKey() + "," + entry.getValue() + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        out.flush();
        out.close();
    }

    public static Properties readProperties(String file) throws IOException {
        Properties prop = new Properties();
        InputStream stream = Util.class.getClassLoader().getResourceAsStream(file);
        prop.load(stream);

        return prop;
    }
}
