package com.example.BootStrapper.Shell;

import java.io.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class Shell{
    private static Shell instance;
    private Shell() {
    }

    public String runShellCommand(String command) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", command);
        } else {
            builder.command("sh", "-c", command);
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StringConsumer stringConsumer = new StringConsumer();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), stringConsumer);
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        future.get(10, TimeUnit.SECONDS);
        return stringConsumer.getString();
    }
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;
        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }

    }
    public static Shell getInstance() {
        if (instance == null) {
            instance = new Shell();
        }
        return instance;
    }
}