package com.guli.ucenter.controller;

import com.google.gson.Gson;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.Result;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.utils.ConstantPropertiesUtil;
import com.guli.ucenter.utils.HttpClientUtils;
import com.guli.ucenter.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/ucenter/wx/")
@Api(description = "微信登录中心")
public class WxApiController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    @ApiOperation(value = "登录：返回二维码")
    public String login(){

        //如果咱们访问网站的登陆帮我们重定向到微信登陆了；微信登陆成后回调我们的方法，拿着state校验
        String url="https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String state = "huaan";
        // 回调地址
        //获取业务服务器重定向地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(2022,"URL转码异常");
        }
        String urlPath = String.format(  //一段字符串的中间某一部分是需要可变 String.format()
                url,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state
        );
        return "redirect:"+urlPath;
    }

    /**
     * 假设Ngrok代理过来了，然后咱们拿到code进行获取用户信息
     * @param code
     * @param state
     * @return
     */
    //调用咱们的回调方法后，此时校验state
    //http://0722.free.idcfengye.com(localhost:8004)/api/ucenter/wx/callback
    @GetMapping("callback")
    @ApiOperation(value = "获取微信用户openId")
    public String callback(String code, String state){
        System.err.println("code:" + code);
        System.err.println("state" + state);
        //业务处理
        //既然拿到这个code了，咱们就可以根据这个code获取微信用户openId了；
        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String urlPath = String.format(
                url,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(urlPath);
            System.out.println(result);
        } catch (Exception e) {
            throw new GuliException(20023,"通过Code获取ACCESS_TOKEN失败");
        }

        Gson gson = new Gson();
        //把返回的json数据转成Map对象
        Map<String, Object> resultMap = gson.fromJson(result, HashMap.class);
        if (resultMap.get("errcode") != null) {
            throw new GuliException(20024,"获取Token返回数据失败");
        }
        String access_token = (String)resultMap.get("access_token");
        String openid = (String)resultMap.get("openid"); //维系开放用户唯一标示
        //保存数据库，首先判断数据库中是否有这个openId（微信用户）
        Member member = memberService.getMemberByOpenId(openid);

        if(member == null){
            //向微信开发平台要此用户的基本信息
            //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
            String urlUcenter = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String urlUcenterPath = String.format(urlUcenter, access_token, openid);
            String resultUcenter = null;
            try {
                resultUcenter = HttpClientUtils.get(urlUcenterPath);
                System.out.println(resultUcenter);
            } catch (Exception e) {
                throw new GuliException(20023,"获取用户信息失败");
            }

            //把返回的json数据转成Map对象
            Map<String, Object> resultUcenterMap = gson.fromJson(resultUcenter, HashMap.class);
            if (resultUcenterMap.get("errcode") != null) {
                throw new GuliException(20024,"获取用户信息失败");
            }
            String nickname = (String)resultUcenterMap.get("nickname");
            String headimgurl = (String)resultUcenterMap.get("headimgurl");
            member = new Member();
            //添加到数据库
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }
        //把member对象的开放数据传递给首页/前端
        //member：{id, nickName, avatar}
        String token = JwtUtils.generateJWT(member);
        //使用JWT： 把对象数据根据一个Key来转成token字符串
        return "redirect:http://localhost:3000?token="+token;

    }

    @GetMapping("getMemberByToken/{token}")
    @ResponseBody
    @ApiOperation(value = "根据token获取用户信息")
    public Result getMemberByToken(@PathVariable("token") String token){
        Claims claims = JwtUtils.checkJWT(token);
        String id = (String)claims.get("id");
        String nickname = (String)claims.get("nickname");
        String avatar = (String)claims.get("avatar");
        Member member = new Member();
        member.setAvatar(avatar);
        member.setId(id);
        member.setNickname(nickname);
        return Result.ok().data("member", member);
    }
}
