package com.example.krzys.quizapp.repository.vo;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

import static com.example.krzys.quizapp.repository.vo.Status.ERROR;
import static com.example.krzys.quizapp.repository.vo.Status.LOADING;
import static com.example.krzys.quizapp.repository.vo.Status.SUCCESS;

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;
    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Resource))
            return false;
        Resource<?> resource = (Resource<?>) o;
        return status == resource.status &&
                data == null ? resource.data == null : data.equals(resource.data) &&
                message == null ? resource.message == null : message.equals(resource.message);
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(status, data, message);
        } else {
            int result = status.hashCode();
            result = 31 * result + (message != null ? message.hashCode() : 0);
            result = 31 * result + (data != null ? data.hashCode() : 0);
            return result;
        }
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
