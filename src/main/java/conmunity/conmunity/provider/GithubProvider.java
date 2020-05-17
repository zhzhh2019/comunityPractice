package conmunity.conmunity.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import conmunity.conmunity.dto.AccessTokenDTO;
import conmunity.conmunity.dto.GitHunUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Component 会自动注入到spring的 上下文 也就是 ioc  控制反转
 *
 * */

@Component
public class GithubProvider {

    public  static  final MediaType MediaTypeJSON = MediaType.get("application/json; charset=utf-8");
    //POST https://github.com/login/oauth/access_token

    public String getAccessToken(AccessTokenDTO accessTokenDTO){



        OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), MediaTypeJSON);
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String token =  response.body().string();
                //token:access_token=d14101a1307ba1bb32027ae9a00081fb8f8fae29&scope=&token_type=bearer
                String accessToken = token.split("&")[0].split("=")[1];
                System.err.println("token:"+token);
                System.err.println("accessToken:"+accessToken);
                return accessToken;
            } catch (IOException e) {
                e.printStackTrace();
            }

        return "";
    }

    public GitHunUser getUser(String accessToken){

        OkHttpClient client = new OkHttpClient();
        //RequestBody body = RequestBody.create(json, MediaTypeJSON);
        Request request = new Request.Builder()
                .header("Authorization","token "+accessToken)
                .url("https://api.github.com/user?access_token")
               .build();
        try (Response response = client.newCall(request).execute()) {

            String string = response.body().string();

            GitHunUser user = JSON.parseObject(string,GitHunUser.class);
            System.err.println("githubusername:"+user.getName());
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }



}
