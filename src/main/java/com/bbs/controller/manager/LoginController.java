package com.bbs.controller.manager;

import com.bbs.controller.BaseController;
import com.bbs.mybatis.model.User;
import com.bbs.util.ApiJsonResult;
import com.bbs.util.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lihongde on 2016/9/8 15:42
 */
@Controller
public class LoginController extends BaseController{

    /**
     * @return
     */
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(String username, String password) {
        ModelAndView mav = new ModelAndView();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            mav.setViewName("index");
            mav.addObject("error", "用户名/密码不能为空");
            return mav;
        }
        if(!username.equals("admin")){
            mav.setViewName("index");
            mav.addObject("error", "用户名输入错误");
            return mav;
        }

        User user = userService.findUserById(Constants.ADMIN_USER_ID);
        String md5Pwd = DigestUtils.md5Hex(password);

        if(!md5Pwd.equals(user.getOpenid())){
            mav.setViewName("index");
            mav.addObject("error", "密码输入错误");
            return mav;
        }
        try {
            mav.setViewName("system/home");
            return mav;
        } catch (RuntimeException e) {
            mav.setViewName("index");
            mav.addObject("error", "未知错误，请联系管理员");
            return mav;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "index";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String modifyPwd(){
        return "system/password";
    }

    @RequestMapping(value = "/password/save", method = RequestMethod.POST)
    @ResponseBody
    public ApiJsonResult save(String password, String newPwd, String confirmPwd){
        User user = userService.findUserById(Constants.ADMIN_USER_ID);
        String pwd = DigestUtils.md5Hex(password);
        if(!pwd.equals(user.getOpenid())){
            return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "原密码输入错误");
        }
        if(!newPwd.equals(confirmPwd)){
            return new ApiJsonResult(Constants.JSON_RESULT.BAD_REQUEST, "两次输入的密码不一致");
        }

        user.setOpenid(DigestUtils.md5Hex(newPwd));
        userService.update(user);
        return new ApiJsonResult(Constants.JSON_RESULT.OK);
    }
}
