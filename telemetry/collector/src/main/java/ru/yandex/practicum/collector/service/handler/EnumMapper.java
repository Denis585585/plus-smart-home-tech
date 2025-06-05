package ru.yandex.practicum.collector.service.handler;

public class EnumMapper {
    public static <T extends Enum<T>, U extends Enum<U>> U map(T sourceEnum, Class<U> targetEnumClass) {
        if (sourceEnum == null) {
            return null;
        }

        for (U targetEnum : targetEnumClass.getEnumConstants()) {
            if (targetEnum.name().equals(sourceEnum.name())) {
                return targetEnum;
            }
        }
        throw new IllegalArgumentException("No mapping found for " + sourceEnum + " in " + targetEnumClass.getSimpleName());
    }
}
