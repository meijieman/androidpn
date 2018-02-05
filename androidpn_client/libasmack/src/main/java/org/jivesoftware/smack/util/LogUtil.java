/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smack.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志统一处理
 */
public class LogUtil{

    public static final String TAG = "tag_push";

    public static boolean mIsDebug = true;

    // 01-06 20:04:48.730
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    public static String makeLogTag(Class cls){
        return TAG + cls.getSimpleName();
    }

    public static void i(Object msg){
        if(mIsDebug){
            Log.i(TAG, "" + msg);
        }
        if(sListener != null){
            sListener.log("i", msg, getTimestamp());
        }
    }

    public static void d(Object msg){
        if(mIsDebug){
            Log.d(TAG, "" + msg);
        }
        if(sListener != null){
            sListener.log("d", msg, getTimestamp());
        }
    }

    public static void w(Object msg){
        if(mIsDebug){
            Log.w(TAG, "" + msg);
        }
        if(sListener != null){
            sListener.log("w", msg, getTimestamp());
        }
    }

    public static void e(Object msg){
        if(mIsDebug){
            Log.e(TAG, "" + msg);
        }
        if(sListener != null){
            sListener.log("e", msg, getTimestamp());
        }
    }

    private static String getTimestamp(){
        return sdf.format(new Date());
    }

    private static LogListener sListener;

    public static void setLogListener(LogListener listener){
        sListener = listener;
    }

    public interface LogListener{

        void log(String level, Object msg, String timestamp);
    }

}
