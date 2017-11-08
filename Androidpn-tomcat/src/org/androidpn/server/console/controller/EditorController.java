package org.androidpn.server.console.controller;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.util.CommonUtil;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditorController extends MultiActionController {


    private UserService userService;

    public EditorController() {
        userService = ServiceLocator.getUserService();
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("editor/editor");
        return mav;
    }

    public ModelAndView add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        String tagName = ServletRequestUtils.getStringParameter(request, "tag_name", "");
        mav.addObject("tag_name", tagName);
        List<User> users = userService.getUsersByTag(tagName);
        if (CommonUtil.isEmpty(users)) {
            mav.addObject("message", "未查询到指定标签用户");
        } else {
            mav.addObject("userList", users);
        }

        mav.setViewName("editor/editor");
        return mav;
    }

}
