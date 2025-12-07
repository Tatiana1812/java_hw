package ru.otus.cache;

import java.util.Objects;

public record CacheKeyWrapper(Long id) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheKeyWrapper)) return false;
        CacheKeyWrapper that = (CacheKeyWrapper) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}