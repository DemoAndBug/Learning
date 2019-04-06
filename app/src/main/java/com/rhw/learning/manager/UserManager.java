package com.rhw.learning.manager;

import com.rhw.learning.module.user.User;

/**
 * Date:2017/12/4 on 12:59
 * Function:单例管理登陆用户信息
 * @author Simon
 */
public class UserManager {
    private static UserManager userManager = null;
    private User user = null;

    public static UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
                return userManager;
            }
        } else {
            return userManager;
        }
    }

    public boolean hasLogin() {
        return user != null;
    }

    /**
     * has user info
     *
     * @return
     */
    public User getUser() {
        return this.user;
    }

    /**
     * init the user
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * remove the user info
     */
    public void removeUser() {
        this.user = null;
    }

}
