package com.guilhermepalma.order_spring_boot;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class Util {

    public static <T> boolean isEmptyOrNull(Collection<T> data) {
        return Objects.isNull(data) || data.isEmpty();
    }
}
