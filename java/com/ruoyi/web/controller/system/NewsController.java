package com.ruoyi.web.controller.system;

import java.util.Date;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.News;
import com.ruoyi.system.service.INewsService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 新闻资讯 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-04-25
 */
@Controller
@RequestMapping("/system/news")
public class NewsController extends BaseController
{
    private String prefix = "system/news";
	
	@Autowired
	private INewsService newsService;
	
	@RequiresPermissions("system:news:view")
	@GetMapping()
	public String news()
	{
	    return prefix + "/news";
	}
	
	/**
	 * 查询新闻资讯列表
	 */
	@RequiresPermissions("system:news:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(News news)
	{
		startPage();
        List<News> list = newsService.selectNewsList(news);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出新闻资讯列表
	 */
	@RequiresPermissions("system:news:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(News news)
    {
    	List<News> list = newsService.selectNewsList(news);
        ExcelUtil<News> util = new ExcelUtil<News>(News.class);
        return util.exportExcel(list, "news");
    }
	
	/**
	 * 新增新闻资讯
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存新闻资讯
	 */
	@RequiresPermissions("system:news:add")
	@Log(title = "新闻资讯", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(News news)
	{		
		news.setCreateBy(ShiroUtils.getUserId().toString());
		news.setCreateTime(new Date());
		news.setUpdateBy(ShiroUtils.getUserId().toString());
		news.setUpdateTime(new Date());
		return toAjax(newsService.insertNews(news));
	}

	/**
	 * 修改新闻资讯
	 */
	@GetMapping("/edit/{newsId}")
	public String edit(@PathVariable("newsId") Integer newsId, ModelMap mmap)
	{
		News news = newsService.selectNewsById(newsId);
		mmap.put("news", news);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存新闻资讯
	 */
	@RequiresPermissions("system:news:edit")
	@Log(title = "新闻资讯", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(News news)
	{		
		news.setUpdateBy(ShiroUtils.getUserId().toString());
		news.setUpdateTime(new Date());
		return toAjax(newsService.updateNews(news));
	}
	
	/**
	 * 删除新闻资讯
	 */
	@RequiresPermissions("system:news:remove")
	@Log(title = "新闻资讯", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(newsService.deleteNewsByIds(ids));
	}
	
}
