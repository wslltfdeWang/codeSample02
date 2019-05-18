package com.ruoyi.web.controller;

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
import com.ruoyi.system.domain.LoanRecommend;
import com.ruoyi.system.service.ILoanRecommendService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 爆款产品推荐 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-04-26
 */
@Controller
@RequestMapping("/system/loanRecommend")
public class LoanRecommendController extends BaseController
{
    private String prefix = "system/loanRecommend";
	
	@Autowired
	private ILoanRecommendService loanRecommendService;
	
	@RequiresPermissions("system:loanRecommend:view")
	@GetMapping()
	public String loanRecommend()
	{
	    return prefix + "/loanRecommend";
	}
	
	/**
	 * 查询爆款产品推荐列表
	 */
	@RequiresPermissions("system:loanRecommend:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(LoanRecommend loanRecommend)
	{
		startPage();
        List<LoanRecommend> list = loanRecommendService.selectLoanRecommendList(loanRecommend);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出爆款产品推荐列表
	 */
	@RequiresPermissions("system:loanRecommend:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LoanRecommend loanRecommend)
    {
    	List<LoanRecommend> list = loanRecommendService.selectLoanRecommendList(loanRecommend);
        ExcelUtil<LoanRecommend> util = new ExcelUtil<LoanRecommend>(LoanRecommend.class);
        return util.exportExcel(list, "loanRecommend");
    }
	
	/**
	 * 新增爆款产品推荐
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存爆款产品推荐
	 */
	@RequiresPermissions("system:loanRecommend:add")
	@Log(title = "爆款产品推荐", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(LoanRecommend loanRecommend)
	{		
		return toAjax(loanRecommendService.insertLoanRecommend(loanRecommend));
	}

	/**
	 * 修改爆款产品推荐
	 */
	@GetMapping("/edit/{loanRecommendId}")
	public String edit(@PathVariable("loanRecommendId") Integer loanRecommendId, ModelMap mmap)
	{
		LoanRecommend loanRecommend = loanRecommendService.selectLoanRecommendById(loanRecommendId);
		mmap.put("loanRecommend", loanRecommend);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存爆款产品推荐
	 */
	@RequiresPermissions("system:loanRecommend:edit")
	@Log(title = "爆款产品推荐", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(LoanRecommend loanRecommend)
	{		
		return toAjax(loanRecommendService.updateLoanRecommend(loanRecommend));
	}
	
	/**
	 * 删除爆款产品推荐
	 */
	@RequiresPermissions("system:loanRecommend:remove")
	@Log(title = "爆款产品推荐", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(loanRecommendService.deleteLoanRecommendByIds(ids));
	}
	
}
