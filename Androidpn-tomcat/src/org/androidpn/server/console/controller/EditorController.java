package org.androidpn.server.console.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditorController extends MultiActionController {

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("editor/editor");
        return mav;
    }

}
