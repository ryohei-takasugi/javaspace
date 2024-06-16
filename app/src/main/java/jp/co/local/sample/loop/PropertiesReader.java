package jp.co.local.sample.loop;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesReader {

    private final Path filePath;

    public PropertiesReader() {
        this.filePath = Path.of("./sample.properties");
    }

    public PropertiesReader(Path filePath) {
        this.filePath = filePath;
    }

    public Properties read() {
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(filePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println(String.format("ファイルの読み込みに失敗しました。:%s", e));
            e.printStackTrace();
            return null;
        }
        return properties;
    }
}
