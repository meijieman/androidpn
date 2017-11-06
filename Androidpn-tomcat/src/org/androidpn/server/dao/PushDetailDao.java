package org.androidpn.server.dao;

import org.androidpn.server.model.PushDetail;

import java.util.List;

public interface PushDetailDao {

    PushDetail savePushDetail(PushDetail pd);

    List<PushDetail> getPushDetails();

    PushDetail getPushDetail(String username, String UUID);
}
