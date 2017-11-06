package org.androidpn.server.service;

import org.androidpn.server.model.PushDetail;

import java.util.List;

public interface PushDetailService {

    PushDetail savePushDetail(PushDetail pd);

    List<PushDetail> getPushDetails();

    PushDetail getPushDetail(String username, String UUID);

}
