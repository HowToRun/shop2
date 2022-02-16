package com.huaxin.xxshop.controller;


import com.huaxin.xxshop.entity.Cart;
import com.huaxin.xxshop.entity.User;
import com.huaxin.xxshop.service.CartService;
import com.huaxin.xxshop.util.UserThreadLocal;
import com.huaxin.xxshop.util.XXShopUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/cart")
public class CartAction {
    @Autowired
     private CartService cartService ;


    /**
     * 删除购物车记录
     * @param cartId
     * @param mv
     * @param session
     * @return
     */
    @RequestMapping("/deleteById")
    public ModelAndView deleteById(String cartId, ModelAndView mv, HttpSession session) {
        cartService.deleteById(cartId);
        mv.setViewName("redirect:/cart/listByUser");
        return mv;
    }

    /**
     * 以用户为单位查询购物车记录
     * @param session
     * @param mv
     * @return
     */
    @RequestMapping("/listByUser")
    public ModelAndView list(HttpSession session, ModelAndView mv) {
        User user = (User) session.getAttribute("user");

       String userId = user.getId();
        //String userId = UserThreadLocal.get();
        UserThreadLocal.set(userId);
        List<Cart> cartList = cartService.getCartsByUserId(userId);
        //System.out.println("dasgdhasgdhjgsjd"+cartList);
//        System.out.println(cartList.size());
//        if(cartList.size() > 0) {
//            System.out.println(cartList.get(0));
//        }
        mv.addObject("cartList", cartList);
        mv.setViewName("cart");

        return mv;
    }

    /**
     * 增加购物车记录
     * @param session
     * @param goodsId
     * @param num
     * @param price
     * @param mv
     * @return 返回到购物车列表展示界面
     */
    @RequestMapping("/add")
    public ModelAndView addCart(HttpSession session, String goodsId,
                                Integer num, Float price, ModelAndView mv) {
//        System.out.println(goodsId + " " + num + " " + price);
        User user = (User) session.getAttribute("user");

        String userId = user.getId();
        System.out.println(userId);
       // String userId = UserThreadLocal.get();
        cartService.addCart(userId,
                goodsId, num, price);
        // 进入之后的下单页面order/add需要goodsId和num
        mv.addObject("goodsId", goodsId);
        mv.addObject("num", num);
        mv.setViewName("redirect:/cart/listByUser");
        return mv;
    }

}
