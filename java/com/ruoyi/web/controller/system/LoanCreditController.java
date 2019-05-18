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
import com.ruoyi.system.domain.LoanCredit;
import com.ruoyi.system.service.ILoanCreditService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 信贷产品 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-21
 */
@Controller
@RequestMapping("/system/loanCredit")
public class LoanCreditController extends BaseController
{
    private String prefix = "system/loanCredit";
	
	@Autowired
	private ILoanCreditService loanCreditService;
	
	@RequiresPermissions("system:loanCredit:view")
	@GetMapping()
	public String loanCredit()
	{
	    return prefix + "/loanCredit";
	}
	
	/**
	 * 查询信贷产品列表
	 */
	@RequiresPermissions("system:loanCredit:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(LoanCredit loanCredit)
	{
		startPage();
        List<LoanCredit> list = loanCreditService.selectLoanCreditList(loanCredit);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出信贷产品列表
	 */
	@RequiresPermissions("system:loanCredit:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LoanCredit loanCredit)
    {
    	List<LoanCredit> list = loanCreditService.selectLoanCreditList(loanCredit);
        ExcelUtil<LoanCredit> util = new ExcelUtil<LoanCredit>(LoanCredit.class);
        return util.exportExcel(list, "loanCredit");
    }
	
	/**
	 * 新增信贷产品
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存信贷产品
	 */
	@RequiresPermissions("system:loanCredit:add")
	@Log(title = "信贷产品", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(LoanCredit loanCredit)
	{		
		return toAjax(loanCreditService.insertLoanCredit(loanCredit));
	}

	/**
	 * 修改信贷产品
	 */
	@GetMapping("/edit/{loanCreditId}")
	public String edit(@PathVariable("loanCreditId") Integer loanCreditId, ModelMap mmap)
	{
		LoanCredit loanCredit = loanCreditService.selectLoanCreditById(loanCreditId);
		mmap.put("loanCredit", loanCredit);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存信贷产品
	 */
	@RequiresPermissions("system:loanCredit:edit")
	@Log(title = "信贷产品", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(LoanCredit loanCredit)
	{		
		return toAjax(loanCreditService.updateLoanCredit(loanCredit));
	}
	
	/**
	 * 删除信贷产品
	 */
	@RequiresPermissions("system:loanCredit:remove")
	@Log(title = "信贷产品", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(loanCreditService.deleteLoanCreditByIds(ids));
	}
	
}
