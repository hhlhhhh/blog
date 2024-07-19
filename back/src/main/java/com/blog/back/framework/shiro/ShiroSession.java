package com.blog.back.framework.shiro;

import com.blog.back.framework.shiro.profile.ShiroBaseProfile;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Component
public class ShiroSession {

    @Autowired
    RedisSessionDAO sessionDAO;

    public Session getSessionByUserId(Integer uid) {
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        ShiroBaseProfile user;
        Object attribute;
        for (Session session : sessions) {
            attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            user = (ShiroBaseProfile) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                continue;
            }
            if (user.getId().equals(uid)) {
                return session;
            }
        }
        return null;
    }

    public void deleteSession(Session session){
        sessionDAO.delete(session);
    }

    public void deleteSession(Integer uid){
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        ShiroBaseProfile user;
        Object attribute;
        for (Session session : sessions) {
            attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            user = (ShiroBaseProfile) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                continue;
            }
            if (user.getId().equals(uid)) {
                sessionDAO.delete(session);
                return;
            }
        }
    }

    //获取sessionList
    public List<Session> getSessionList(Long current,Long size){
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        List<Session> sessionList = new ArrayList<>();
        long i = 0,left = (current-1)*size,right = current*size;
        Iterator<Session> iterator = sessions.iterator();
        while(iterator.hasNext()&&i++<left){
            iterator.next();
        }
        while(iterator.hasNext()&&i++<right){
            sessionList.add(iterator.next());
        }
        return sessionList;
    }

    //获取在线人数
    public Integer getOnlineCount(){
        return sessionDAO.getActiveSessions().size();
    }

    //获取ShiroBaseProfileList
    public List<ShiroBaseProfile> getShiroBaseProfileList(Long current,Long size){
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        List<ShiroBaseProfile> sessionList = new ArrayList<>();
        long i = 0,left = (current-1)*size,right = current*size;
        Iterator<Session> iterator = sessions.iterator();
        while(iterator.hasNext()&&i++<left){
            iterator.next();
        }
        while(iterator.hasNext()&&i++<=right){
            Session session = iterator.next();
            Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                sessionDAO.delete(session);
                continue;
            }
            ShiroBaseProfile user = (ShiroBaseProfile) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                sessionDAO.delete(session);
                continue;
            }

            user.setPassword(null);
            sessionList.add(user);
        }
        return sessionList;
    }

    //获取指定账号的session
    public Session getSessionByAccount(String account){
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        ShiroBaseProfile user;
        Object attribute;
        for (Session session : sessions) {
            attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            user = (ShiroBaseProfile) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                continue;
            }
            if (user.getAccount().equals(account)) {
                return session;
            }
        }
        return null;
    }


}
