package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.PasswordExpiredHandlePolicy;
import cn.chenyunlong.qing.auth.domain.user.port.SecurityPolicyPort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

@Service
public class SecurityPolicyService implements SecurityPolicyPort {

    private static final Integer DEFAULT_MAX_LOGIN_ATTEMPTS = 5;

    public PasswordExpiredHandlePolicy getPasswordExpiredPolicy() {
        LinkedList<String> list = new LinkedList<>();

        list.add("1");
        System.out.println("list = " + list);

        ListIterator<String> stringListIterator = list.listIterator();

        if (stringListIterator.hasPrevious()) {
            stringListIterator.previous();

            if (stringListIterator.hasNext()) {
                stringListIterator.next();
            }
        }

        String s = list.pollLast();
        list.add(1, "2");
        boolean offer = list.offer("2");
        if (offer) {
            list.offer("3");
        }

        Stack<String> stack = new Stack<>();

        return PasswordExpiredHandlePolicy.FAIL;
    }

    @Override
    public int getMaxLoginAttempts() {
        return DEFAULT_MAX_LOGIN_ATTEMPTS;
    }

    @Override
    public boolean isIpAllowed(IpAddress ipAddress) {
        return true;
    }

    public boolean isLoginTimeAllowed() {
        return true;
    }

    public boolean isDeviceAllowed(String userAgent) {
        return true;
    }

}
