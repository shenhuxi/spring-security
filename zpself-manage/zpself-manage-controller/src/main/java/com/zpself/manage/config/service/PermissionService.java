package com.zpself.manage.config.service;


import com.zpself.manage.common.controller.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Service("permissionService")
public class PermissionService {

	private static Logger logger = LoggerFactory.getLogger(PermissionService.class);

	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	/**
	 * 检验当前身份是否有权限访问资源
	 * 
	 * @param request
	 * @param authentication
	 * @return
	 */
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		String requestURI = request.getRequestURI();
		logger.info("requestURI:{}", requestURI);
		if (requestURI.endsWith("/")) {
			requestURI = requestURI.substring(0, requestURI.length() - 1);
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean hasPermission = false;// 是否拥有权限资源
		// 校验用户是否拥有访问该资源的的权限
		if (!hasPermission && authorities != null && !authorities.isEmpty()) {
			for (GrantedAuthority grantedAuthority : authorities) {
				String authority = grantedAuthority.getAuthority();
				String[] authArray = authority.split(";"); // system/user/list;POST
				if (authArray != null && authArray.length == 2) {
					String url = authArray[0];// 访问路径
					String method = authArray[1];// 访问方法
					if (StringUtils.isNotEmpty(url)
							&& antPathMatcher.match("/api" +"/"+ url, requestURI)
							&& request.getMethod().equalsIgnoreCase(method)) {
						hasPermission = true;
						break;
					}
				}
			}
		}
		return hasPermission;
	}
}