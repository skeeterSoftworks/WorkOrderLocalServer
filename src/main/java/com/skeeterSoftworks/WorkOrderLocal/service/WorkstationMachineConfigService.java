package com.skeeterSoftworks.WorkOrderLocal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class WorkstationMachineConfigService {

    private static final Pattern MACHINE_NAME_PATTERN = Pattern.compile(
            "\"machineName\"\\s*:\\s*\"([^\"]*)\"",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Value("${workstation.machine.config.file:./workstation-machine.json}")
    private String configFilePath;

    public Optional<String> readMachineName() {
        Path path = Path.of(configFilePath);
        if (!Files.isRegularFile(path)) {
            return Optional.empty();
        }
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            Matcher m = MACHINE_NAME_PATTERN.matcher(content);
            if (!m.find()) {
                return Optional.empty();
            }
            String name = m.group(1);
            if (name == null || name.isBlank()) {
                return Optional.empty();
            }
            return Optional.of(name.trim());
        } catch (IOException e) {
            log.error("Failed to read workstation machine config from {}: {}", configFilePath, e.getMessage());
            return Optional.empty();
        }
    }

    public void saveMachineName(String machineName) throws IOException {
        Path path = Path.of(configFilePath);
        Path parent = path.getParent();
        if (parent != null && !Files.isDirectory(parent)) {
            Files.createDirectories(parent);
        }
        String value = machineName == null ? "" : machineName.trim();
        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        String json = "{\n  \"machineName\" : \"" + escaped + "\"\n}";
        Files.writeString(path, json, StandardCharsets.UTF_8);
        log.info("Saved workstation machine name to {}", path.toAbsolutePath());
    }
}
