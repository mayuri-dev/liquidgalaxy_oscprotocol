package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import androidx.core.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(21)
@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi21Impl";
    private static Method sAddFontWeightStyle;
    private static Method sCreateFromFamiliesWithDefault;
    private static Class sFontFamily;
    private static Constructor sFontFamilyCtor;
    private static boolean sHasInitBeenCalled = false;

    TypefaceCompatApi21Impl() {
    }

    private static void init() {
        Method addFontMethod;
        Constructor fontFamilyCtor;
        Class fontFamilyClass;
        Method createFromFamiliesWithDefaultMethod;
        if (!sHasInitBeenCalled) {
            sHasInitBeenCalled = true;
            try {
                fontFamilyClass = Class.forName(FONT_FAMILY_CLASS);
                fontFamilyCtor = fontFamilyClass.getConstructor(new Class[0]);
                addFontMethod = fontFamilyClass.getMethod(ADD_FONT_WEIGHT_STYLE_METHOD, new Class[]{String.class, Integer.TYPE, Boolean.TYPE});
                Object familyArray = Array.newInstance(fontFamilyClass, 1);
                createFromFamiliesWithDefaultMethod = Typeface.class.getMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{familyArray.getClass()});
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                Log.e(TAG, e.getClass().getName(), e);
                fontFamilyClass = null;
                fontFamilyCtor = null;
                addFontMethod = null;
                createFromFamiliesWithDefaultMethod = null;
            }
            sFontFamilyCtor = fontFamilyCtor;
            sFontFamily = fontFamilyClass;
            sAddFontWeightStyle = addFontMethod;
            sCreateFromFamiliesWithDefault = createFromFamiliesWithDefaultMethod;
        }
    }

    private File getFile(@NonNull ParcelFileDescriptor fd) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(fd.getFd());
            String path = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(path).st_mode)) {
                return new File(path);
            }
            return null;
        } catch (ErrnoException e) {
            return null;
        }
    }

    private static Object newFamily() {
        init();
        try {
            return sFontFamilyCtor.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Typeface createFromFamiliesWithDefault(Object family) {
        init();
        try {
            Object familyArray = Array.newInstance(sFontFamily, 1);
            Array.set(familyArray, 0, family);
            return (Typeface) sCreateFromFamiliesWithDefault.invoke(null, new Object[]{familyArray});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean addFontWeightStyle(Object family, String name, int weight, boolean style) {
        init();
        try {
            return ((Boolean) sAddFontWeightStyle.invoke(family, new Object[]{name, Integer.valueOf(weight), Boolean.valueOf(style)})).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0068, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0069, code lost:
        r5 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x006d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x006e, code lost:
        r9 = r5;
        r5 = r4;
        r4 = r9;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0068 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:10:0x0023] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r11, android.os.CancellationSignal r12, @androidx.annotation.NonNull androidx.core.provider.FontsContractCompat.FontInfo[] r13, int r14) {
        /*
            r10 = this;
            int r0 = r13.length
            r1 = 0
            r2 = 1
            if (r0 >= r2) goto L_0x0006
            return r1
        L_0x0006:
            androidx.core.provider.FontsContractCompat$FontInfo r0 = r10.findBestInfo(r13, r14)
            android.content.ContentResolver r2 = r11.getContentResolver()
            android.net.Uri r3 = r0.getUri()     // Catch:{ IOException -> 0x0082 }
            java.lang.String r4 = "r"
            android.os.ParcelFileDescriptor r3 = r2.openFileDescriptor(r3, r4, r12)     // Catch:{ IOException -> 0x0082 }
            if (r3 != 0) goto L_0x0023
            if (r3 == 0) goto L_0x0022
            r3.close()     // Catch:{ IOException -> 0x0082 }
        L_0x0022:
            return r1
        L_0x0023:
            java.io.File r4 = r10.getFile(r3)     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            if (r4 == 0) goto L_0x003a
            boolean r5 = r4.canRead()     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            if (r5 != 0) goto L_0x0030
            goto L_0x003a
        L_0x0030:
            android.graphics.Typeface r5 = android.graphics.Typeface.createFromFile(r4)     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            if (r3 == 0) goto L_0x0039
            r3.close()     // Catch:{ IOException -> 0x0082 }
        L_0x0039:
            return r5
        L_0x003a:
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            java.io.FileDescriptor r6 = r3.getFileDescriptor()     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            r5.<init>(r6)     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            android.graphics.Typeface r6 = super.createFromInputStream(r11, r5)     // Catch:{ Throwable -> 0x0053, all -> 0x0050 }
            r5.close()     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            if (r3 == 0) goto L_0x004f
            r3.close()     // Catch:{ IOException -> 0x0082 }
        L_0x004f:
            return r6
        L_0x0050:
            r6 = move-exception
            r7 = r1
            goto L_0x0059
        L_0x0053:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x0055 }
        L_0x0055:
            r7 = move-exception
            r9 = r7
            r7 = r6
            r6 = r9
        L_0x0059:
            if (r7 == 0) goto L_0x0064
            r5.close()     // Catch:{ Throwable -> 0x005f, all -> 0x0068 }
            goto L_0x0067
        L_0x005f:
            r8 = move-exception
            r7.addSuppressed(r8)     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
            goto L_0x0067
        L_0x0064:
            r5.close()     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
        L_0x0067:
            throw r6     // Catch:{ Throwable -> 0x006b, all -> 0x0068 }
        L_0x0068:
            r4 = move-exception
            r5 = r1
            goto L_0x0071
        L_0x006b:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x006d }
        L_0x006d:
            r5 = move-exception
            r9 = r5
            r5 = r4
            r4 = r9
        L_0x0071:
            if (r3 == 0) goto L_0x0081
            if (r5 == 0) goto L_0x007e
            r3.close()     // Catch:{ Throwable -> 0x0079 }
            goto L_0x0081
        L_0x0079:
            r6 = move-exception
            r5.addSuppressed(r6)     // Catch:{ IOException -> 0x0082 }
            goto L_0x0081
        L_0x007e:
            r3.close()     // Catch:{ IOException -> 0x0082 }
        L_0x0081:
            throw r4     // Catch:{ IOException -> 0x0082 }
        L_0x0082:
            r3 = move-exception
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatApi21Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, androidx.core.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry entry, Resources resources, int style) {
        Object family = newFamily();
        FontFileResourceEntry[] entries = entry.getEntries();
        int length = entries.length;
        int i = 0;
        while (i < length) {
            FontFileResourceEntry e = entries[i];
            File tmpFile = TypefaceCompatUtil.getTempFile(context);
            if (tmpFile == null) {
                return null;
            }
            try {
                if (!TypefaceCompatUtil.copyToFile(tmpFile, resources, e.getResourceId())) {
                    tmpFile.delete();
                    return null;
                } else if (!addFontWeightStyle(family, tmpFile.getPath(), e.getWeight(), e.isItalic())) {
                    return null;
                } else {
                    tmpFile.delete();
                    i++;
                }
            } catch (RuntimeException e2) {
                return null;
            } finally {
                tmpFile.delete();
            }
        }
        return createFromFamiliesWithDefault(family);
    }
}
