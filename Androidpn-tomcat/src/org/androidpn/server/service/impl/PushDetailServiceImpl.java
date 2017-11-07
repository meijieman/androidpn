package org.androidpn.server.service.impl;

import org.androidpn.server.dao.PushDetailDao;
import org.androidpn.server.model.PushDetail;
import org.androidpn.server.service.PushDetailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class PushDetailServiceImpl implements PushDetailService {

    protected final Log log = LogFactory.getLog(getClass());

    PushDetailDao dao;

    public void setPushDetailDao(PushDetailDao dao) {
        this.dao = dao;
    }

    @Override
    public PushDetail savePushDetail(PushDetail pd) {
        return dao.savePushDetail(pd);
    }

    @Override
    public List<PushDetail> getPushDetails() {
        return dao.getPushDetails();
    }

    @Override
    public PushDetail getPushDetail(String username, String UUID) {
        log.debug("tag_route 根据 username 和 uuid 获取发送的消息 " + dao.getPushDetail(username, UUID) + "\n UUID " + UUID + ", username " + username);
        return dao.getPushDetail(username, UUID);
    }

    @Override
    public List<PushDetail> getPushDetailsThatFailureByUsername(String username) {
        return dao.getPushDetailsThatFailureByUsername(username);
    }
}
