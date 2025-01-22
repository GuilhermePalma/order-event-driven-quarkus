package com.guilhermepalma.order_quarkus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static <T> boolean isEmptyOrNull(Collection<T> data) {
        if (Objects.isNull(data) || data.isEmpty()) {
            return true;
        } else {
            return data.stream().filter(item -> !Objects.isNull(item)).findAny().isEmpty();
        }
    }
    public static <T> boolean isEmptyOrNull(String data) {
        return Objects.isNull(data) || data.isEmpty();
    }
    public static String generateIdentifierId() {
        return String.format("%s|%s", UUID.randomUUID().toString(), LocalDateTime.now().toString());
    }

    public static Set<UUID> convertStringToUUID(Set<String> ids) {
        return Util.isEmptyOrNull(ids) ? Collections.emptySet() : ids.stream().map(UUID::fromString).collect(Collectors.toSet());
    }
    public static UUID convertStringToUUID(String id) {
        return Util.isEmptyOrNull(id) ? null : UUID.fromString(id);
    }
}
