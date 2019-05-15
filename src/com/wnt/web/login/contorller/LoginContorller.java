package com.wnt.web.login.contorller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.uitl.EncryptUtil;
import org.wnt.core.uitl.IaRegCheck;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.StringUtil;

import com.wnt.web.login.entity.UserEntity;
import com.wnt.web.login.service.LoginService;
import com.wnt.web.operationlog.service.OperationLogService;

@Controller
@RequestMapping("/loginContorller")
public class LoginContorller {

    static public  int maxErrorCount = 5;
    static public  int maxLockTime = 5;
    
    static private Map<String, Map<String, Object>> userLockInfoMap = new HashMap<String, Map<String, Object>>();
	static private Map<String, String> userLoginInfo = new HashMap<String, String>();
    @Resource
	private LoginService loginService;
	
    @Resource
    private OperationLogService operationLogService;
    
    static {
        Map<String, Object> adminMap = new HashMap<String, Object>();
        adminMap.put("errorCount", 0);
        adminMap.put("lockStatus", false);
        adminMap.put("lockBeginTime", null);
        userLockInfoMap.put("admin", adminMap);
        
        Map<String, Object> auditMap = new HashMap<String, Object>();
        auditMap.put("errorCount", 0);
        auditMap.put("lockStatus", false);
        auditMap.put("lockBeginTime", null);
        userLockInfoMap.put("audit", auditMap);
    }
    
	@ResponseBody  
	@RequestMapping("/login")
	public Map<String, String> login(String name , String password, String key, HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		try {		    		    		    
			UserEntity loginEntity = new UserEntity();
//			String key = request.getSession().getAttribute("key").toString();
			
			System.out.println(key);
			name = EncryptUtil.aesDecrypt(name, key);
			loginEntity.setName(name);
			if (StringUtils.isNotBlank(password)) {
				String tempPassword = EncryptUtil.aesDecrypt(password, key);				
				tempPassword = EncryptUtil.aesEncrypt(tempPassword, "dShZTODdnJqVgWCw");
				loginEntity.setPassword(tempPassword);
			}
			if(loginService.queryUser(name) == null) {
			    map.put("success", "no");
			    map.put("info", "该用户不存在");
			    operationLogService.addOperationLog(name, request, "登录", "失败", "该用户不存在");
			    return map;
			}
						
			Map<String, Object> lockInfoMap = userLockInfoMap.get(name.toLowerCase());
            if(lockInfoMap != null) {
                boolean lockStatus = (Boolean)lockInfoMap.get("lockStatus");
                if(lockStatus) {
                    Date beginTime = (Date)lockInfoMap.get("lockBeginTime");
                    Date curTime = new Date();
                    long waitTime = (curTime.getTime() - beginTime.getTime())/(1000*60);
                    if(waitTime >= maxLockTime) {
                        //解除锁定
                        lockInfoMap.put("errorCount", 0);
                        lockInfoMap.put("lockStatus", false);
                        lockInfoMap.put("lockBeginTime", null);
                    }else {
                        map.put("success", "no");
                        map.put("info", "该用户登录错误次数超限，已被锁定，还需等待" + (maxLockTime - waitTime) +"分钟");
                        operationLogService.addOperationLog(name, request, "登录", "失败", "该用户登录错误次数超限，已被锁定");
                        return map;
                    }
                }
            }
			
			boolean success = loginService.login(loginEntity);
			if (success) {
			    name = name.toLowerCase();
			    String remoteAddr = request.getRemoteAddr();
			    String loginUser = userLoginInfo.get(remoteAddr);
			    if(loginUser != null && loginUser.compareTo(name) != 0) {
			        map.put("success", "no");
	                map.put("info", "该客户端已经有其它用户登录，请注销后再登录！");
	                operationLogService.addOperationLog(name, request, "登录", "失败", "该客户端已经有其它用户登录，请注销后再登录");
			        return map;
			    }
			    
				request.getSession().setAttribute("userName", name);
				map.put("success", "yes");
				map.put("info", "登陆成功");
				lockInfoMap.put("errorCount", 0);
				operationLogService.addOperationLog(name, request, "登录", "成功", "--");
				
				
				userLoginInfo.put(remoteAddr, name);
				
			} else {
			    
			    int errorCount = (int)lockInfoMap.get("errorCount");
                errorCount++;
                				
				lockInfoMap.put("errorCount", errorCount);
				if(errorCount >= maxErrorCount) {
				    lockInfoMap.put("lockStatus", true);
				    lockInfoMap.put("lockBeginTime", new Date());
				    map.put("info", "登陆失败，用户名或密码不正确。" + "已经连续失败" + errorCount + "次," 
				                + "该用户将被锁定" + maxLockTime + "分钟");
				    operationLogService.addOperationLog(name, request, "登录", "失败", "用户名或密码不正确");
				}else {
				    map.put("success", "no");
	                map.put("info", "登陆失败，用户名或密码不正确。" + "再失败" + (maxErrorCount-errorCount)
	                        + "次，用户将被锁定。");
	                operationLogService.addOperationLog(name, request, "登录", "失败", "用户名或密码不正确");
				}
				
			} 
		} catch (Exception e) {
		    LogUtil.error("Login Error", e);
			map.put("success", "error");
			map.put("info", "登录出现异常, 请联系管理员。");
			operationLogService.addOperationLog(name, request, "登录", "失败", "登录出现异常, 请联系管理员");
		}
		return map;
	}
	
	@RequestMapping("/updateTojsp")
	public String updateTojsp(@RequestParam(value="page", defaultValue="1")int id, HttpServletRequest request){
		try {
		    UserEntity userEntity = null;
		    if(request.getSession().getAttribute("userName") != null) {
		        String userName = request.getSession().getAttribute("userName").toString();
		        userEntity = loginService.queryUser(userName);
		    }else {
    			userEntity = loginService.queryUser(id);    			
		    }
		    if (userEntity != null) {
                request.getSession().setAttribute("name", userEntity.getName());
                request.getSession().setAttribute("password", EncryptUtil.aesDecrypt(userEntity.getPassword(), "dShZTODdnJqVgWCw"));
                request.getSession().setAttribute("id", userEntity.getId());
            } 
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "user/updateUser";
	}
	
	@ResponseBody  
	@RequestMapping("/updateUserPassword")
	public Map<String, String> updateUserPassword(@RequestParam(value="page", defaultValue="1")int id, String password, String oldPassword, String key, HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		
		//String key = request.getSession().getAttribute("key").toString();
        UserEntity userEntity = null;
        if(request.getSession().getAttribute("userName") != null) {
            String userName = request.getSession().getAttribute("userName").toString();
            userEntity = loginService.queryUser(userName);
        }else {
            userEntity = loginService.queryUser(id);                
        }
        
		try {		
			if (StringUtils.isNotBlank(oldPassword)) {
				oldPassword = EncryptUtil.aesDecrypt(oldPassword, key);	
				oldPassword = EncryptUtil.aesEncrypt(oldPassword, "dShZTODdnJqVgWCw");
			}
			if(!userEntity.getPassword().equals(oldPassword) ){
				map.put("success", "no");
				map.put("info", "当前密码错误，请重新输入");
				operationLogService.addOperationLog(userEntity.getName(), request, "登录", "失败", "当前密码错误，请重新输入");
				return map;
			}
			// 加密存库
			if (StringUtils.isNotBlank(password)) {
				password = EncryptUtil.aesDecrypt(password, key);	
				password = EncryptUtil.aesEncrypt(password, "dShZTODdnJqVgWCw");
			}
			UserEntity loginEntity = new UserEntity();
			loginEntity.setId(id);
			loginEntity.setName(userEntity.getName());
			loginEntity.setPassword(password);
			boolean success = loginService.updateUserPassword(loginEntity);
			if (success) {
				map.put("success", "yes");
				map.put("info", "修改成功");
				operationLogService.addOperationLog(userEntity.getName(), request, "修改密码", "成功", "--");
			} else {
				map.put("success", "no");
				map.put("info", "修改失败，请联系管理员。");
				operationLogService.addOperationLog(userEntity.getName(), request, "修改密码", "失败", "更新数据库失败");
			} 
		} catch (Exception e) {
			map.put("success", "error");
			map.put("info", "修改出现异常，请联系管理员。");
			operationLogService.addOperationLog(userEntity.getName(), request, "修改密码", "失败", "修改出现异常,请联系管理员 ");
		}
		return map;
	}
	
	@ResponseBody  
	@RequestMapping("/quit")
	public String quit(HttpServletRequest request){
	    String userName = request.getSession().getAttribute("userName").toString();
	    
		request.getSession().removeAttribute("userName");
		request.getSession().removeAttribute("key");
		
		operationLogService.addOperationLog(userName, request, "退出登录", "成功", "--");
		
		try {
		    userLoginInfo.remove(request.getRemoteAddr());
		}catch(Exception e) {
		    
		}
		return "success";
	}
	
	@RequestMapping("/lockUser")
    public String lockUser(@RequestParam(value="page", defaultValue="1")int id, HttpServletRequest request){
        try {
            UserEntity userEntity = loginService.queryUser(id);                
            
            if (userEntity != null) {
                request.getSession().setAttribute("errorCount", userEntity.getMaxErrorCount());
                request.getSession().setAttribute("lockTime",  userEntity.getMaxLockTime());
            } 
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "user/lockUser";
    }
	
	@ResponseBody  
    @RequestMapping("/updateLockConfig")
    public Map<String, String> updateLockConfig(String errorCount, String lockTime, HttpServletRequest request){
        Map<String, String> map = new HashMap<String, String>();
        String userName = request.getSession().getAttribute("userName").toString();
        try {
            if(errorCount == null || lockTime == null) {
                return null;
            }
            
            if(!StringUtil.isNumeric(errorCount) || !StringUtil.isNumeric(lockTime)) {
                return null;
            }
            
            UserEntity userEntity = new UserEntity();            
        
            int iErrorCount = Integer.valueOf(errorCount);
            int iLockTime = Integer.valueOf(lockTime);
            userEntity.setMaxErrorCount(iErrorCount);
            userEntity.setMaxLockTime(iLockTime);
            
            loginService.updateUserLockConfig(userEntity);
           
            LoginContorller.maxErrorCount = iErrorCount;
            LoginContorller.maxLockTime = iLockTime;
            
            map.put("success", "yes");
            map.put("info", "修改成功！");
                      
            operationLogService.addOperationLog(userName, request, "修改锁定配置", "成功", "登录错误次数：" + iErrorCount +
                "次，锁定时间：" + iLockTime + "分钟");
            
        } catch (Exception e) {
            map.put("success", "error");
            map.put("info", "修改出现异常，请联系管理员。");
            operationLogService.addOperationLog(userName, request, "修改锁定配置", "失败", "修改出现异常，请联系管理员");
        }
        return map;
    }
	
	public static String getLoginUser(String remoteAddr) {
	    return userLoginInfo.get(remoteAddr);
	}
}
