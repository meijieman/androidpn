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

import com.hongfans.push.util.LogUtil;

import org.jivesoftware.smack.ConnectionListener;

/**
 * A listener class for monitoring connection closing and reconnection events.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class PersistentConnectionListener implements ConnectionListener{

    private final XmppManager xmppManager;

    public PersistentConnectionListener(XmppManager xmppManager){
        this.xmppManager = xmppManager;
    }

    @Override
    public void connectionClosed(){
        LogUtil.d("connectionClosed()...");
    }

    @Override
    public void connectionClosedOnError(Exception e){
        LogUtil.d("connectionClosedOnError()...");
        if(xmppManager.getConnection() != null
           && xmppManager.getConnection().isConnected()){
            xmppManager.getConnection().disconnect();
        }
        xmppManager.startReconnectionThread();
    }

    @Override
    public void reconnectingIn(int seconds){
        LogUtil.d("reconnectingIn()...");
    }

    @Override
    public void reconnectionSuccessful(){
        LogUtil.d("reconnectionSuccessful()...");
    }

    @Override
    public void reconnectionFailed(Exception e){
        LogUtil.d("reconnectionFailed()...");
    }

}
