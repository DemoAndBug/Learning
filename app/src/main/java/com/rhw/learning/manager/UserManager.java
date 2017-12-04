package com.rhw.learning.manager;

import com.rhw.learning.module.user.User;

/**
 * Author:renhongwei
 * Date:2017/12/4 on 12:59
 * Function:单例管理登陆用户信息
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

    /**
     * init the user
     *
     * @param user
     */
    public void setUser(User user) {

        this.user = user;
    }

    public boolean hasLogined() {

        return user == null ? false : true;
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
     * remove the user info
     */
    public void removeUser() {

        this.user = null;
    }

}
