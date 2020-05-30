package androidx.core.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.DragShadowBuilder;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnUnhandledKeyEventListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.annotation.C0007Px;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.UiThread;
import androidx.collection.ArrayMap;
import androidx.core.C0113R;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewCompat {
    private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS = {C0113R.C0115id.accessibility_custom_action_0, C0113R.C0115id.accessibility_custom_action_1, C0113R.C0115id.accessibility_custom_action_2, C0113R.C0115id.accessibility_custom_action_3, C0113R.C0115id.accessibility_custom_action_4, C0113R.C0115id.accessibility_custom_action_5, C0113R.C0115id.accessibility_custom_action_6, C0113R.C0115id.accessibility_custom_action_7, C0113R.C0115id.accessibility_custom_action_8, C0113R.C0115id.accessibility_custom_action_9, C0113R.C0115id.accessibility_custom_action_10, C0113R.C0115id.accessibility_custom_action_11, C0113R.C0115id.accessibility_custom_action_12, C0113R.C0115id.accessibility_custom_action_13, C0113R.C0115id.accessibility_custom_action_14, C0113R.C0115id.accessibility_custom_action_15, C0113R.C0115id.accessibility_custom_action_16, C0113R.C0115id.accessibility_custom_action_17, C0113R.C0115id.accessibility_custom_action_18, C0113R.C0115id.accessibility_custom_action_19, C0113R.C0115id.accessibility_custom_action_20, C0113R.C0115id.accessibility_custom_action_21, C0113R.C0115id.accessibility_custom_action_22, C0113R.C0115id.accessibility_custom_action_23, C0113R.C0115id.accessibility_custom_action_24, C0113R.C0115id.accessibility_custom_action_25, C0113R.C0115id.accessibility_custom_action_26, C0113R.C0115id.accessibility_custom_action_27, C0113R.C0115id.accessibility_custom_action_28, C0113R.C0115id.accessibility_custom_action_29, C0113R.C0115id.accessibility_custom_action_30, C0113R.C0115id.accessibility_custom_action_31};
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    @Deprecated
    public static final int LAYER_TYPE_HARDWARE = 2;
    @Deprecated
    public static final int LAYER_TYPE_NONE = 0;
    @Deprecated
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    @Deprecated
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    @Deprecated
    public static final int MEASURED_SIZE_MASK = 16777215;
    @Deprecated
    public static final int MEASURED_STATE_MASK = -16777216;
    @Deprecated
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;
    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    @Deprecated
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";
    public static final int TYPE_NON_TOUCH = 1;
    public static final int TYPE_TOUCH = 0;
    private static boolean sAccessibilityDelegateCheckFailed = false;
    private static Field sAccessibilityDelegateField;
    private static AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();
    private static Method sChildrenDrawingOrderMethod;
    private static Method sDispatchFinishTemporaryDetach;
    private static Method sDispatchStartTemporaryDetach;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static boolean sTempDetachBound;
    private static ThreadLocal<Rect> sThreadLocalRect;
    private static WeakHashMap<View, String> sTransitionNameMap;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap = null;

    static class AccessibilityPaneVisibilityManager implements OnGlobalLayoutListener, OnAttachStateChangeListener {
        private WeakHashMap<View, Boolean> mPanesToVisible = new WeakHashMap<>();

        AccessibilityPaneVisibilityManager() {
        }

        @RequiresApi(19)
        public void onGlobalLayout() {
            for (Entry<View, Boolean> entry : this.mPanesToVisible.entrySet()) {
                checkPaneVisibility((View) entry.getKey(), ((Boolean) entry.getValue()).booleanValue());
            }
        }

        @RequiresApi(19)
        public void onViewAttachedToWindow(View view) {
            registerForLayoutCallback(view);
        }

        public void onViewDetachedFromWindow(View view) {
        }

        /* access modifiers changed from: 0000 */
        @RequiresApi(19)
        public void addAccessibilityPane(View pane) {
            this.mPanesToVisible.put(pane, Boolean.valueOf(pane.getVisibility() == 0));
            pane.addOnAttachStateChangeListener(this);
            if (pane.isAttachedToWindow()) {
                registerForLayoutCallback(pane);
            }
        }

        /* access modifiers changed from: 0000 */
        @RequiresApi(19)
        public void removeAccessibilityPane(View pane) {
            this.mPanesToVisible.remove(pane);
            pane.removeOnAttachStateChangeListener(this);
            unregisterForLayoutCallback(pane);
        }

        @RequiresApi(19)
        private void checkPaneVisibility(View pane, boolean oldVisibility) {
            boolean newVisibility = pane.getVisibility() == 0;
            if (oldVisibility != newVisibility) {
                if (newVisibility) {
                    ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(pane, 16);
                }
                this.mPanesToVisible.put(pane, Boolean.valueOf(newVisibility));
            }
        }

        @RequiresApi(19)
        private void registerForLayoutCallback(View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        @RequiresApi(19)
        private void unregisterForLayoutCallback(View view) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    static abstract class AccessibilityViewProperty<T> {
        private final int mContentChangeType;
        private final int mFrameworkMinimumSdk;
        private final int mTagKey;
        private final Class<T> mType;

        /* access modifiers changed from: 0000 */
        public abstract T frameworkGet(View view);

        /* access modifiers changed from: 0000 */
        public abstract void frameworkSet(View view, T t);

        AccessibilityViewProperty(int tagKey, Class<T> type, int frameworkMinimumSdk) {
            this(tagKey, type, 0, frameworkMinimumSdk);
        }

        AccessibilityViewProperty(int tagKey, Class<T> type, int contentChangeType, int frameworkMinimumSdk) {
            this.mTagKey = tagKey;
            this.mType = type;
            this.mContentChangeType = contentChangeType;
            this.mFrameworkMinimumSdk = frameworkMinimumSdk;
        }

        /* access modifiers changed from: 0000 */
        public void set(View view, T value) {
            if (frameworkAvailable()) {
                frameworkSet(view, value);
            } else if (extrasAvailable() && shouldUpdate(get(view), value)) {
                ViewCompat.getOrCreateAccessibilityDelegateCompat(view);
                view.setTag(this.mTagKey, value);
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public T get(View view) {
            if (frameworkAvailable()) {
                return frameworkGet(view);
            }
            if (extrasAvailable()) {
                Object value = view.getTag(this.mTagKey);
                if (this.mType.isInstance(value)) {
                    return value;
                }
            }
            return null;
        }

        private boolean frameworkAvailable() {
            return VERSION.SDK_INT >= this.mFrameworkMinimumSdk;
        }

        private boolean extrasAvailable() {
            return VERSION.SDK_INT >= 19;
        }

        /* access modifiers changed from: 0000 */
        public boolean shouldUpdate(T oldValue, T newValue) {
            return !newValue.equals(oldValue);
        }

        /* access modifiers changed from: 0000 */
        public boolean booleanNullToFalseEquals(Boolean a, Boolean b) {
            if ((a == null ? false : a.booleanValue()) == (b == null ? false : b.booleanValue())) {
                return true;
            }
            return false;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusDirection {
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRealDirection {
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRelativeDirection {
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NestedScrollType {
    }

    public interface OnUnhandledKeyEventListenerCompat {
        boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollAxis {
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }

    static class UnhandledKeyEventManager {
        private static final ArrayList<WeakReference<View>> sViewsWithListeners = new ArrayList<>();
        private SparseArray<WeakReference<View>> mCapturedKeys = null;
        private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent = null;
        @Nullable
        private WeakHashMap<View, Boolean> mViewsContainingListeners = null;

        UnhandledKeyEventManager() {
        }

        private SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys == null) {
                this.mCapturedKeys = new SparseArray<>();
            }
            return this.mCapturedKeys;
        }

        /* renamed from: at */
        static UnhandledKeyEventManager m4at(View root) {
            UnhandledKeyEventManager manager = (UnhandledKeyEventManager) root.getTag(C0113R.C0115id.tag_unhandled_key_event_manager);
            if (manager != null) {
                return manager;
            }
            UnhandledKeyEventManager manager2 = new UnhandledKeyEventManager();
            root.setTag(C0113R.C0115id.tag_unhandled_key_event_manager, manager2);
            return manager2;
        }

        /* access modifiers changed from: 0000 */
        public boolean dispatch(View root, KeyEvent event) {
            if (event.getAction() == 0) {
                recalcViewsWithUnhandled();
            }
            View consumer = dispatchInOrder(root, event);
            if (event.getAction() == 0) {
                int keycode = event.getKeyCode();
                if (consumer != null && !KeyEvent.isModifierKey(keycode)) {
                    getCapturedKeys().put(keycode, new WeakReference(consumer));
                }
            }
            return consumer != null;
        }

        @Nullable
        private View dispatchInOrder(View view, KeyEvent event) {
            WeakHashMap<View, Boolean> weakHashMap = this.mViewsContainingListeners;
            if (weakHashMap == null || !weakHashMap.containsKey(view)) {
                return null;
            }
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                    View consumer = dispatchInOrder(vg.getChildAt(i), event);
                    if (consumer != null) {
                        return consumer;
                    }
                }
            }
            if (onUnhandledKeyEvent(view, event)) {
                return view;
            }
            return null;
        }

        /* access modifiers changed from: 0000 */
        public boolean preDispatch(KeyEvent event) {
            WeakReference<KeyEvent> weakReference = this.mLastDispatchedPreViewKeyEvent;
            if (weakReference != null && weakReference.get() == event) {
                return false;
            }
            this.mLastDispatchedPreViewKeyEvent = new WeakReference<>(event);
            WeakReference<View> currentReceiver = null;
            SparseArray<WeakReference<View>> capturedKeys = getCapturedKeys();
            if (event.getAction() == 1) {
                int idx = capturedKeys.indexOfKey(event.getKeyCode());
                if (idx >= 0) {
                    currentReceiver = (WeakReference) capturedKeys.valueAt(idx);
                    capturedKeys.removeAt(idx);
                }
            }
            if (currentReceiver == null) {
                currentReceiver = (WeakReference) capturedKeys.get(event.getKeyCode());
            }
            if (currentReceiver == null) {
                return false;
            }
            View target = (View) currentReceiver.get();
            if (target != null && ViewCompat.isAttachedToWindow(target)) {
                onUnhandledKeyEvent(target, event);
            }
            return true;
        }

        private boolean onUnhandledKeyEvent(@NonNull View v, @NonNull KeyEvent event) {
            ArrayList<OnUnhandledKeyEventListenerCompat> viewListeners = (ArrayList) v.getTag(C0113R.C0115id.tag_unhandled_key_listeners);
            if (viewListeners != null) {
                for (int i = viewListeners.size() - 1; i >= 0; i--) {
                    if (((OnUnhandledKeyEventListenerCompat) viewListeners.get(i)).onUnhandledKeyEvent(v, event)) {
                        return true;
                    }
                }
            }
            return false;
        }

        static void registerListeningView(View v) {
            synchronized (sViewsWithListeners) {
                Iterator it = sViewsWithListeners.iterator();
                while (it.hasNext()) {
                    if (((WeakReference) it.next()).get() == v) {
                        return;
                    }
                }
                sViewsWithListeners.add(new WeakReference(v));
            }
        }

        static void unregisterListeningView(View v) {
            synchronized (sViewsWithListeners) {
                for (int i = 0; i < sViewsWithListeners.size(); i++) {
                    if (((WeakReference) sViewsWithListeners.get(i)).get() == v) {
                        sViewsWithListeners.remove(i);
                        return;
                    }
                }
            }
        }

        private void recalcViewsWithUnhandled() {
            WeakHashMap<View, Boolean> weakHashMap = this.mViewsContainingListeners;
            if (weakHashMap != null) {
                weakHashMap.clear();
            }
            if (!sViewsWithListeners.isEmpty()) {
                synchronized (sViewsWithListeners) {
                    if (this.mViewsContainingListeners == null) {
                        this.mViewsContainingListeners = new WeakHashMap<>();
                    }
                    for (int i = sViewsWithListeners.size() - 1; i >= 0; i--) {
                        View v = (View) ((WeakReference) sViewsWithListeners.get(i)).get();
                        if (v == null) {
                            sViewsWithListeners.remove(i);
                        } else {
                            this.mViewsContainingListeners.put(v, Boolean.TRUE);
                            for (ViewParent nxt = v.getParent(); nxt instanceof View; nxt = nxt.getParent()) {
                                this.mViewsContainingListeners.put((View) nxt, Boolean.TRUE);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Rect getEmptyTempRect() {
        if (sThreadLocalRect == null) {
            sThreadLocalRect = new ThreadLocal<>();
        }
        Rect rect = (Rect) sThreadLocalRect.get();
        if (rect == null) {
            rect = new Rect();
            sThreadLocalRect.set(rect);
        }
        rect.setEmpty();
        return rect;
    }

    @Deprecated
    public static boolean canScrollHorizontally(View view, int direction) {
        return view.canScrollHorizontally(direction);
    }

    @Deprecated
    public static boolean canScrollVertically(View view, int direction) {
        return view.canScrollVertically(direction);
    }

    @Deprecated
    public static int getOverScrollMode(View v) {
        return v.getOverScrollMode();
    }

    @Deprecated
    public static void setOverScrollMode(View v, int overScrollMode) {
        v.setOverScrollMode(overScrollMode);
    }

    @Deprecated
    public static void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
        v.onPopulateAccessibilityEvent(event);
    }

    @Deprecated
    public static void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
        v.onInitializeAccessibilityEvent(event);
    }

    public static void onInitializeAccessibilityNodeInfo(@NonNull View v, AccessibilityNodeInfoCompat info) {
        v.onInitializeAccessibilityNodeInfo(info.unwrap());
    }

    public static void setAccessibilityDelegate(@NonNull View v, AccessibilityDelegateCompat delegate) {
        if (delegate == null && (getAccessibilityDelegateInternal(v) instanceof AccessibilityDelegateAdapter)) {
            delegate = new AccessibilityDelegateCompat();
        }
        v.setAccessibilityDelegate(delegate == null ? null : delegate.getBridge());
    }

    public static void setAutofillHints(@NonNull View v, @Nullable String... autofillHints) {
        if (VERSION.SDK_INT >= 26) {
            v.setAutofillHints(autofillHints);
        }
    }

    @SuppressLint({"InlinedApi"})
    public static int getImportantForAutofill(@NonNull View v) {
        if (VERSION.SDK_INT >= 26) {
            return v.getImportantForAutofill();
        }
        return 0;
    }

    public static void setImportantForAutofill(@NonNull View v, int mode) {
        if (VERSION.SDK_INT >= 26) {
            v.setImportantForAutofill(mode);
        }
    }

    public static boolean isImportantForAutofill(@NonNull View v) {
        if (VERSION.SDK_INT >= 26) {
            return v.isImportantForAutofill();
        }
        return true;
    }

    public static boolean hasAccessibilityDelegate(@NonNull View view) {
        return getAccessibilityDelegateInternal(view) != null;
    }

    @Nullable
    public static AccessibilityDelegateCompat getAccessibilityDelegate(@NonNull View view) {
        AccessibilityDelegate delegate = getAccessibilityDelegateInternal(view);
        if (delegate == null) {
            return null;
        }
        if (delegate instanceof AccessibilityDelegateAdapter) {
            return ((AccessibilityDelegateAdapter) delegate).mCompat;
        }
        return new AccessibilityDelegateCompat(delegate);
    }

    static AccessibilityDelegateCompat getOrCreateAccessibilityDelegateCompat(@NonNull View v) {
        AccessibilityDelegateCompat delegateCompat = getAccessibilityDelegate(v);
        if (delegateCompat == null) {
            delegateCompat = new AccessibilityDelegateCompat();
        }
        setAccessibilityDelegate(v, delegateCompat);
        return delegateCompat;
    }

    @Nullable
    private static AccessibilityDelegate getAccessibilityDelegateInternal(@NonNull View v) {
        if (sAccessibilityDelegateCheckFailed) {
            return null;
        }
        if (sAccessibilityDelegateField == null) {
            try {
                sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
                sAccessibilityDelegateField.setAccessible(true);
            } catch (Throwable th) {
                sAccessibilityDelegateCheckFailed = true;
                return null;
            }
        }
        try {
            Object o = sAccessibilityDelegateField.get(v);
            if (o instanceof AccessibilityDelegate) {
                return (AccessibilityDelegate) o;
            }
            return null;
        } catch (Throwable th2) {
            sAccessibilityDelegateCheckFailed = true;
            return null;
        }
    }

    public static boolean hasTransientState(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            return view.hasTransientState();
        }
        return false;
    }

    public static void setHasTransientState(@NonNull View view, boolean hasTransientState) {
        if (VERSION.SDK_INT >= 16) {
            view.setHasTransientState(hasTransientState);
        }
    }

    public static void postInvalidateOnAnimation(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        } else {
            view.postInvalidate();
        }
    }

    public static void postInvalidateOnAnimation(@NonNull View view, int left, int top, int right, int bottom) {
        if (VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation(left, top, right, bottom);
        } else {
            view.postInvalidate(left, top, right, bottom);
        }
    }

    public static void postOnAnimation(@NonNull View view, Runnable action) {
        if (VERSION.SDK_INT >= 16) {
            view.postOnAnimation(action);
        } else {
            view.postDelayed(action, ValueAnimator.getFrameDelay());
        }
    }

    public static void postOnAnimationDelayed(@NonNull View view, Runnable action, long delayMillis) {
        if (VERSION.SDK_INT >= 16) {
            view.postOnAnimationDelayed(action, delayMillis);
        } else {
            view.postDelayed(action, ValueAnimator.getFrameDelay() + delayMillis);
        }
    }

    public static int getImportantForAccessibility(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            return view.getImportantForAccessibility();
        }
        return 0;
    }

    public static void setImportantForAccessibility(@NonNull View view, int mode) {
        if (VERSION.SDK_INT >= 19) {
            view.setImportantForAccessibility(mode);
        } else if (VERSION.SDK_INT >= 16) {
            if (mode == 4) {
                mode = 2;
            }
            view.setImportantForAccessibility(mode);
        }
    }

    public static boolean isImportantForAccessibility(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.isImportantForAccessibility();
        }
        return true;
    }

    public static boolean performAccessibilityAction(@NonNull View view, int action, Bundle arguments) {
        if (VERSION.SDK_INT >= 16) {
            return view.performAccessibilityAction(action, arguments);
        }
        return false;
    }

    public static int addAccessibilityAction(@NonNull View view, @NonNull CharSequence label, @NonNull AccessibilityViewCommand command) {
        int actionId = getAvailableActionIdFromResources(view);
        if (actionId != -1) {
            addAccessibilityAction(view, new AccessibilityActionCompat(actionId, label, command));
        }
        return actionId;
    }

    private static int getAvailableActionIdFromResources(View view) {
        int result = -1;
        List<AccessibilityActionCompat> actions = getActionList(view);
        int i = 0;
        while (true) {
            int[] iArr = ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
            if (i >= iArr.length || result != -1) {
                return result;
            }
            int id = iArr[i];
            boolean idAvailable = true;
            for (int j = 0; j < actions.size(); j++) {
                idAvailable &= ((AccessibilityActionCompat) actions.get(j)).getId() != id;
            }
            if (idAvailable) {
                result = id;
            }
            i++;
        }
        return result;
    }

    public static void replaceAccessibilityAction(@NonNull View view, @NonNull AccessibilityActionCompat replacedAction, @Nullable CharSequence label, @Nullable AccessibilityViewCommand command) {
        addAccessibilityAction(view, replacedAction.createReplacementAction(label, command));
    }

    private static void addAccessibilityAction(@NonNull View view, @NonNull AccessibilityActionCompat action) {
        if (VERSION.SDK_INT >= 21) {
            getOrCreateAccessibilityDelegateCompat(view);
            removeActionWithId(action.getId(), view);
            getActionList(view).add(action);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    public static void removeAccessibilityAction(@NonNull View view, int actionId) {
        if (VERSION.SDK_INT >= 21) {
            removeActionWithId(actionId, view);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    private static void removeActionWithId(int actionId, View view) {
        List<AccessibilityActionCompat> actions = getActionList(view);
        for (int i = 0; i < actions.size(); i++) {
            if (((AccessibilityActionCompat) actions.get(i)).getId() == actionId) {
                actions.remove(i);
                return;
            }
        }
    }

    private static List<AccessibilityActionCompat> getActionList(View view) {
        ArrayList<AccessibilityActionCompat> actions = (ArrayList) view.getTag(C0113R.C0115id.tag_accessibility_actions);
        if (actions != null) {
            return actions;
        }
        ArrayList arrayList = new ArrayList();
        view.setTag(C0113R.C0115id.tag_accessibility_actions, arrayList);
        return arrayList;
    }

    public static void enableAccessibleClickableSpanSupport(View view) {
        if (VERSION.SDK_INT >= 19) {
            getOrCreateAccessibilityDelegateCompat(view);
        }
    }

    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            AccessibilityNodeProvider provider = view.getAccessibilityNodeProvider();
            if (provider != null) {
                return new AccessibilityNodeProviderCompat(provider);
            }
        }
        return null;
    }

    @Deprecated
    public static float getAlpha(View view) {
        return view.getAlpha();
    }

    @Deprecated
    public static void setLayerType(View view, int layerType, Paint paint) {
        view.setLayerType(layerType, paint);
    }

    @Deprecated
    public static int getLayerType(View view) {
        return view.getLayerType();
    }

    public static int getLabelFor(@NonNull View view) {
        if (VERSION.SDK_INT >= 17) {
            return view.getLabelFor();
        }
        return 0;
    }

    public static void setLabelFor(@NonNull View view, @IdRes int labeledId) {
        if (VERSION.SDK_INT >= 17) {
            view.setLabelFor(labeledId);
        }
    }

    public static void setLayerPaint(@NonNull View view, Paint paint) {
        if (VERSION.SDK_INT >= 17) {
            view.setLayerPaint(paint);
            return;
        }
        view.setLayerType(view.getLayerType(), paint);
        view.invalidate();
    }

    public static int getLayoutDirection(@NonNull View view) {
        if (VERSION.SDK_INT >= 17) {
            return view.getLayoutDirection();
        }
        return 0;
    }

    public static void setLayoutDirection(@NonNull View view, int layoutDirection) {
        if (VERSION.SDK_INT >= 17) {
            view.setLayoutDirection(layoutDirection);
        }
    }

    public static ViewParent getParentForAccessibility(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            return view.getParentForAccessibility();
        }
        return view.getParent();
    }

    @NonNull
    public static <T extends View> T requireViewById(@NonNull View view, @IdRes int id) {
        if (VERSION.SDK_INT >= 28) {
            return view.requireViewById(id);
        }
        T targetView = view.findViewById(id);
        if (targetView != null) {
            return targetView;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this View");
    }

    @Deprecated
    public static boolean isOpaque(View view) {
        return view.isOpaque();
    }

    @Deprecated
    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        return View.resolveSizeAndState(size, measureSpec, childMeasuredState);
    }

    @Deprecated
    public static int getMeasuredWidthAndState(View view) {
        return view.getMeasuredWidthAndState();
    }

    @Deprecated
    public static int getMeasuredHeightAndState(View view) {
        return view.getMeasuredHeightAndState();
    }

    @Deprecated
    public static int getMeasuredState(View view) {
        return view.getMeasuredState();
    }

    @Deprecated
    public static int combineMeasuredStates(int curState, int newState) {
        return View.combineMeasuredStates(curState, newState);
    }

    public static int getAccessibilityLiveRegion(@NonNull View view) {
        if (VERSION.SDK_INT >= 19) {
            return view.getAccessibilityLiveRegion();
        }
        return 0;
    }

    public static void setAccessibilityLiveRegion(@NonNull View view, int mode) {
        if (VERSION.SDK_INT >= 19) {
            view.setAccessibilityLiveRegion(mode);
        }
    }

    @C0007Px
    public static int getPaddingStart(@NonNull View view) {
        if (VERSION.SDK_INT >= 17) {
            return view.getPaddingStart();
        }
        return view.getPaddingLeft();
    }

    @C0007Px
    public static int getPaddingEnd(@NonNull View view) {
        if (VERSION.SDK_INT >= 17) {
            return view.getPaddingEnd();
        }
        return view.getPaddingRight();
    }

    public static void setPaddingRelative(@NonNull View view, @C0007Px int start, @C0007Px int top, @C0007Px int end, @C0007Px int bottom) {
        if (VERSION.SDK_INT >= 17) {
            view.setPaddingRelative(start, top, end, bottom);
        } else {
            view.setPadding(start, top, end, bottom);
        }
    }

    private static void bindTempDetach() {
        try {
            sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
            sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Couldn't find method", e);
        }
        sTempDetachBound = true;
    }

    public static void dispatchStartTemporaryDetach(@NonNull View view) {
        if (VERSION.SDK_INT >= 24) {
            view.dispatchStartTemporaryDetach();
            return;
        }
        if (!sTempDetachBound) {
            bindTempDetach();
        }
        Method method = sDispatchStartTemporaryDetach;
        if (method != null) {
            try {
                method.invoke(view, new Object[0]);
            } catch (Exception e) {
                Log.d(TAG, "Error calling dispatchStartTemporaryDetach", e);
            }
        } else {
            view.onStartTemporaryDetach();
        }
    }

    public static void dispatchFinishTemporaryDetach(@NonNull View view) {
        if (VERSION.SDK_INT >= 24) {
            view.dispatchFinishTemporaryDetach();
            return;
        }
        if (!sTempDetachBound) {
            bindTempDetach();
        }
        Method method = sDispatchFinishTemporaryDetach;
        if (method != null) {
            try {
                method.invoke(view, new Object[0]);
            } catch (Exception e) {
                Log.d(TAG, "Error calling dispatchFinishTemporaryDetach", e);
            }
        } else {
            view.onFinishTemporaryDetach();
        }
    }

    @Deprecated
    public static float getTranslationX(View view) {
        return view.getTranslationX();
    }

    @Deprecated
    public static float getTranslationY(View view) {
        return view.getTranslationY();
    }

    @Deprecated
    @Nullable
    public static Matrix getMatrix(View view) {
        return view.getMatrix();
    }

    public static int getMinimumWidth(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            return view.getMinimumWidth();
        }
        if (!sMinWidthFieldFetched) {
            try {
                sMinWidthField = View.class.getDeclaredField("mMinWidth");
                sMinWidthField.setAccessible(true);
            } catch (NoSuchFieldException e) {
            }
            sMinWidthFieldFetched = true;
        }
        Field field = sMinWidthField;
        if (field != null) {
            try {
                return ((Integer) field.get(view)).intValue();
            } catch (Exception e2) {
            }
        }
        return 0;
    }

    public static int getMinimumHeight(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            return view.getMinimumHeight();
        }
        if (!sMinHeightFieldFetched) {
            try {
                sMinHeightField = View.class.getDeclaredField("mMinHeight");
                sMinHeightField.setAccessible(true);
            } catch (NoSuchFieldException e) {
            }
            sMinHeightFieldFetched = true;
        }
        Field field = sMinHeightField;
        if (field != null) {
            try {
                return ((Integer) field.get(view)).intValue();
            } catch (Exception e2) {
            }
        }
        return 0;
    }

    @NonNull
    public static ViewPropertyAnimatorCompat animate(@NonNull View view) {
        if (sViewPropertyAnimatorMap == null) {
            sViewPropertyAnimatorMap = new WeakHashMap<>();
        }
        ViewPropertyAnimatorCompat vpa = (ViewPropertyAnimatorCompat) sViewPropertyAnimatorMap.get(view);
        if (vpa != null) {
            return vpa;
        }
        ViewPropertyAnimatorCompat vpa2 = new ViewPropertyAnimatorCompat(view);
        sViewPropertyAnimatorMap.put(view, vpa2);
        return vpa2;
    }

    @Deprecated
    public static void setTranslationX(View view, float value) {
        view.setTranslationX(value);
    }

    @Deprecated
    public static void setTranslationY(View view, float value) {
        view.setTranslationY(value);
    }

    @Deprecated
    public static void setAlpha(View view, @FloatRange(from = 0.0d, mo37to = 1.0d) float value) {
        view.setAlpha(value);
    }

    @Deprecated
    public static void setX(View view, float value) {
        view.setX(value);
    }

    @Deprecated
    public static void setY(View view, float value) {
        view.setY(value);
    }

    @Deprecated
    public static void setRotation(View view, float value) {
        view.setRotation(value);
    }

    @Deprecated
    public static void setRotationX(View view, float value) {
        view.setRotationX(value);
    }

    @Deprecated
    public static void setRotationY(View view, float value) {
        view.setRotationY(value);
    }

    @Deprecated
    public static void setScaleX(View view, float value) {
        view.setScaleX(value);
    }

    @Deprecated
    public static void setScaleY(View view, float value) {
        view.setScaleY(value);
    }

    @Deprecated
    public static float getPivotX(View view) {
        return view.getPivotX();
    }

    @Deprecated
    public static void setPivotX(View view, float value) {
        view.setPivotX(value);
    }

    @Deprecated
    public static float getPivotY(View view) {
        return view.getPivotY();
    }

    @Deprecated
    public static void setPivotY(View view, float value) {
        view.setPivotY(value);
    }

    @Deprecated
    public static float getRotation(View view) {
        return view.getRotation();
    }

    @Deprecated
    public static float getRotationX(View view) {
        return view.getRotationX();
    }

    @Deprecated
    public static float getRotationY(View view) {
        return view.getRotationY();
    }

    @Deprecated
    public static float getScaleX(View view) {
        return view.getScaleX();
    }

    @Deprecated
    public static float getScaleY(View view) {
        return view.getScaleY();
    }

    @Deprecated
    public static float getX(View view) {
        return view.getX();
    }

    @Deprecated
    public static float getY(View view) {
        return view.getY();
    }

    public static void setElevation(@NonNull View view, float elevation) {
        if (VERSION.SDK_INT >= 21) {
            view.setElevation(elevation);
        }
    }

    public static float getElevation(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.getElevation();
        }
        return 0.0f;
    }

    public static void setTranslationZ(@NonNull View view, float translationZ) {
        if (VERSION.SDK_INT >= 21) {
            view.setTranslationZ(translationZ);
        }
    }

    public static float getTranslationZ(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.getTranslationZ();
        }
        return 0.0f;
    }

    public static void setTransitionName(@NonNull View view, String transitionName) {
        if (VERSION.SDK_INT >= 21) {
            view.setTransitionName(transitionName);
            return;
        }
        if (sTransitionNameMap == null) {
            sTransitionNameMap = new WeakHashMap<>();
        }
        sTransitionNameMap.put(view, transitionName);
    }

    @Nullable
    public static String getTransitionName(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.getTransitionName();
        }
        WeakHashMap<View, String> weakHashMap = sTransitionNameMap;
        if (weakHashMap == null) {
            return null;
        }
        return (String) weakHashMap.get(view);
    }

    public static int getWindowSystemUiVisibility(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            return view.getWindowSystemUiVisibility();
        }
        return 0;
    }

    public static void requestApplyInsets(@NonNull View view) {
        if (VERSION.SDK_INT >= 20) {
            view.requestApplyInsets();
        } else if (VERSION.SDK_INT >= 16) {
            view.requestFitSystemWindows();
        }
    }

    @Deprecated
    public static void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled) {
        String str = "Unable to invoke childrenDrawingOrderEnabled";
        Method method = sChildrenDrawingOrderMethod;
        String str2 = TAG;
        if (method == null) {
            try {
                sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
            } catch (NoSuchMethodException e) {
                Log.e(str2, "Unable to find childrenDrawingOrderEnabled", e);
            }
            sChildrenDrawingOrderMethod.setAccessible(true);
        }
        try {
            sChildrenDrawingOrderMethod.invoke(viewGroup, new Object[]{Boolean.valueOf(enabled)});
        } catch (IllegalAccessException e2) {
            Log.e(str2, str, e2);
        } catch (IllegalArgumentException e3) {
            Log.e(str2, str, e3);
        } catch (InvocationTargetException e4) {
            Log.e(str2, str, e4);
        }
    }

    public static boolean getFitsSystemWindows(@NonNull View v) {
        if (VERSION.SDK_INT >= 16) {
            return v.getFitsSystemWindows();
        }
        return false;
    }

    @Deprecated
    public static void setFitsSystemWindows(View view, boolean fitSystemWindows) {
        view.setFitsSystemWindows(fitSystemWindows);
    }

    @Deprecated
    public static void jumpDrawablesToCurrentState(View v) {
        v.jumpDrawablesToCurrentState();
    }

    public static void setOnApplyWindowInsetsListener(@NonNull View v, final OnApplyWindowInsetsListener listener) {
        if (VERSION.SDK_INT >= 21) {
            if (listener == null) {
                v.setOnApplyWindowInsetsListener(null);
                return;
            }
            v.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                    return (WindowInsets) WindowInsetsCompat.unwrap(listener.onApplyWindowInsets(view, WindowInsetsCompat.wrap(insets)));
                }
            });
        }
    }

    public static WindowInsetsCompat onApplyWindowInsets(@NonNull View view, WindowInsetsCompat insets) {
        if (VERSION.SDK_INT < 21) {
            return insets;
        }
        WindowInsets unwrapped = (WindowInsets) WindowInsetsCompat.unwrap(insets);
        WindowInsets result = view.onApplyWindowInsets(unwrapped);
        if (!result.equals(unwrapped)) {
            unwrapped = new WindowInsets(result);
        }
        return WindowInsetsCompat.wrap(unwrapped);
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(@NonNull View view, WindowInsetsCompat insets) {
        if (VERSION.SDK_INT < 21) {
            return insets;
        }
        WindowInsets unwrapped = (WindowInsets) WindowInsetsCompat.unwrap(insets);
        WindowInsets result = view.dispatchApplyWindowInsets(unwrapped);
        if (!result.equals(unwrapped)) {
            unwrapped = new WindowInsets(result);
        }
        return WindowInsetsCompat.wrap(unwrapped);
    }

    @Deprecated
    public static void setSaveFromParentEnabled(View v, boolean enabled) {
        v.setSaveFromParentEnabled(enabled);
    }

    @Deprecated
    public static void setActivated(View view, boolean activated) {
        view.setActivated(activated);
    }

    public static boolean hasOverlappingRendering(@NonNull View view) {
        if (VERSION.SDK_INT >= 16) {
            return view.hasOverlappingRendering();
        }
        return true;
    }

    public static boolean isPaddingRelative(@NonNull View view) {
        if (VERSION.SDK_INT >= 17) {
            return view.isPaddingRelative();
        }
        return false;
    }

    public static void setBackground(@NonNull View view, @Nullable Drawable background) {
        if (VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static ColorStateList getBackgroundTintList(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintList();
        }
        return view instanceof TintableBackgroundView ? ((TintableBackgroundView) view).getSupportBackgroundTintList() : null;
    }

    public static void setBackgroundTintList(@NonNull View view, ColorStateList tintList) {
        if (VERSION.SDK_INT >= 21) {
            view.setBackgroundTintList(tintList);
            if (VERSION.SDK_INT == 21) {
                Drawable background = view.getBackground();
                boolean hasTint = (view.getBackgroundTintList() == null && view.getBackgroundTintMode() == null) ? false : true;
                if (background != null && hasTint) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        } else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView) view).setSupportBackgroundTintList(tintList);
        }
    }

    public static Mode getBackgroundTintMode(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintMode();
        }
        return view instanceof TintableBackgroundView ? ((TintableBackgroundView) view).getSupportBackgroundTintMode() : null;
    }

    public static void setBackgroundTintMode(@NonNull View view, Mode mode) {
        if (VERSION.SDK_INT >= 21) {
            view.setBackgroundTintMode(mode);
            if (VERSION.SDK_INT == 21) {
                Drawable background = view.getBackground();
                boolean hasTint = (view.getBackgroundTintList() == null && view.getBackgroundTintMode() == null) ? false : true;
                if (background != null && hasTint) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        } else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView) view).setSupportBackgroundTintMode(mode);
        }
    }

    public static void setNestedScrollingEnabled(@NonNull View view, boolean enabled) {
        if (VERSION.SDK_INT >= 21) {
            view.setNestedScrollingEnabled(enabled);
        } else if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild) view).setNestedScrollingEnabled(enabled);
        }
    }

    public static boolean isNestedScrollingEnabled(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.isNestedScrollingEnabled();
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).isNestedScrollingEnabled();
        }
        return false;
    }

    public static boolean startNestedScroll(@NonNull View view, int axes) {
        if (VERSION.SDK_INT >= 21) {
            return view.startNestedScroll(axes);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).startNestedScroll(axes);
        }
        return false;
    }

    public static void stopNestedScroll(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            view.stopNestedScroll();
        } else if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild) view).stopNestedScroll();
        }
    }

    public static boolean hasNestedScrollingParent(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.hasNestedScrollingParent();
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).hasNestedScrollingParent();
        }
        return false;
    }

    public static boolean dispatchNestedScroll(@NonNull View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        if (VERSION.SDK_INT >= 21) {
            return view.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
        return false;
    }

    public static boolean dispatchNestedPreScroll(@NonNull View view, int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        if (VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        }
        return false;
    }

    public static boolean startNestedScroll(@NonNull View view, int axes, int type) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2) view).startNestedScroll(axes, type);
        }
        if (type == 0) {
            return startNestedScroll(view, axes);
        }
        return false;
    }

    public static void stopNestedScroll(@NonNull View view, int type) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2) view).stopNestedScroll(type);
        } else if (type == 0) {
            stopNestedScroll(view);
        }
    }

    public static boolean hasNestedScrollingParent(@NonNull View view, int type) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2) view).hasNestedScrollingParent(type);
        } else if (type == 0) {
            return hasNestedScrollingParent(view);
        }
        return false;
    }

    public static void dispatchNestedScroll(@NonNull View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        View view2 = view;
        if (view2 instanceof NestedScrollingChild3) {
            ((NestedScrollingChild3) view2).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
        } else {
            dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        }
    }

    public static boolean dispatchNestedScroll(@NonNull View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2) view).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        }
        if (type == 0) {
            return dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
        return false;
    }

    public static boolean dispatchNestedPreScroll(@NonNull View view, int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2) view).dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
        }
        if (type == 0) {
            return dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
        }
        return false;
    }

    public static boolean dispatchNestedFling(@NonNull View view, float velocityX, float velocityY, boolean consumed) {
        if (VERSION.SDK_INT >= 21) {
            return view.dispatchNestedFling(velocityX, velocityY, consumed);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedFling(velocityX, velocityY, consumed);
        }
        return false;
    }

    public static boolean dispatchNestedPreFling(@NonNull View view, float velocityX, float velocityY) {
        if (VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreFling(velocityX, velocityY);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedPreFling(velocityX, velocityY);
        }
        return false;
    }

    public static boolean isInLayout(@NonNull View view) {
        if (VERSION.SDK_INT >= 18) {
            return view.isInLayout();
        }
        return false;
    }

    public static boolean isLaidOut(@NonNull View view) {
        if (VERSION.SDK_INT >= 19) {
            return view.isLaidOut();
        }
        return view.getWidth() > 0 && view.getHeight() > 0;
    }

    public static boolean isLayoutDirectionResolved(@NonNull View view) {
        if (VERSION.SDK_INT >= 19) {
            return view.isLayoutDirectionResolved();
        }
        return false;
    }

    public static float getZ(@NonNull View view) {
        if (VERSION.SDK_INT >= 21) {
            return view.getZ();
        }
        return 0.0f;
    }

    public static void setZ(@NonNull View view, float z) {
        if (VERSION.SDK_INT >= 21) {
            view.setZ(z);
        }
    }

    public static void offsetTopAndBottom(@NonNull View view, int offset) {
        if (VERSION.SDK_INT >= 23) {
            view.offsetTopAndBottom(offset);
        } else if (VERSION.SDK_INT >= 21) {
            Rect parentRect = getEmptyTempRect();
            boolean needInvalidateWorkaround = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View p = (View) parent;
                parentRect.set(p.getLeft(), p.getTop(), p.getRight(), p.getBottom());
                needInvalidateWorkaround = !parentRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            compatOffsetTopAndBottom(view, offset);
            if (needInvalidateWorkaround && parentRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View) parent).invalidate(parentRect);
            }
        } else {
            compatOffsetTopAndBottom(view, offset);
        }
    }

    private static void compatOffsetTopAndBottom(View view, int offset) {
        view.offsetTopAndBottom(offset);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View) parent);
            }
        }
    }

    public static void offsetLeftAndRight(@NonNull View view, int offset) {
        if (VERSION.SDK_INT >= 23) {
            view.offsetLeftAndRight(offset);
        } else if (VERSION.SDK_INT >= 21) {
            Rect parentRect = getEmptyTempRect();
            boolean needInvalidateWorkaround = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View p = (View) parent;
                parentRect.set(p.getLeft(), p.getTop(), p.getRight(), p.getBottom());
                needInvalidateWorkaround = !parentRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            compatOffsetLeftAndRight(view, offset);
            if (needInvalidateWorkaround && parentRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View) parent).invalidate(parentRect);
            }
        } else {
            compatOffsetLeftAndRight(view, offset);
        }
    }

    private static void compatOffsetLeftAndRight(View view, int offset) {
        view.offsetLeftAndRight(offset);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View) parent);
            }
        }
    }

    private static void tickleInvalidationFlag(View view) {
        float y = view.getTranslationY();
        view.setTranslationY(1.0f + y);
        view.setTranslationY(y);
    }

    public static void setClipBounds(@NonNull View view, Rect clipBounds) {
        if (VERSION.SDK_INT >= 18) {
            view.setClipBounds(clipBounds);
        }
    }

    @Nullable
    public static Rect getClipBounds(@NonNull View view) {
        if (VERSION.SDK_INT >= 18) {
            return view.getClipBounds();
        }
        return null;
    }

    public static boolean isAttachedToWindow(@NonNull View view) {
        if (VERSION.SDK_INT >= 19) {
            return view.isAttachedToWindow();
        }
        return view.getWindowToken() != null;
    }

    public static boolean hasOnClickListeners(@NonNull View view) {
        if (VERSION.SDK_INT >= 15) {
            return view.hasOnClickListeners();
        }
        return false;
    }

    public static void setScrollIndicators(@NonNull View view, int indicators) {
        if (VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(indicators);
        }
    }

    public static void setScrollIndicators(@NonNull View view, int indicators, int mask) {
        if (VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(indicators, mask);
        }
    }

    public static int getScrollIndicators(@NonNull View view) {
        if (VERSION.SDK_INT >= 23) {
            return view.getScrollIndicators();
        }
        return 0;
    }

    public static void setPointerIcon(@NonNull View view, PointerIconCompat pointerIcon) {
        if (VERSION.SDK_INT >= 24) {
            view.setPointerIcon((PointerIcon) (pointerIcon != null ? pointerIcon.getPointerIcon() : null));
        }
    }

    @Nullable
    public static Display getDisplay(@NonNull View view) {
        if (VERSION.SDK_INT >= 17) {
            return view.getDisplay();
        }
        if (isAttachedToWindow(view)) {
            return ((WindowManager) view.getContext().getSystemService("window")).getDefaultDisplay();
        }
        return null;
    }

    public static void setTooltipText(@NonNull View view, @Nullable CharSequence tooltipText) {
        if (VERSION.SDK_INT >= 26) {
            view.setTooltipText(tooltipText);
        }
    }

    public static boolean startDragAndDrop(@NonNull View v, ClipData data, DragShadowBuilder shadowBuilder, Object localState, int flags) {
        if (VERSION.SDK_INT >= 24) {
            return v.startDragAndDrop(data, shadowBuilder, localState, flags);
        }
        return v.startDrag(data, shadowBuilder, localState, flags);
    }

    public static void cancelDragAndDrop(@NonNull View v) {
        if (VERSION.SDK_INT >= 24) {
            v.cancelDragAndDrop();
        }
    }

    public static void updateDragShadow(@NonNull View v, DragShadowBuilder shadowBuilder) {
        if (VERSION.SDK_INT >= 24) {
            v.updateDragShadow(shadowBuilder);
        }
    }

    public static int getNextClusterForwardId(@NonNull View view) {
        if (VERSION.SDK_INT >= 26) {
            return view.getNextClusterForwardId();
        }
        return -1;
    }

    public static void setNextClusterForwardId(@NonNull View view, int nextClusterForwardId) {
        if (VERSION.SDK_INT >= 26) {
            view.setNextClusterForwardId(nextClusterForwardId);
        }
    }

    public static boolean isKeyboardNavigationCluster(@NonNull View view) {
        if (VERSION.SDK_INT >= 26) {
            return view.isKeyboardNavigationCluster();
        }
        return false;
    }

    public static void setKeyboardNavigationCluster(@NonNull View view, boolean isCluster) {
        if (VERSION.SDK_INT >= 26) {
            view.setKeyboardNavigationCluster(isCluster);
        }
    }

    public static boolean isFocusedByDefault(@NonNull View view) {
        if (VERSION.SDK_INT >= 26) {
            return view.isFocusedByDefault();
        }
        return false;
    }

    public static void setFocusedByDefault(@NonNull View view, boolean isFocusedByDefault) {
        if (VERSION.SDK_INT >= 26) {
            view.setFocusedByDefault(isFocusedByDefault);
        }
    }

    public static View keyboardNavigationClusterSearch(@NonNull View view, View currentCluster, int direction) {
        if (VERSION.SDK_INT >= 26) {
            return view.keyboardNavigationClusterSearch(currentCluster, direction);
        }
        return null;
    }

    public static void addKeyboardNavigationClusters(@NonNull View view, @NonNull Collection<View> views, int direction) {
        if (VERSION.SDK_INT >= 26) {
            view.addKeyboardNavigationClusters(views, direction);
        }
    }

    public static boolean restoreDefaultFocus(@NonNull View view) {
        if (VERSION.SDK_INT >= 26) {
            return view.restoreDefaultFocus();
        }
        return view.requestFocus();
    }

    public static boolean hasExplicitFocusable(@NonNull View view) {
        if (VERSION.SDK_INT >= 26) {
            return view.hasExplicitFocusable();
        }
        return view.hasFocusable();
    }

    public static int generateViewId() {
        int result;
        int newValue;
        if (VERSION.SDK_INT >= 17) {
            return View.generateViewId();
        }
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > 16777215) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }

    public static void addOnUnhandledKeyEventListener(@NonNull View v, @NonNull final OnUnhandledKeyEventListenerCompat listener) {
        if (VERSION.SDK_INT >= 28) {
            Map map = (Map) v.getTag(C0113R.C0115id.tag_unhandled_key_listeners);
            if (map == null) {
                map = new ArrayMap();
                v.setTag(C0113R.C0115id.tag_unhandled_key_listeners, map);
            }
            OnUnhandledKeyEventListener fwListener = new OnUnhandledKeyEventListener() {
                public boolean onUnhandledKeyEvent(View v, KeyEvent event) {
                    return listener.onUnhandledKeyEvent(v, event);
                }
            };
            map.put(listener, fwListener);
            v.addOnUnhandledKeyEventListener(fwListener);
            return;
        }
        ArrayList arrayList = (ArrayList) v.getTag(C0113R.C0115id.tag_unhandled_key_listeners);
        if (arrayList == null) {
            arrayList = new ArrayList();
            v.setTag(C0113R.C0115id.tag_unhandled_key_listeners, arrayList);
        }
        arrayList.add(listener);
        if (arrayList.size() == 1) {
            UnhandledKeyEventManager.registerListeningView(v);
        }
    }

    public static void removeOnUnhandledKeyEventListener(@NonNull View v, @NonNull OnUnhandledKeyEventListenerCompat listener) {
        if (VERSION.SDK_INT >= 28) {
            Map<OnUnhandledKeyEventListenerCompat, OnUnhandledKeyEventListener> viewListeners = (Map) v.getTag(C0113R.C0115id.tag_unhandled_key_listeners);
            if (viewListeners != null) {
                OnUnhandledKeyEventListener fwListener = (OnUnhandledKeyEventListener) viewListeners.get(listener);
                if (fwListener != null) {
                    v.removeOnUnhandledKeyEventListener(fwListener);
                }
                return;
            }
            return;
        }
        ArrayList<OnUnhandledKeyEventListenerCompat> viewListeners2 = (ArrayList) v.getTag(C0113R.C0115id.tag_unhandled_key_listeners);
        if (viewListeners2 != null) {
            viewListeners2.remove(listener);
            if (viewListeners2.size() == 0) {
                UnhandledKeyEventManager.unregisterListeningView(v);
            }
        }
    }

    protected ViewCompat() {
    }

    @UiThread
    static boolean dispatchUnhandledKeyEventBeforeHierarchy(View root, KeyEvent evt) {
        if (VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.m4at(root).preDispatch(evt);
    }

    @UiThread
    static boolean dispatchUnhandledKeyEventBeforeCallback(View root, KeyEvent evt) {
        if (VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.m4at(root).dispatch(root, evt);
    }

    @UiThread
    public static void setScreenReaderFocusable(View view, boolean screenReaderFocusable) {
        screenReaderFocusableProperty().set(view, Boolean.valueOf(screenReaderFocusable));
    }

    @UiThread
    public static boolean isScreenReaderFocusable(View view) {
        Boolean result = (Boolean) screenReaderFocusableProperty().get(view);
        if (result == null) {
            return false;
        }
        return result.booleanValue();
    }

    private static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
        return new AccessibilityViewProperty<Boolean>(C0113R.C0115id.tag_screen_reader_focusable, Boolean.class, 28) {
            /* access modifiers changed from: 0000 */
            @RequiresApi(28)
            public Boolean frameworkGet(View view) {
                return Boolean.valueOf(view.isScreenReaderFocusable());
            }

            /* access modifiers changed from: 0000 */
            @RequiresApi(28)
            public void frameworkSet(View view, Boolean value) {
                view.setScreenReaderFocusable(value.booleanValue());
            }

            /* access modifiers changed from: 0000 */
            public boolean shouldUpdate(Boolean oldValue, Boolean newValue) {
                return !booleanNullToFalseEquals(oldValue, newValue);
            }
        };
    }

    @UiThread
    public static void setAccessibilityPaneTitle(View view, CharSequence accessibilityPaneTitle) {
        if (VERSION.SDK_INT >= 19) {
            paneTitleProperty().set(view, accessibilityPaneTitle);
            if (accessibilityPaneTitle != null) {
                sAccessibilityPaneVisibilityManager.addAccessibilityPane(view);
            } else {
                sAccessibilityPaneVisibilityManager.removeAccessibilityPane(view);
            }
        }
    }

    @UiThread
    public static CharSequence getAccessibilityPaneTitle(View view) {
        return (CharSequence) paneTitleProperty().get(view);
    }

    private static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
        return new AccessibilityViewProperty<CharSequence>(C0113R.C0115id.tag_accessibility_pane_title, CharSequence.class, 8, 28) {
            /* access modifiers changed from: 0000 */
            @RequiresApi(28)
            public CharSequence frameworkGet(View view) {
                return view.getAccessibilityPaneTitle();
            }

            /* access modifiers changed from: 0000 */
            @RequiresApi(28)
            public void frameworkSet(View view, CharSequence value) {
                view.setAccessibilityPaneTitle(value);
            }

            /* access modifiers changed from: 0000 */
            public boolean shouldUpdate(CharSequence oldValue, CharSequence newValue) {
                return !TextUtils.equals(oldValue, newValue);
            }
        };
    }

    @UiThread
    public static boolean isAccessibilityHeading(View view) {
        Boolean result = (Boolean) accessibilityHeadingProperty().get(view);
        if (result == null) {
            return false;
        }
        return result.booleanValue();
    }

    @UiThread
    public static void setAccessibilityHeading(View view, boolean isHeading) {
        accessibilityHeadingProperty().set(view, Boolean.valueOf(isHeading));
    }

    private static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
        return new AccessibilityViewProperty<Boolean>(C0113R.C0115id.tag_accessibility_heading, Boolean.class, 28) {
            /* access modifiers changed from: 0000 */
            @RequiresApi(28)
            public Boolean frameworkGet(View view) {
                return Boolean.valueOf(view.isAccessibilityHeading());
            }

            /* access modifiers changed from: 0000 */
            @RequiresApi(28)
            public void frameworkSet(View view, Boolean value) {
                view.setAccessibilityHeading(value.booleanValue());
            }

            /* access modifiers changed from: 0000 */
            public boolean shouldUpdate(Boolean oldValue, Boolean newValue) {
                return !booleanNullToFalseEquals(oldValue, newValue);
            }
        };
    }

    @RequiresApi(19)
    static void notifyViewAccessibilityStateChangedIfNeeded(View view, int changeType) {
        if (((AccessibilityManager) view.getContext().getSystemService("accessibility")).isEnabled()) {
            boolean isAccessibilityPane = getAccessibilityPaneTitle(view) != null;
            if (getAccessibilityLiveRegion(view) != 0 || (isAccessibilityPane && view.getVisibility() == 0)) {
                AccessibilityEvent event = AccessibilityEvent.obtain();
                event.setEventType(isAccessibilityPane ? 32 : 2048);
                event.setContentChangeTypes(changeType);
                view.sendAccessibilityEventUnchecked(event);
            } else if (view.getParent() != null) {
                try {
                    view.getParent().notifySubtreeAccessibilityStateChanged(view, view, changeType);
                } catch (AbstractMethodError e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(view.getParent().getClass().getSimpleName());
                    sb.append(" does not fully implement ViewParent");
                    Log.e(TAG, sb.toString(), e);
                }
            }
        }
    }
}
