package com.study.shiro.shiroconfig;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 *
 * @author
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    String redisHost;

    @Value("${spring.redis.port}")
    int redisPort;

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/page/login");

        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();


//        filterChainDefinitionMap.put("/user/index", "perms[system:user:index]");
//        filterChainDefinitionMap.put("/user/add", "perms[system:user:add]");
//        filterChainDefinitionMap.put("/user/edit", "perms[system:user:edit]");
//        filterChainDefinitionMap.put("/user/delete", "perms[system:user:delete]");
//        filterChainDefinitionMap.put("/user/grant/**", "perms[system:user:grant]");

        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/ziyuan/**", "anon");
//        把页面跳转请求放开
        filterChainDefinitionMap.put("/page/**", "anon");
        filterChainDefinitionMap.put("layui/**", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/register", "anon");
        // 登录成功后要跳转的链接 (自己实现Controller)
        shiroFilterFactoryBean.setSuccessUrl("/page/main");
        // 未授权界面(自己实现后台跳转)//未授权界面,该配置无效，并不会进行页面跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/page/403");

        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

//        不但要登陆，还要vip角色，记住我的有效期之内也要重新登陆才能访问
        filterChainDefinitionMap.put("/user/reat", "authc,roles[vip]");
//第一次登陆状态是记住我的，下次直接打开记住我的时效还没过期的就可以访问
        filterChainDefinitionMap.put("/user/rememberMe", "user");
		
        /**
         * ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★逐一配置★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
         */
//++++++++++++++++++++++++++++    perms
//        filterChainDefinitionMap.put("/user/index", "perms[system:user:index]");
//        filterChainDefinitionMap.put("/user/add", "perms[system:user:add]");
//        filterChainDefinitionMap.put("/user/edit", "perms[system:user:edit]");
//        filterChainDefinitionMap.put("/user/delete", "perms[system:user:delete]");
//        filterChainDefinitionMap.put("/user/grant/**", "perms[system:user:grant]");

//++++++++++++++++++++++++++++++ roles
        // filterChainDefinitionMap.put("/user/select", "roles[admin]");

        /**
         * ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★数据库统一查询配置★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
         */
//        List<Permission> permissionList = permissionDao.all();
//        if (permissionList != null) {
//            for (Permission permission : permissionList) {
//                filterChainDefinitionMap.put(permission.getUrl(), "perms[" + permission.getPermissionName() + "]");
//            }
//        }
		
		
		
		
		
		
		
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 :这是一个坑呢，一不小心代码就不好使了;
        // ① authc:所有url都必须认证通过才可以访问; ② anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 使用记住我 交给SecurityManager管理
        securityManager.setRememberMeManager(rememberMeManager(rememberMeCookie()));

        // 自定义缓存实现
        securityManager.setCacheManager(cacheManager());
        // 自定义session管理
//        securityManager.setSessionManager(sessionManager());

        // //将自定义的realm交给SecurityManager管理
        securityManager.setRealm(myRealm());
        return securityManager;
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
//        密码比较器
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myRealm;
    }

    /**
     * Shiro生命周期处理器
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

//	_________________________________________ 记住我 _______________________________________________________________

    /**
     * rememberMe管理器, cipherKey生成见{@code Base64Test.java}
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCipherKey(Base64.decode("Z3VucwAAAAAAAAAAAAAAAA=="));
        manager.setCookie(rememberMeCookie);
        return manager;
    }

    /**
     * 记住密码Cookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(7 * 24 * 60 * 60);//7天
        return simpleCookie;
    }

    /**
     * （3）配置缓存验证器
     * @return
     */
    @Bean
    public CacheManager cacheManager(){
        return new MemoryConstrainedCacheManager();
    }


	    //  缓存配置
    //shiro自带的MemoryConstrainedCacheManager作缓存
    // 但是只能用于本机，在集群时就无法使用，需要使用ehcache
//    @Bean(name = "cacheManager")
//    public MemoryConstrainedCacheManager cacheManager() {
//        MemoryConstrainedCacheManager cacheManager=new MemoryConstrainedCacheManager();//使用内存缓存
//        return cacheManager;
//    }
	

    //    ————————————————————————————————————————————— 配置ShiroDialect，用于Thymeleaf和Shiro标签配合使用 ————————————————————————————————————————————————————
    /**
     * 必须（thymeleaf页面使用shiro标签控制按钮是否显示）
     * 未引入thymeleaf包，Caused by: java.lang.ClassNotFoundException: org.thymeleaf.dialect.AbstractProcessorDialect
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


    /**
     * 凭证匹配器
     * 密码校验规则HashedCredentialsMatcher
     * 这个类是为了对密码进行编码的 ,
     * 防止密码在数据库里明码保存 , 当然在登陆认证的时候 ,
     * 这个类也负责对form里输入的密码进行编码
     * 处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
     *
     * @return
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("MD5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1024);//散列的次数
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);

        return hashedCredentialsMatcher;
    }
}
