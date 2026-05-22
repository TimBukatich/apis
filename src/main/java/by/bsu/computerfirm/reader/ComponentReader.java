package by.bsu.computerfirm.reader;

import by.bsu.computerfirm.exception.ComponentReaderException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ComponentReader {

    private ComponentReader() {
    }

    public static List<String> readAllLines(String filePath) throws ComponentReaderException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ComponentReaderException("File path must not be null or empty");
        }
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new ComponentReaderException("File not found: " + filePath);
        }
        if (!Files.isRegularFile(path)) {
            throw new ComponentReaderException("Path is not a regular file: " + filePath);
        }
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new ComponentReaderException("Failed to read file: " + filePath, e);
        }
    }

    public static List<String> readNonEmptyLines(String filePath) throws ComponentReaderException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new ComponentReaderException("File not accessible: " + filePath);
        }
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            return stream
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ComponentReaderException("Failed to read file: " + filePath, e);
        }
    }
}
