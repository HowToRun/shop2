package com.huaxin.xxshop.controller;

import com.huaxin.xxshop.entity.*;
import com.huaxin.xxshop.service.CategoryService;
import com.huaxin.xxshop.service.GoodsService;
import com.huaxin.xxshop.util.XXShopUtil;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/goods")
public class GoodsAction {
	@Autowired
    private CategoryService categoryService;
    @Autowired
    private GoodsService goodsService;


	/**
	 * ๅๅๆ็ดข
	 * @author 
	 * @param word
	 * @param session
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/search",method = RequestMethod.GET)
	public String search(String word, HttpSession session, Model model) throws UnsupportedEncodingException {
//		word= new String(word.getBytes("iso8859-1"),"utf-8");
		System.out.println("word"+word);
		List<Goods> goods =goodsService.findGoodsByWord(word);
		System.out.println(goods);
		int MAX = 8;
		List<Goods> hotGoodses = goodsService.getGoodsByRole(MAX);
		System.out.println(hotGoodses);
		//List<Category> categories = categoryService.getCategoriesForIndex();
		model.addAttribute("word", word);
		model.addAttribute("search_result",goods);
		model.addAttribute("hotGoodses", hotGoodses);
		//model.addAttribute("categories",categories);
		return "search_result";
	}

	/**
	 * ๅฐๅๅ็ไฟกๆฏๅจ้จๆพ็คบๅบๆฅ
	 * @param goods
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Goods goods, Model model) {
		List<Goods> goodses = goodsService.getGoods(goods);
		List<Category> categories = categoryService.getCategories();

		model.addAttribute("goodses", goodses);
		model.addAttribute("categories", categories);
		return "/admin/goods_list";
//		return "list";
	}


	/**
	 * ๅธฆๆๆๆๅๅๅ็ฑป็ๅ้กต๏ผ่ฟ่กๅ้กตๆฅ่ฏข
	 * @param page
	 * @param goods
	 * @param model
	 * @return
	 */
	@RequestMapping("/listByPage")
//	public String listByPage(Integer page, Goods goods, Model model) {
	public String listByPage(Integer page,String name, String categoryId, Model model) throws UnsupportedEncodingException {
//		if (name!=null) {
//name = new String(name.getBytes("ISO8859-1"),"utf-8");
//		}
		System.out.println("page: " + page + ", name:" + name+":categoryId"+categoryId);
		Goods goods = new Goods();
		goods.setName(name);
		goods.setCategoryId(categoryId);
		List<Category> categories = categoryService.getCategories();
		if (page==null || page==0) {
			page = new Integer(1);
		}
		PageBean<Goods> pageBean = goodsService.getGoodsByPage(page, goods);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("categories", categories);
		return "admin/goods_list";
	}
	@RequestMapping("/salesStatistics")
	public String salesStatistics(Integer page, String startDate,String endDate,String category, Model model) throws UnsupportedEncodingException {
//		if (name!=null) {
//name = new String(name.getBytes("ISO8859-1"),"utf-8");
//		}
		System.out.println("page: " + page + ", startDate:"+startDate+", endDate:"+endDate+", category:"+category);
		if (StringUtils.isEmpty(startDate)){
//			้ป่ฎค่ทๅๅฝๆฅ
			Date d = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			startDate = sf.format(d);
			endDate = sf.format(d);
		}

		if (page==null || page==0) {
			page = new Integer(1);
		}

		PageBean<SalesStatistics> pageBean = goodsService.salesStatistics(page,startDate,endDate,category);
		model.addAttribute("pageBean", pageBean);
		return "admin/sales_statistics";
	}


	/**
	 * ๅพๅฐๆทปๅ?ๅๅ้กต้ข็ๅๅๅทๅๅ็ฑป
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model) {
		String goodsNo = "XX" + XXShopUtil.getGoodsNo();
		List<Category> categories = categoryService.getCategories();
		model.addAttribute("goodsNo", goodsNo);
		model.addAttribute("categories", categories);
		return "/admin/goods_add";
	}


	/**
	 * ่ฟ่กๅๅๆทปๅ?็ๆไฝ
	 * ๅฎไนๆไปถๅ้๏ผ็จๆฅๆฅๆถๆฅ่ชๅๅฐ้กต้ข็ๆฐๆฎ-->pic
	 * @param goods
	 * @param mv
	 * @param pic
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/addoper")
	public ModelAndView addoper(@ModelAttribute("goods") Goods goods,
						  ModelAndView mv, MultipartFile pic,
						  HttpServletRequest request)
					throws IllegalStateException, IOException {
		System.out.println("now add goods: " + goods);
		/*
		 * dir ่ทๅๅฝๅๆไปถๅญๆพ็็ฎๅฝๅฐๅ
		 * suffix ็จๆฅ่ทๅๅฝๅไธไผ?็ๆไปถ็ๆฉๅฑๅ
		 */
		String FILE_TARGET = "target";
		String basePath = request.getSession().getServletContext().getRealPath("/"); // "/": ่กจ็คบ่ทๅๆ?น็ฎๅฝ
		//basePath = basePath.substring(0,basePath.lastIndexOf(FILE_TARGET));
		String dir = basePath + "goodsimage/";
		// ๆฐๅพ็็ๅ็งฐ
		String originFileName = pic.getOriginalFilename();
		if (originFileName==null) {
			
		}
		String newFileName = XXShopUtil.getId() +
				originFileName.substring(originFileName.lastIndexOf("."));
		System.out.println(originFileName.substring(originFileName.lastIndexOf(".")));
		// ๆฐ็ๅพ็
		File newFile = new File(dir + newFileName);
		// ๅฐๅๅญไธญ็ๆฐๆฎๅๅฅ็ฃ็
		pic.transferTo(newFile);

		if(goods != null) {
			goods.setThumbnail("goodsimage/" + newFileName);
			goodsService.addGoods(goods);
		}

		mv.setViewName("redirect:/goods/listByPage");
		return mv;
	}


	/**
	 * ่ทณ่ฝฌๅฐๆดๆฐ็้กต้ข๏ผๅฐๅ็ฑปไฟกๆฏๅๅๅไฟกๆฏๅธฆ่ฟๅป
	 * @param goodsId
	 * @param model
	 * @return
	 */
	@RequestMapping("/update")
	public String update(String goodsId, Model model) {
		List<Category> categories = categoryService.getCategories();
		Goods goods = goodsService.getGoodsById(goodsId);
		model.addAttribute("goods", goods);
		model.addAttribute("categories", categories);
		return "/admin/goods_update";
	}

	/**
	 * ๆดๆฐๅๅ
	 * @param goods
	 * @param mv
	 * @param pic
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/updateoper")
	public ModelAndView updateoper(Goods goods, ModelAndView mv,
								   MultipartFile pic, HttpServletRequest request)
						throws IllegalStateException, IOException {

        //System.out.println("Now update goods: " + goods);
        Goods originGoods = goodsService.getGoodsById(goods.getId());
        System.out.println("adasd"+pic);
        if(pic != null) {
            /*
             * dir ่ทๅๅฝๅๆไปถๅญๆพ็็ฎๅฝๅฐๅ
             * suffix ็จๆฅ่ทๅๅฝๅไธไผ?็ๆไปถ็ๆฉๅฑๅ
             */
            String FILE_TARGET = "target";
            String basePath = request.getSession().getServletContext().getRealPath("/"); // "/": ่กจ็คบ่ทๅๆ?น็ฎๅฝ
           // basePath = basePath.substring(0,basePath.lastIndexOf(FILE_TARGET));
            String dir = basePath + "goodsimage/";
            String path = basePath + "src/main/webapp/";
            // ๆฐๅพ็็ๅ็งฐ
            String originFileName = pic.getOriginalFilename();
            if (originFileName.equals("")) {
				originFileName =originGoods.getThumbnail();
				//originFileName.substring(originFileName.lastIndexOf("/"));
			}
            System.out.println("originFileName+"+originFileName);
            String newFileName = XXShopUtil.getId() +
                    originFileName.substring(originFileName.lastIndexOf("."));
            // ๆฐ็ๅพ็
            File newFile = new File(dir + newFileName);
            // ๅฐๅๅญไธญ็ๆฐๆฎๅๅฅ็ฃ็
            pic.transferTo(newFile);
            goods.setThumbnail("goodsimage/" + newFileName);
            (new File(path + originGoods.getThumbnail())).delete();
        }

		if(goods.getThumbnail() == null) {
			goods.setThumbnail(originGoods.getThumbnail());
			goods.setCategoryId(originGoods.getCategoryId());
		}

		System.out.println("Finally goods is: " + goods);
		goodsService.updateGoods(goods);

		mv.setViewName("redirect:/goods/listByPage");
		return mv;
	}

	/**
	 * ๅ?้คๅๅ
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(String goodsId) {
		Goods goods = goodsService.getGoodsById(goodsId);
		goodsService.deleteGoods(goods);
		return "redirect:/goods/listByPage";
	}

	/**
	 * ๅๅๅ็ฑปๅฑ็คบ
	 * goodsesWithOrder ๅธฆๆ้กบๅบ็ๅๅ้ๅ
	 * order ็จๆฅๆฅๆถ้กต้ขไผ?่พ่ฟๆฅ็ๆๅบ่งๅ
	 * ้่ฟcategoryIdๆฅๆพ็คบๆๆ็ๅๅ
	 * @param goodsCategoryId
	 * @param order
	 * @param mv
	 * @return
	 */
	@RequestMapping("/listByCate")
//	public String listByCate(Goods goods, String order, Model model) {
	public ModelAndView listByCate(String goodsCategoryId, String order, ModelAndView mv) {
//		Category category = categoryService.getCategoryById(goods.getCategoryId());
		// ้ป่ฎคๆ้้ๆๅบ
		if(order == null) {
			order = "sellnum";
		}
		Category category = categoryService.getCategoryById(goodsCategoryId);
		List<Category> categories = categoryService.getCategories();
		// ๅงไธๅชshow6ไธชๅๅ
		int MAX = 6;
		int TEN = 10;

		List<Goods> goodses = goodsService.getGoodsByOrder(order, MAX, goodsCategoryId);
		List<Goods> goodsesWithOrder = goodsService.getGoodsBySellNum(TEN);

		System.out.println(goodsesWithOrder.size());

		mv.addObject("category", category);
        mv.addObject("categories", categories);
        mv.addObject("goodses", goodses);
        mv.addObject("goodsesWithOrder", goodsesWithOrder);
		mv.setViewName("goods_list");
		return mv;
	}

	/**
	 * ๆพ็คบๅๅ็ไฟกๆฏ
	 * @param goodsId
	 * @param mv
	 * @return
	 */
	@RequestMapping("/view")
	public ModelAndView view(String goodsId, ModelAndView mv) {
		Goods goods = goodsService.getGoodsById(goodsId);
		List<Category> categories = categoryService.getCategories();
//		model.addAttribute("goods", goods);
//		model.addAttribute("categories", categories);
		mv.addObject("goods", goods);
		mv.addObject("categories", categories);
//		session.setAttribute("goods", goods);
		mv.setViewName("goods_view");
		return mv;
	}


	/**
	 * ๅธฆๆๆไธไธชๅๅ็ฑปๅซ็ๅ้กต
	 * @param goods
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/listByPageCate")
	public String listByPageCate(Goods goods, Integer page, String order,Model model) {
		System.out.println(goods);
		System.out.println(page);
		System.out.println(order);
		Category category = categoryService.getCategoryById(goods.getCategoryId());
		if(order == null) {
			order = "sellnum";
		}
		if(page == null) {
			page = new Integer(1);
		}if (page == 0) {
			page = 1;
		}
		List<Category> categories = categoryService.getCategories();
		PageBean<Goods> pageBean = goodsService.getGoodsByPageAndOrder(page, goods, order);
		int ShowNum = 10;
		List<Goods> goodses = goodsService.getGoodsByOrder("sellNum",ShowNum,goods.getCategoryId());
		model.addAttribute("category", category);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("order",order);
		model.addAttribute("categories",categories);
		model.addAttribute("goodses",goodses);

		return "goods_list";
	}
//	@RequestMapping("/listByPageCate")
//	public String listByPageCate(Goods goods, Integer page, Model model) {
//		Category category = categoryService.getCategoryById(goods.getCategoryId());
//		if (page == 0) {
//			page = new Integer(1);
//		}
//		PageBean<Goods> pageBean = goodsService.getGoodsByPage(page, goods);
//		model.addAttribute("category", category);
//		model.addAttribute("pageBean", pageBean);
//		return "goods_list";
//	}


	// ไธ้ขๅฏ่ฝๆฏๅฎ็ฐ่ดญ็ฉ่ฝฆๅฐ็ชๅฏ่ง็ๅณ้ฎไปฃ็?
	/*
	 * ๆฅๅไธไธชๅญ็ฌฆไธฒ๏ผๅนถ่ฟๅไธไธชๅญ็ฌฆไธฒ
	 */
	private String goodsIds;
	private String result;


	/**
	 * goodsIds็จๆฅๆฅๆถๆฅ่ชๅๅฐ็ๆฐๆฎ๏ผๅๅซไบcategoryIdๅnum็jsonๅญ็ฌฆไธฒ
	 * result ๅ่ฟๅๆฅ่ฏขๅฐ็ไฟกๆฏ๏ผไนๆฏๅฐ่ฃๆไบไธไธชjsonๅญ็ฌฆไธฒ
	 * @param goodsIds
	 * @param model
	 * @return
	 */
	@RequestMapping("/getGoodsesByIds")
	public String getGoodsesByIds(String goodsIds, Model model) {
//		System.out.println("goodsIds:" + goodsIds);
		String[] ids = goodsIds.split(",");
//		for (String s : ids) {
//			System.out.println("test:" + s);
//		}
//		System.out.println("test:" + ids.toString());
		List<Goods> goodses = goodsService.getGoodsByIds(ids);
		JsonConfig c = new JsonConfig();
		c.setExcludes(new String[] { "category", "goodsNo", "categoryId",
				"price1", "stock", "description", "role", "sellTime",
				"sellNum", "score" });
		JSONArray a = JSONArray.fromObject(goodses, c);
		String result = a.toString();
		model.addAttribute("result", result);
		System.out.println(result);
//		System.out.println("result:" + result);
		return "/TestJSP.jsp";
//		return "getgoodsesbyids";

	}


}
