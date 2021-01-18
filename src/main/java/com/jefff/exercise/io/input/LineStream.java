package com.jefff.exercise.io.input;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
public class LineStream {
    String fileName;
    Stream<String> stringStream;
    AtomicInteger lineNumber;

    public LineStream(String fileName, Stream<String> stringStream) {
        this.fileName = fileName;
        this.stringStream = stringStream;
        lineNumber = new AtomicInteger(0);
    }

    public Stream<Line> getStream() {
        return stringStream.map(this::getLine);
    }

    private Line getLine(String s) {
        return new Line(s, lineNumber.incrementAndGet());
    }

    public static LineStream getLineStream(final String name) throws Exception {
        File file = new File(name);
        try {
            final Stream<String> stringStream = Files.lines(file.toPath());
            return new LineStream(name, stringStream);

        } catch (IOException e) {
            throw new Exception(String.format("Unable to create stream from file: '%s'", name));
        }
    }
}
