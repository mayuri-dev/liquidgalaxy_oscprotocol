package androidx.core.view;

import android.app.Activity;
import android.os.Build.VERSION;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

public final class DragAndDropPermissionsCompat {
    private Object mDragAndDropPermissions;

    private DragAndDropPermissionsCompat(Object dragAndDropPermissions) {
        this.mDragAndDropPermissions = dragAndDropPermissions;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static DragAndDropPermissionsCompat request(Activity activity, DragEvent dragEvent) {
        if (VERSION.SDK_INT >= 24) {
            DragAndDropPermissions dragAndDropPermissions = activity.requestDragAndDropPermissions(dragEvent);
            if (dragAndDropPermissions != null) {
                return new DragAndDropPermissionsCompat(dragAndDropPermissions);
            }
        }
        return null;
    }

    public void release() {
        if (VERSION.SDK_INT >= 24) {
            ((DragAndDropPermissions) this.mDragAndDropPermissions).release();
        }
    }
}
