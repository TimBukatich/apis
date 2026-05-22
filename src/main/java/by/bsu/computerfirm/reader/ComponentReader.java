package by.bsu.computerfirm.reader;

import by.bsu.computerfirm.exception.ComponentReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ComponentReader {

    private static final Logger LOGGER = LogManager.getLogger(ComponentReader.class);

    private ComponentReader() {
    }

    public static List<String> readAllLines(String filePath) throws ComponentReaderException {
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.error("File path must not be null or empty");
            throw new ComponentReaderException("File path must not be null or empty");
        }
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            LOGGER.error("File not found: {}", filePath);
            throw new ComponentReaderException("File not found: " + filePath);
        }
        if (!Files.isRegularFile(path)) {
            LOGGER.error("Path is not a regular file: {}", filePath);
            throw new ComponentReaderException("Path is not a regular file: " + filePath);
        }
        LOGGER.debug("Reading all lines from file: {}", filePath);
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            List<String> result = stream.collect(Collectors.toList());
            LOGGER.info("Loaded {} raw lines from {}", result.size(), filePath);
            return result;
        } catch (IOException e) {
            LOGGER.error("Failed to read file: {}", filePath, e);
            throw new ComponentReaderException("Failed to read file: " + filePath, e);
        }
    }

    public static List<String> readNonEmptyLines(String filePath) throws ComponentReaderException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            LOGGER.error("File not accessible: {}", filePath);
            throw new ComponentReaderException("File not accessible: " + filePath);
        }
        LOGGER.debug("Reading non-empty lines from file: {}", filePath);
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            List<String> result = stream
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.toList());
            LOGGER.info("Loaded {} non-empty lines from {}", result.size(), filePath);
            return result;
        } catch (IOException e) {
            LOGGER.error("Failed to read file: {}", filePath, e);
            throw new ComponentReaderException("Failed to read file: " + filePath, e);
        }
    }
}
