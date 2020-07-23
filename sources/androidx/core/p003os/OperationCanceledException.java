package androidx.core.p003os;

import androidx.annotation.Nullable;

/* renamed from: androidx.core.os.OperationCanceledException */
public class OperationCanceledException extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(@Nullable String message) {
        super(message != null ? message : "The operation has been canceled.");
    }
}
