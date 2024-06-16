package jp.co.local.sample.loop;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AsyncFileReader {

    private final Path filePath;

    public AsyncFileReader(Path filePath) {
        this.filePath = filePath;
    }

    public CompletableFuture<JsonElement> read() {
        final CompletableFuture<JsonElement> future = new CompletableFuture<>();
        AsynchronousFileChannel fileChannel = null;

        try {
            fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.READ);
            final AsynchronousFileChannel finalFileChannel = fileChannel; // for use in lambda

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer size, ByteBuffer attachment) {
                    try {
                        attachment.flip();
                        byte[] data = new byte[size];
                        attachment.get(data);

                        JsonElement result = JsonParser.parseString(new String(data));
                        future.complete(result);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    } finally {
                        try {
                            finalFileChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failed(Throwable th, ByteBuffer attachment) {
                    try {
                        future.completeExceptionally(th);
                    } finally {
                        try {
                            finalFileChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (Throwable th) {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            future.completeExceptionally(th);
        }

        return future;
    }
}
