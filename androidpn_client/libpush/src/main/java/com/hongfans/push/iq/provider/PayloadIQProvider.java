package com.hongfans.push.iq.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/11/5 15:00
 */
public class PayloadIQProvider implements IQProvider{

    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception{
        return null;
    }
}
