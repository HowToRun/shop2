package com.huaxin.xxshop.controller;

import com.huaxin.xxshop.entity.*;
import com.huaxin.xxshop.service.*;
import com.huaxin.xxshop.util.SysResult;
import com.huaxin.xxshop.util.XXShopUtil;
import com.opensymphony.xwork2.ActionContext;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于处理控制前台的用户请求的Action
 * @author 没有蜡笔的小新 2015-12-22
 */

@Controller
@RequestMapping("/user")
public class UserAction {
    @Autowired
    private UserService userService = null;
	@Autowired
    private CategoryService categoryService = null;
    @Autowired
	private GoodsService goodsService = null;
    @Autowired
    private OrderService orderService;
	@Autowired
	private VisitService visitService = null;

	/**
	 * 测试用方法
	 * @param request
	 */
	@RequestMapping("/servletTest")
	public void servletTest(HttpServletRequest request) {
		System.out.println("request.getSession().getServletContext().getRealPath('/')"
				+ request.getSession().getServletContext().getRealPath("/"));
		// /Users/yin/Documents/Git/GitHub/Local/xxshop/target/xxshop/
	}

	/**
	 * 进行登陆操作 判断当前的用户名和密码是否正确，如果验证正确还需要将
	 * 用户信息存放在session里面
	 * @return 返回对应result
	 */
	@RequestMapping(value = "/login", method= RequestMethod.POST)
	public String login(String name, String password, HttpSession session,
						HttpServletRequest request,HttpServletResponse response) {
		User user = userService.findUser(name, password);
		Visit visit = null;
		if (user == null || user.getStatus() == 2) {
			return "login";
		} else {
			user.setStatus(1);
			userService.updateStatus(user.getId(), user.getStatus());
			session.setAttribute("user", user);
			user.setIp(XXShopUtil.getIpAddr(request));
			//            visit.setUserId(user.getId());
			userService.addLogin(user);
			//把用户的密码存到cookie中，判断是会员还是普通用户
			String cookieName="userPassword";
			String cookieValue=user.getPassword();
			Cookie myCookie=new Cookie(cookieName,cookieValue);
			myCookie.setMaxAge(60*60*24);//一天
			response.addCookie(myCookie);
			// 重定向到 index 资源
			return "redirect:/index"; 
		}
	}





	@RequestMapping("/userUpdate")
	public ModelAndView userUpdate(HttpSession session, ModelAndView mv) {
		User user = userService.getUser(((User)session.getAttribute("user")).getId());
		mv.addObject("user", user);
		mv.setViewName("usercenter/user_update");
		return mv;
	}

	@RequestMapping("/updateFailed")
	public ModelAndView updateFailed(HttpSession session, ModelAndView mv) {
		User user = userService.getUser(((User)session.getAttribute("user")).getId());
		mv.addObject("user", user);
		mv.setViewName("usercenter/update_failed");
		return mv;
	}


	/**
	 *尝试修改个人信息
	 */
	@RequestMapping("/update")
	public String update(HttpSession session,HttpServletResponse response, String email,String phoneNum,String oldpass,String password) throws IOException {
		User user = userService.getUser(((User)session.getAttribute("user")).getId());
		System.out.println("UserAction :"+phoneNum);
		userService.updateEmail(user.getId(),email);
		userService.updatePhoneNum(user.getId(),phoneNum);
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out = response.getWriter();
		if(oldpass.equals("")){
			System.out.println("旧密码为空，不执行密码修改操作");
		}else if (oldpass.equals(user.getPassword())){
			System.out.println("新旧密码相同，可以执行密码操作");
			userService.updatePassword(user.getId(),password);
		}else {
			System.out.println("新旧密码不同，跳转到报错页面");
			out.print("<script language=\"javascript\">alert('原始密码输入错误，修改失败……');window.location.href='/xxshop/login'</script>");
			return "usercenter/update_failed";
		}
		System.out.println(user.getPassword());
		System.out.println(oldpass);
		return "redirect:/user/userInfo";
	}

	@RequestMapping("/listByPage")
	public String listByPage(Integer page, Model model) {
		if (page==null ) {
			page = new Integer(1);
		}
		PageBean<Visit> pageBean = visitService.getVisitsByPage(page);
		model.addAttribute("pageBean", pageBean);
		return "admin/login_list";
	}





	/**
	 * 传入商品图片并储存在对应文件夹下
	 * @param mv
	 * @param pic
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/uploadTest")
	public ModelAndView uploadTest(ModelAndView mv, MultipartFile pic, HttpServletRequest request)
				throws IllegalStateException, IOException {
		String FILE_TARGET = "target";
		String basePath = request.getSession().getServletContext().getRealPath("/"); // "/": 表示获取根目录
		basePath = basePath.substring(0,basePath.lastIndexOf(FILE_TARGET));
		String path = basePath + "src/main/webapp/goodsimage/";
		System.out.println(path);
		// 新图片的名称
		String originFileName = pic.getOriginalFilename();
		String newFileName = XXShopUtil.getId() +
				originFileName.substring(originFileName.lastIndexOf("."));
		// 新的图片
		File newFile = new File(path + newFileName);
		// 将内存中的数据写入磁盘
		pic.transferTo(newFile);
		return mv;
	}

	/**
	 * 查询
	 * 似乎可以直接从session中取出用户基本信息
	 * 所以如果不需要更多的信息不需要该方法
	 * @param session
	 * @param mv
	 * @return
	 */
	@RequestMapping("/userInfo")
	public ModelAndView userInfo(HttpSession session, ModelAndView mv) {
		User user = userService.getUser(((User)session.getAttribute("user")).getId());
		mv.addObject("user", user);
		mv.setViewName("usercenter/user_info");
		return mv;
	}



	/**
	 * 实现用户注册的方法
	 * @return
	 */
	@RequestMapping("/register")
	public String register(User user) {
		// 检验各项信息
		user.setRole("u");
		userService.register(user);
		return "login";
	}
	/**
	 * 实现用户密码重置
	 * @return
	 */
	@RequestMapping("/backPassword")
	@ResponseBody
	public SysResult rectPassword(String email,String password) {
		System.out.println(email+password);
		User user = new User();
		user.setEmail(email);
		User user1 = userService.findUserByParam(user);
		userService.updatePassword(user1.getId(),password);
		return SysResult.oK();
	}


	/**
	 * 判断是否存在当前的用户信息，用于相应Ajax的方法
	 */
	@RequestMapping("/user_isexist/{param}/{aaa}" )
	@ResponseBody
	public SysResult isexist(@PathVariable("param") String param,
							 @PathVariable("aaa") String aaa) {
		System.out.println("ahhahahahahahah");
		User user = new User();
		if("0".equals(aaa)){
			user.setName(param);
		}else if("1".equals(aaa)){
		//	param = param+".com";
			user.setEmail(param);
		}
		 user = userService.findUserByParam(user);
		System.out.println(user);
		return SysResult.oK(user);

	}

	/**
	 * sendEmailCode
	 */
	/*@RequestMapping("/sendEmailCode" )
	@ResponseBody
	public SysResult emailCode(String email) {


		return SysResult.oK();

	}*/
	/**
	 * unused
	 * 实现上传头像的功能 使用了 org.apache.commons.io.FileUtils 中的静态方法copyFile()
	 * @return 返回struts的对应result
	 */
	@RequestMapping("/uploadAvatar")
	public String uploadAvatar(HttpServletRequest request,HttpSession session,MultipartFile file) {
		System.out.println(file);
		//获得原始文件名
		String avatarFileName = file.getOriginalFilename();

		System.out.println("原始文件名："+ avatarFileName);
		String userId = ((User) session.getAttribute("user")).getId();

		/**
		 *  dir 获取当前文件存放的目录地址 suffix 用来获取当前上传的文件的扩展名
		 */
		String FILE_TARGET = "target";
		String basePath = request.getSession().getServletContext().getRealPath("/"); // "/": 表示获取根目录
		System.out.println(basePath);
		//basePath = basePath.replace("\\","/");
		//basePath = basePath.substring(0,basePath.lastIndexOf(FILE_TARGET));
		String dir = basePath + "useravatars/";
		/*String dir = ServletActionContext.getServletContext().getRealPath(
				"/useravatars");*/
		String suffix = "";
		if (file != null) {
			suffix = avatarFileName.substring(avatarFileName.lastIndexOf("."));
			// 组装成最后需要保存的文件的地址(包括文件的扩展名)
			File saveFile = new File(dir+userId + suffix);
			System.out.println(dir+userId + suffix);
			try {
				// 上传文件 org.apache.commons.io.FileUtils 既可以实现将文件上传到服务器上对应的文件夹下面
				//FileUtils.copyFile(avatar, saveFile);
				// 将内存中的数据写入磁盘
				file.transferTo(saveFile);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		// 将对应的文件地址信息存放到数据库中去
		userService.updateAvatar(userId, "useravatars/" + userId + suffix);
		((User) session.getAttribute("user"))
				.setAvatar("useravatars/" + userId + suffix);
//		return "usercenter";
        return "/usercenter/index";
	}

	/**
	 * 实现在线充值的功能
	 * @param session
	 * @param recharge
	 * @return
	 */
	@RequestMapping("/recharge")
	public String recharge(HttpSession session, String recharge) {
		User user = (User) session.getAttribute("user");
		float money = user.getMoney()
				+ Float.parseFloat(recharge);
		user.setMoney(money);
		userService.updateMoney(user.getId(), money);
		return "/usercenter/account_log";
	}

    /**
     * 后台管理员登录
     * @param name 管理员用户姓名
     * @param password 管理员用户密码
     * @param captcha 登录输入验证码
     * @return 判断后，返回对应视图名
     * note 还有验证码的验证没有实现
     */
	@RequestMapping("/adminLogin")
	public String adminLogin(String name, String password, String captcha, HttpSession session) {
		// 验证码的验证似乎应该在前台比较好
//	    String verifyCode = "uwv6";
//	    if(!captcha.toLowerCase().equals(verifyCode)) {
//	        //msg = "验证码不对";
//	        return "admin/login";
//        }
        User adminUser = userService.findUser(name, password);
//		User adminUser = userService.findUser(user.getName(), user.getPassword());
		if (adminUser == null) {
			//msg = "用户名或密码不正确";
			return "/admin/login";
		}
		if (!adminUser.getRole().equals("a")) {
			//msg = "您的权限不足";
			return "/admin/login";
		}
		session.setAttribute("user", adminUser);
		return "/admin/index";
	}

	/*
	 * 得到当前所有的用户信息，并显示到用户的信息栏中
	 */
	@RequestMapping("/member")
	public String member(Model model) {
        List<User> userList = userService.getAllUser();
		model.addAttribute("userList", userList);
		return "/admin/member_list";
	}


	/**
	 * 删除一个用户
	 * * 实际上是将用户的状态信息设置为2
	 * @param userId
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(String userId) {
		if (userService.getUser(userId) != null) {
			userService.deleteUser(userId);
		}
//		return "opersuc";
        return "redirect:/user/member";
	}


	/**
	 * 检验当前用户余额是否足够
	 * @param orderId
	 * @param session
	 * @param response
	 */
	@RequestMapping("/moneyLeftEnough")
	public void moneyLeftEnough(String orderId, HttpSession session,
                                HttpServletResponse response) {
	    User user = ((User) session.getAttribute("user"));
		System.out.println(orderId.getClass() + " got orderId: " + orderId);
	    Order order = orderService.getOrder(orderId);
		System.out.println("Finally no more two orders!");
		System.out.println(order);
	    PrintWriter write = null;
		try {
//		    write = ServletActionContext.getResponse().getWriter(); // 此为Struts2中的方法
            write = response.getWriter();
            if (order.getTotalMoney() <= user.getMoney()) {
				write.print(1);
			} else {
				write.print(0);
			}
			write.flush();
		} catch (IOException e) {
            System.out.println("Something wrong!");
		    e.printStackTrace();
		} catch (Exception e) {
            e.printStackTrace();
        }
		finally {
			if(write!=null) {
                write.close();
            }
		}
	}

    /**
     * 用户登出
     * @return 返回登录界面
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user==null) {
			//return “login;
		}
        user.setStatus(0); // status=0表示不在线
        userService.updateStatus(user.getId(), user.getStatus());
        session.removeAttribute("user"); // 清空session
        //System.out.println("用户退出了!");
        return "login";
    }

    /**
     * 向用户登录页面跳转
     * @return
     */
    @RequestMapping(value="/toLogin")
    public String toLogin() {
        return "login";
    }
    /**
     * 向用户注册页面跳转
     * @return
     */
    @RequestMapping(value="/toRegister")
    public String toRegister() {
        return "register";
    }

	/**
	 * 忘记密码用邮箱找回
	 * @return
	 */
	@RequestMapping(value="/toBackPassword")
	public String toBackPassword() {
		return "back_password";
	}
    /**
     * 向后台管理员登录页面跳转
     * @return
     */
    @RequestMapping(value="/toAdminLogin")
    public String toAdminLogin() {
        return "/admin/index";
    }

    /**
     * 向用户中心账户余额页面跳转
     * @return
     */
    @RequestMapping(value="/usercenter/toAccountLog")
    public String toAccountLog() {
        return "/usercenter/account_log";
    }
    /**
     * 向用户中心在线充值页面跳转
     * @return
     */
    @RequestMapping(value="/usercenter/toOnlineRecharge")
    public String toOnlineRecharge() {
        return "/usercenter/online_recharge";
    }

    @RequestMapping(value="/usercenter/toAvatarChange")
	public String toAvatarChange() { return "usercenter/user_avatar";}

	@RequestMapping("toCart")
	public String toCart() {
    	return "cart";
	}


}
