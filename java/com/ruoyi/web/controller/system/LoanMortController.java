package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.LoanMort;
import com.ruoyi.system.service.ILoanMortService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 抵押贷款 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-21
 */
@Controller
@RequestMapping("/system/loanMort")
public class LoanMortController extends BaseController
{
    private String prefix = "system/loanMort";
	
	@Autowired
	private ILoanMortService loanMortService;
	
	@RequiresPermissions("system:loanMort:view")
	@GetMapping()
	public String loanMort()
	{
	    return prefix + "/loanMort";
	}
	
	/**
	 * 查询抵押贷款列表
	 */
	@RequiresPermissions("system:loanMort:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(LoanMort loanMort)
	{
		startPage();
        List<LoanMort> list = loanMortService.selectLoanMortList(loanMort);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出抵押贷款列表
	 */
	@RequiresPermissions("system:loanMort:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LoanMort loanMort)
    {
    	List<LoanMort> list = loanMortService.selectLoanMortList(loanMort);
        ExcelUtil<LoanMort> util = new ExcelUtil<LoanMort>(LoanMort.class);
        return util.exportExcel(list, "loanMort");
    }
	
	/**
	 * 新增抵押贷款
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存抵押贷款
	 */
	@RequiresPermissions("system:loanMort:add")
	@Log(title = "抵押贷款", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(LoanMort loanMort)
	{		
		return toAjax(loanMortService.insertLoanMort(loanMort));
	}

	/**
	 * 修改抵押贷款
	 */
	@GetMapping("/edit/{loanMortId}")
	public String edit(@PathVariable("loanMortId") Integer loanMortId, ModelMap mmap)
	{
		LoanMort loanMort = loanMortService.selectLoanMortById(loanMortId);
		mmap.put("loanMort", loanMort);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存抵押贷款
	 */
	@RequiresPermissions("system:loanMort:edit")
	@Log(title = "抵押贷款", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(LoanMort loanMort)
	{		
		return toAjax(loanMortService.updateLoanMort(loanMort));
	}
	
	/**
	 * 删除抵押贷款
	 */
	@RequiresPermissions("system:loanMort:remove")
	@Log(title = "抵押贷款", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(loanMortService.deleteLoanMortByIds(ids));
	}
	
}
