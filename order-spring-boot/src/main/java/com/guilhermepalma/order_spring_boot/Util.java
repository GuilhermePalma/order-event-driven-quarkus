package com.guilhermepalma.order_spring_boot;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class Util {

    public static <T> boolean isEmptyOrNull(Collection<T> data) {
        return Objects.isNull(data) || data.isEmpty();
    }
    public static String generateIdentifierId() {
        return String.format("%s|%s", UUID.randomUUID().toString(), LocalDateTime.now().toString());
    }

}
