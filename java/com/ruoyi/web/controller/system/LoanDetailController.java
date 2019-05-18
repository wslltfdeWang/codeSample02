package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.exception.BusinessServiceException;
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
import com.ruoyi.system.domain.LoanDetail;
import com.ruoyi.system.service.ILoanDetailService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 贷款产品明细 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-28
 */
@Controller
@RequestMapping("/system/loanDetail")
public class LoanDetailController extends BaseController
{
    private String prefix = "system/loanDetail";
	
	@Autowired
	private ILoanDetailService loanDetailService;
	
	@RequiresPermissions("system:loanDetail:view")
	@GetMapping()
	public String loanDetail()
	{
	    return prefix + "/loanDetail";
	}
	
	/**
	 * 查询贷款产品明细列表
	 */
	@RequiresPermissions("system:loanDetail:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(LoanDetail loanDetail)
	{
		startPage();
        List<LoanDetail> list = loanDetailService.selectLoanDetailList(loanDetail);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出贷款产品明细列表
	 */
	@RequiresPermissions("system:loanDetail:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LoanDetail loanDetail)
    {
    	List<LoanDetail> list = loanDetailService.selectLoanDetailList(loanDetail);
        ExcelUtil<LoanDetail> util = new ExcelUtil<LoanDetail>(LoanDetail.class);
        return util.exportExcel(list, "loanDetail");
    }
	
	/**
	 * 新增贷款产品明细
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存贷款产品明细
	 */
	@RequiresPermissions("system:loanDetail:add")
	@Log(title = "贷款产品明细", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(LoanDetail loanDetail)
	{
		try {
			return toAjax(loanDetailService.insertLoanDetail(loanDetail));
		} catch (BusinessServiceException e) {
			return error(e.getMessage());
		}
	}

	/**
	 * 修改贷款产品明细
	 */
	@GetMapping("/edit/{loanDetailId}")
	public String edit(@PathVariable("loanDetailId") Integer loanDetailId, ModelMap mmap)
	{
		LoanDetail loanDetail = loanDetailService.selectLoanDetailById(loanDetailId);
		mmap.put("loanDetail", loanDetail);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存贷款产品明细
	 */
	@RequiresPermissions("system:loanDetail:edit")
	@Log(title = "贷款产品明细", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(LoanDetail loanDetail)
	{		
		return toAjax(loanDetailService.updateLoanDetail(loanDetail));
	}
	
	/**
	 * 删除贷款产品明细
	 */
	@RequiresPermissions("system:loanDetail:remove")
	@Log(title = "贷款产品明细", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(loanDetailService.deleteLoanDetailByIds(ids));
	}
	
}
