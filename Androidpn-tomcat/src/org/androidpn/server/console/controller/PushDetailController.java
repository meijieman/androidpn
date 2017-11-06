package org.androidpn.server.console.controller;

import org.androidpn.server.model.PushDetail;
import org.androidpn.server.service.NotificationService;
import org.androidpn.server.service.PushDetailService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PushDetailController extends MultiActionController {

    private NotificationService mNotificationService;
    private UserService mUserService;
    private PushDetailService  mPushDetailService;


    public PushDetailController(){
        mNotificationService = ServiceLocator.getNotificationService();
        mUserService = ServiceLocator.getUserService();
        mPushDetailService = ServiceLocator.getPushDetailService();
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView mav = new ModelAndView();

        List<PushDetail> pushDetailList = mPushDetailService.getPushDetails();

        for (PushDetail pushDetail : pushDetailList) {
            pushDetail.setAlias(mUserService.getUserByUsername(pushDetail.getUsername()).getAlias());
            pushDetail.setTitle(mNotificationService.getNotificationByUuid(pushDetail.getUuid()).getTitle());
        }
        mav.addObject("pushdetaillist", pushDetailList);
        mav.setViewName("pushdetail/list");
        return mav;
    }
}
