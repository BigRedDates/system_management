package tju.wbllab.system_management.ctr;


import tju.wbllab.system_management.dao.model.User;

import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SessionListener implements HttpSessionListener,HttpSessionAttributeListener {
    //保存sessionId和user的映射key-sessionId
	private static Map<String,User> userMap = new HashMap<>();
	private static Map<String,HttpSession> sessionMap = new HashMap<String,HttpSession>();
	public SessionListener(){
		
	}
	public void removeSessionMap(String sessionId){
		sessionMap.remove(sessionId);
		//保持两个map的一致性
		Map<String,User> tempMap = new HashMap<>();
		tempMap.putAll(userMap);
		Iterator<Entry<String,User>> iter = tempMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, User> entry=iter.next();
			User userBean=entry.getValue();
			if(sessionId.equals(userBean.getSessionId())){
				//userMap也删除相应用户，有可能同一浏览器登陆两个用户，造成session混乱，故以sessionid为判断标准来同步
				userMap.remove(userBean.getStudentID());
			}
		}
	} 
	public int getOnlineUserNum(){
		System.out.println("==================获取在线用户userMap大小为 "+userMap.size());
		System.out.println("==================获取在线用户sessionMap大小为 "+sessionMap.size());  
		System.out.println("==================获取在线用户");
		output();
		return sessionMap.size(); 
	} 
	/*以下是实现HttpSessionListener中的方法*/
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		String sessionId=arg0.getSession().getId();
		User userBean=(User)arg0.getSession().getAttribute("user");
		if(null==userBean){
			return;
		}
		String studentID=userBean.getStudentID();
		userMap.remove(studentID);
		sessionMap.remove(sessionId);
		 
		System.out.println("==================sessionDestroyed");
		output();
	}
	/*以下是实现HttpSessionAttributeListener中的方法*/
	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		if("user".equals(arg0.getName())){
			User userBean=(User)arg0.getSession().getAttribute("user");
			String sessionId=arg0.getSession().getId();
			String studentID=userBean.getStudentID();
			userBean.setSessionId(sessionId);
			//踢掉已存在用户
			User ub1=userMap.get(studentID);//已经存在的用户
			if(null!=ub1&&ub1.getStudentID().equals(userBean.getStudentID())){
				singleLogin(ub1.getId());
			}
			userMap.put(studentID, userBean);
			sessionMap.put(sessionId, arg0.getSession());
		} 
		System.out.println("==================attributeAdded");
		output();
	}
    //踢掉之前的用户
	public static void singleLogin(int userId){
		clearUserById(userId);
	}
	//清空同一在线用户的session
	public static void clearUserById(int userId){
		Map<String,User> tempMap = new HashMap<>();
		tempMap.putAll(userMap);
		Iterator<Entry<String,User>> iter = tempMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, User> entry=iter.next();
			User userBean=entry.getValue();
			if(userId==userBean.getId()){
				String sessionId=userBean.getSessionId();
				HttpSession session=sessionMap.get(sessionId);
				session.removeAttribute("user");
				session.invalidate();
				sessionMap.remove(sessionId);
				userMap.remove(userBean.getStudentID());
			}
		} 
		System.out.println("==================clearUserById");
		output();
	}
	//用户退出时的监听
	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		
	}
	public static void output(){
		Iterator<Entry<String,User>> iter = userMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, User> entry=iter.next();
			User userBean=entry.getValue();
			System.out.println("=================="+userBean.getStudentID());
		}
		Iterator<Entry<String,HttpSession>> iters = sessionMap.entrySet().iterator();
		while(iters.hasNext()){
			Entry<String, HttpSession> entry=iters.next();
			HttpSession sessionBean=entry.getValue();
			System.out.println("=================="+sessionBean.getId());
		}
	}
}
