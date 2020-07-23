package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.XmlRes;
import androidx.core.C0113R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public final class ColorStateListInflaterCompat {
    private ColorStateListInflaterCompat() {
    }

    @Nullable
    public static ColorStateList inflate(@NonNull Resources resources, @XmlRes int resId, @Nullable Theme theme) {
        try {
            return createFromXml(resources, resources.getXml(resId), theme);
        } catch (Exception e) {
            Log.e("CSLCompat", "Failed to inflate ColorStateList.", e);
            return null;
        }
    }

    @NonNull
    public static ColorStateList createFromXml(@NonNull Resources r, @NonNull XmlPullParser parser, @Nullable Theme theme) throws XmlPullParserException, IOException {
        int type;
        AttributeSet attrs = Xml.asAttributeSet(parser);
        do {
            int next = parser.next();
            type = next;
            if (next == 2) {
                break;
            }
        } while (type != 1);
        if (type == 2) {
            return createFromXmlInner(r, parser, attrs, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    @NonNull
    public static ColorStateList createFromXmlInner(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        String name = parser.getName();
        if (name.equals("selector")) {
            return inflate(r, parser, attrs, theme);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parser.getPositionDescription());
        sb.append(": invalid color state list tag ");
        sb.append(name);
        throw new XmlPullParserException(sb.toString());
    }

    private static ColorStateList inflate(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        AttributeSet attributeSet = attrs;
        int i = 1;
        int innerDepth = parser.getDepth() + 1;
        int[][] stateSpecList = new int[20][];
        int[] colorList = new int[stateSpecList.length];
        int listSize = 0;
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == i) {
                Resources resources = r;
                Theme theme2 = theme;
                int i2 = innerDepth;
                break;
            }
            int depth = parser.getDepth();
            int depth2 = depth;
            if (depth < innerDepth && type == 3) {
                Resources resources2 = r;
                Theme theme3 = theme;
                int i3 = innerDepth;
                break;
            } else if (type != 2 || depth2 > innerDepth || !parser.getName().equals("item")) {
                Resources resources3 = r;
                Theme theme4 = theme;
                innerDepth = innerDepth;
                i = 1;
            } else {
                TypedArray a = obtainAttributes(r, theme, attributeSet, C0113R.styleable.ColorStateListItem);
                int baseColor = a.getColor(C0113R.styleable.ColorStateListItem_android_color, -65281);
                float alphaMod = 1.0f;
                if (a.hasValue(C0113R.styleable.ColorStateListItem_android_alpha)) {
                    alphaMod = a.getFloat(C0113R.styleable.ColorStateListItem_android_alpha, 1.0f);
                } else if (a.hasValue(C0113R.styleable.ColorStateListItem_alpha)) {
                    alphaMod = a.getFloat(C0113R.styleable.ColorStateListItem_alpha, 1.0f);
                }
                a.recycle();
                int numAttrs = attrs.getAttributeCount();
                int[] stateSpec = new int[numAttrs];
                int j = 0;
                int i4 = 0;
                while (i4 < numAttrs) {
                    int innerDepth2 = innerDepth;
                    int stateResId = attributeSet.getAttributeNameResource(i4);
                    TypedArray a2 = a;
                    if (!(stateResId == 16843173 || stateResId == 16843551 || stateResId == C0113R.attr.alpha)) {
                        int j2 = j + 1;
                        stateSpec[j] = attributeSet.getAttributeBooleanValue(i4, false) ? stateResId : -stateResId;
                        j = j2;
                    }
                    i4++;
                    innerDepth = innerDepth2;
                    a = a2;
                }
                int innerDepth3 = innerDepth;
                TypedArray typedArray = a;
                int[] stateSpec2 = StateSet.trimStateSet(stateSpec, j);
                colorList = GrowingArrayUtils.append(colorList, listSize, modulateColorAlpha(baseColor, alphaMod));
                stateSpecList = (int[][]) GrowingArrayUtils.append((T[]) stateSpecList, listSize, stateSpec2);
                listSize++;
                innerDepth = innerDepth3;
                i = 1;
            }
        }
        int[] colors = new int[listSize];
        int[][] stateSpecs = new int[listSize][];
        System.arraycopy(colorList, 0, colors, 0, listSize);
        System.arraycopy(stateSpecList, 0, stateSpecs, 0, listSize);
        return new ColorStateList(stateSpecs, colors);
    }

    private static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    @ColorInt
    private static int modulateColorAlpha(@ColorInt int color, @FloatRange(from = 0.0d, mo37to = 1.0d) float alphaMod) {
        return (16777215 & color) | (Math.round(((float) Color.alpha(color)) * alphaMod) << 24);
    }
}
