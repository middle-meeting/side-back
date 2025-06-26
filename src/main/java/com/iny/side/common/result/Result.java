package com.iny.side.common.result;

import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<T> permits Result.Success, Result.Failure {
    
    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(String message, Throwable cause) implements Result<T> {}
    
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }
    
    static <T> Result<T> failure(String message) {
        return new Failure<>(message, null);
    }
    
    static <T> Result<T> failure(String message, Throwable cause) {
        return new Failure<>(message, cause);
    }
    
    default boolean isSuccess() {
        return this instanceof Success<T>;
    }
    
    default boolean isFailure() {
        return this instanceof Failure<T>;
    }
    
    default T getValue() {
        return switch (this) {
            case Success<T> success -> success.value();
            case Failure<T> failure -> throw new IllegalStateException("Cannot get value from failure: " + failure.message());
        };
    }
    
    default String getErrorMessage() {
        return switch (this) {
            case Success<T> success -> throw new IllegalStateException("Cannot get error from success");
            case Failure<T> failure -> failure.message();
        };
    }
    
    default Result<T> onSuccess(Consumer<T> action) {
        if (this instanceof Success<T> success) {
            action.accept(success.value());
        }
        return this;
    }
    
    default Result<T> onFailure(Consumer<String> action) {
        if (this instanceof Failure<T> failure) {
            action.accept(failure.message());
        }
        return this;
    }
    
    default <U> Result<U> map(Function<T, U> mapper) {
        return switch (this) {
            case Success<T> success -> Result.success(mapper.apply(success.value()));
            case Failure<T> failure -> Result.failure(failure.message(), failure.cause());
        };
    }
}
