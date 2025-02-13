package service.user;

import pojo.User;

public interface UserService {

    //用户登录
    public User getUser(String account, String pwd);

    //根据用户账户修改密码
    public boolean updatePwd(String account, String oldPwd, String newPwd);

    //注册新用户
    public boolean insertUser(String account, String password);

    //保存当前歌曲信息和播放模式
    public boolean updateMusic(String account, int musicId, int pattern);
}
