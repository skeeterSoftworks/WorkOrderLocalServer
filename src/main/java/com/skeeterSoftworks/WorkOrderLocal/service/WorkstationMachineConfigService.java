package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.WorkstationMachineConfigTO;
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

    private static final Pattern MACHINE_ID_PATTERN = Pattern.compile(
            "\"machineId\"\\s*:\\s*(\\d+)",
            Pattern.CASE_INSENSITIVE
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

    public Optional<Long> readMachineId() {
        Path path = Path.of(configFilePath);
        if (!Files.isRegularFile(path)) {
            return Optional.empty();
        }
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            Matcher m = MACHINE_ID_PATTERN.matcher(content);
            if (!m.find()) {
                return Optional.empty();
            }
            return Optional.of(Long.parseLong(m.group(1)));
        } catch (IOException | NumberFormatException e) {
            log.error("Failed to read machineId from {}: {}", configFilePath, e.getMessage());
            return Optional.empty();
        }
    }

    public void save(WorkstationMachineConfigTO to) throws IOException {
        Path path = Path.of(configFilePath);
        Path parent = path.getParent();
        if (parent != null && !Files.isDirectory(parent)) {
            Files.createDirectories(parent);
        }
        String value = to.getMachineName() == null ? "" : to.getMachineName().trim();
        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"machineName\" : \"").append(escaped).append("\"");
        if (to.getMachineId() != null && to.getMachineId() > 0) {
            json.append(",\n  \"machineId\" : ").append(to.getMachineId());
        }
        json.append("\n}");
        Files.writeString(path, json.toString(), StandardCharsets.UTF_8);
        log.info("Saved workstation machine config to {}", path.toAbsolutePath());
    }

    /** @deprecated use {@link #save(WorkstationMachineConfigTO)} */
    @Deprecated
    public void saveMachineName(String machineName) throws IOException {
        WorkstationMachineConfigTO to = new WorkstationMachineConfigTO();
        to.setMachineName(machineName);
        save(to);
    }
}
