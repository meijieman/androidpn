/*
 * Copyright (C) 2010 Moduad Co., Ltd.
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
package com.hongfans.push;

/**
 * Static constants for this package.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class Constants{

    public static final String SHARED_PREFERENCE_NAME = "client_preferences";

    // PREFERENCE KEYS

    public static final String API_KEY = "API_KEY";

    public static final String VERSION = "VERSION";

    public static final String XMPP_HOST = "XMPP_HOST";

    public static final String XMPP_PORT = "XMPP_PORT";

    public static final String XMPP_USERNAME = "XMPP_USERNAME";

    public static final String XMPP_PASSWORD = "XMPP_PASSWORD";

    // public static final String USER_KEY = "USER_KEY";

//    public static final String DEVICE_ID = "DEVICE_ID"; // push　认为的设备id

    public static final String EMULATOR_DEVICE_ID = "EMULATOR_DEVICE_ID";

    public static final String CLIENT_DEVICE_ID = "CLIENT_DEVICE_ID"; // 应用端设置的设备id

    public static final String INTENT_SERVICE_NAME = "INTENT_SERVICE_NAME"; // 应用端设置的回调

    public static final String NOTIFICATION_ICON = "NOTIFICATION_ICON";

    public static final String SETTINGS_NOTIFICATION_ENABLED = "SETTINGS_NOTIFICATION_ENABLED";

    public static final String SETTINGS_SOUND_ENABLED = "SETTINGS_SOUND_ENABLED";

    public static final String SETTINGS_VIBRATE_ENABLED = "SETTINGS_VIBRATE_ENABLED";

    public static final String SETTINGS_TOAST_ENABLED = "SETTINGS_TOAST_ENABLED";
    public static final String SETTINGS_AUTO_START = "SETTINGS_AUTO_START";

    // NOTIFICATION FIELDS

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";

    public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";

    public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";

    public static final String NOTIFICATION_URI = "NOTIFICATION_URI";

    // INTENT ACTIONS

    public static final String ACTION_SHOW_NOTIFICATION = "org.androidpn.client.SHOW_NOTIFICATION";

}
