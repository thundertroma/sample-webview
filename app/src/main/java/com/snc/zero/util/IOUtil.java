package com.snc.zero.util;

import com.snc.zero.log.Logger;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO 유틸리티
 * @author Aaron Jo.
 * @since 2016
 */
public class IOUtil {
    private static final String TAG = IOUtil.class.getSimpleName();

    public static void closeQuietly(InputStream input) {
        try {
            if (null != input) {
                input.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public static void closeQuietly(OutputStream output) {
        try {
            if (null != output) {
                output.flush();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        try {
            if (null != output) {
                output.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

}
