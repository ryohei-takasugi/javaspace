package jp.co.local.sample.basic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLoop {

    public void run() {
        ArrayAndList list = new ArrayAndList();
        list.run();

        Logger logger = Logger.getLogger(ArrayAndList.class.getName());
        logger.info("test");
        logger.log(Level.SEVERE, "debug");

        try {
            Path path = Path.of("/root/projects/README.md");
            List<String> readAllLines = Files.readAllLines(path, Charset.forName("UTF-8"));
            readAllLines.stream().forEach(line -> System.out.println(line));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Path path = Path.of("/root/projects/README.md");
            try (FileChannel fc = FileChannel.open(path)) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (true) {
                    buffer.clear();
                    if (fc.read(buffer) == -1) {
                        break;
                    }
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        System.out.print((char) buffer.get());
                    }
                }
            }
            System.out.println();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Path path = Path.of("/root/projects");
            Files.list(path).filter(p -> !p.getFileName().toString().startsWith("."))
                    .forEach(p -> System.out.println(p.toAbsolutePath()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
