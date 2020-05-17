package conmunity.conmunity.controller;


import conmunity.conmunity.dto.AccessTokenDTO;
import conmunity.conmunity.dto.GitHunUser;
import conmunity.conmunity.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.clentid}")
    private String clentid;

    @Value("${github.clentsecret}")
    private String clentsecret;

    @Value("${github.redirect_uri}")
    private String redirect_uri;


    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           HttpServletRequest request,
                           Model model){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setClient_id(clentid);
        accessTokenDTO.setClient_secret(clentsecret);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GitHunUser user = githubProvider.getUser(accessToken);

        if(user != null){
            //登陆成功，写cookie和 session
            request.getSession().setAttribute("user",user);
            return "redirect:/index";//重定向到 首页

        }else{
            return "redirect:/index";
        }
    }





}
