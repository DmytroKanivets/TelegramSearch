package com.kpi.bot.services.loader.telegram;

import com.kpi.bot.services.loader.telegram.structure.Channel;
import com.kpi.bot.services.loader.telegram.structure.User;
import com.kpi.bot.utils.StringUtils;
import org.telegram.api.chat.TLAbsChat;
import org.telegram.api.chat.channel.TLChannel;
import org.telegram.api.user.TLAbsUser;
import org.telegram.api.user.TLUser;

public class TelegramConverter {

    public static User convertUser(TLAbsUser user) {
        if (user instanceof TLUser) {
            return convertUser((TLUser) user);
        } else {
            return null;
        }
    }

    public static User convertUser(TLUser tlUser) {
        User user = new User();

        user.setId(String.valueOf(tlUser.getId()));
        user.setHash(String.valueOf(tlUser.getAccessHash()));

        String userName = null;
        if (StringUtils.notEmpty(tlUser.getFirstName()) && StringUtils.notEmpty(tlUser.getLastName())) {
            userName = tlUser.getFirstName() + " " + tlUser.getLastName();
        } else if (StringUtils.notEmpty(tlUser.getFirstName())) {
            userName = tlUser.getFirstName();
        } else if (StringUtils.notEmpty(tlUser.getLastName())) {
            userName = tlUser.getLastName();
        } else if (StringUtils.notEmpty(tlUser.getUserName())) {
            userName = tlUser.getUserName();
        }
        user.setUserName(userName);

        return user;

    }

    public static Channel convertChannel(TLAbsChat chat) {
        if (chat instanceof TLChannel) {
            return convertChannel((TLChannel) chat);
        } else {
            System.err.println("Other type than channel are not supported: " + chat.toString());
            return null;
        }
    }

    public static Channel convertChannel(TLChannel chat) {
        Channel channel = new Channel();
        channel.setId(String.valueOf(chat.getId()));
        channel.setHash(String.valueOf(chat.getAccessHash()));
        channel.setName(chat.getTitle());
        return channel;
    }
}
