package androidx.fragment.app;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle.State;
import androidx.viewpager.widget.PagerAdapter;

public abstract class FragmentPagerAdapter extends PagerAdapter {
    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;
    @Deprecated
    public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 0;
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentPagerAdapter";
    private final int mBehavior;
    private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem;
    private final FragmentManager mFragmentManager;

    @NonNull
    public abstract Fragment getItem(int i);

    @Deprecated
    public FragmentPagerAdapter(@NonNull FragmentManager fm) {
        this(fm, 0);
    }

    public FragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        this.mCurTransaction = null;
        this.mCurrentPrimaryItem = null;
        this.mFragmentManager = fm;
        this.mBehavior = behavior;
    }

    public void startUpdate(@NonNull ViewGroup container) {
        if (container.getId() == -1) {
            StringBuilder sb = new StringBuilder();
            sb.append("ViewPager with adapter ");
            sb.append(this);
            sb.append(" requires a view id");
            throw new IllegalStateException(sb.toString());
        }
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        long itemId = getItemId(position);
        Fragment fragment = this.mFragmentManager.findFragmentByTag(makeFragmentName(container.getId(), itemId));
        if (fragment != null) {
            this.mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            this.mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
        }
        if (fragment != this.mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            if (this.mBehavior == 1) {
                this.mCurTransaction.setMaxLifecycle(fragment, State.STARTED);
            } else {
                fragment.setUserVisibleHint(false);
            }
        }
        return fragment;
    }

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        this.mCurTransaction.detach(fragment);
        if (fragment == this.mCurrentPrimaryItem) {
            this.mCurrentPrimaryItem = null;
        }
    }

    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        Fragment fragment2 = this.mCurrentPrimaryItem;
        if (fragment != fragment2) {
            if (fragment2 != null) {
                fragment2.setMenuVisibility(false);
                if (this.mBehavior == 1) {
                    if (this.mCurTransaction == null) {
                        this.mCurTransaction = this.mFragmentManager.beginTransaction();
                    }
                    this.mCurTransaction.setMaxLifecycle(this.mCurrentPrimaryItem, State.STARTED);
                } else {
                    this.mCurrentPrimaryItem.setUserVisibleHint(false);
                }
            }
            fragment.setMenuVisibility(true);
            if (this.mBehavior == 1) {
                if (this.mCurTransaction == null) {
                    this.mCurTransaction = this.mFragmentManager.beginTransaction();
                }
                this.mCurTransaction.setMaxLifecycle(fragment, State.RESUMED);
            } else {
                fragment.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = fragment;
        }
    }

    public void finishUpdate(@NonNull ViewGroup container) {
        FragmentTransaction fragmentTransaction = this.mCurTransaction;
        if (fragmentTransaction != null) {
            fragmentTransaction.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
        }
    }

    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Nullable
    public Parcelable saveState() {
        return null;
    }

    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
    }

    public long getItemId(int position) {
        return (long) position;
    }

    private static String makeFragmentName(int viewId, long id) {
        StringBuilder sb = new StringBuilder();
        sb.append("android:switcher:");
        sb.append(viewId);
        sb.append(":");
        sb.append(id);
        return sb.toString();
    }
}
