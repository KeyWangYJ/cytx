package com.cytx.controller;

import com.cytx.pojo.User;
import com.cytx.service.UserService;
import com.cytx.utils.GetMessageCode;
import com.cytx.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.*;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
     UserService userService;

    /**
     * 用户登录
     * @param model
     * @param request
     * @param session
     * @param response
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String userLogin(Model model,HttpServletRequest request, HttpSession session, HttpServletResponse response, User user){
        String inputVerifyCode=request.getParameter("verifyCode");
        String verifyCodeValue=(String) session.getAttribute("verifyCodeValue");
        if(verifyCodeValue.equals(inputVerifyCode.toUpperCase())){
            user.setUserPassword(user.getUserPassword());
            User u = userService.confirmUser(user);
            if(u==null){
                model.addAttribute("errorInfo","用户名或密码错误！");
                return "user/login";
            }else {
                if (u.getUserId() != null && !"".equals(u.getUserId())) {
                    //登录成功
                    //***************判断用户是否勾选了自动登录*****************
                    String autoLogin = request.getParameter("autoLogin");
                    if("autoLogin".equals(autoLogin)){
                        //要自动登录
                        //创建存储用户名的cookie
                        Cookie cookie_username = new Cookie("cookie_username",user.getUserName());
                        cookie_username.setMaxAge(10*60);
                        //创建存储密码的cookie
                        Cookie cookie_password = new Cookie("cookie_password",user.getUserPassword());
                        cookie_password.setMaxAge(10*60);
                        response.addCookie(cookie_username);
                        response.addCookie(cookie_password);
                    }
                    //将user对象存到session中
                    session.setAttribute("user", u);
                    return "redirect:/user";
                }
                model.addAttribute("errorInfo","用户名id为空！");
                return "user/login";
            }
        }else{
            model.addAttribute("errorInfo","验证码错误！");
            return "user/login";
        }
    }

    /**
     * 短信登录
     */
    @RequestMapping(value = "/sendSMS", method = RequestMethod.POST)
    public void SendSms(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        String phone=req.getParameter("phonenum");
        //根据获取到的手机号发送验证码
        String code= GetMessageCode.getCode(phone);
        System.out.println(code);
        resp.getWriter().print(code);
    }
    @RequestMapping(value = "/phoneIsRegister",method = RequestMethod.POST)
    public boolean PhoneIsRegister(HttpServletRequest req,HttpServletResponse resp){
        String phone=req.getParameter("phonenum");
        Boolean isRegister = userService.phoneIsExist(phone);
        return isRegister;

    }
}
