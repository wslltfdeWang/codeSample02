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
import com.ruoyi.system.domain.PersLoanResult;
import com.ruoyi.system.service.IPersLoanResultService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 个人贷款产品结果 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-03-16
 */
@Controller
@RequestMapping("/system/persLoanResult")
public class PersLoanResultController extends BaseController
{
    private String prefix = "system/persLoanResult";
	
	@Autowired
	private IPersLoanResultService persLoanResultService;
	
	@RequiresPermissions("system:persLoanResult:view")
	@GetMapping()
	public String persLoanResult()
	{
	    return prefix + "/persLoanResult";
	}
	
	/**
	 * 查询个人贷款产品结果列表
	 */
	@RequiresPermissions("system:persLoanResult:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PersLoanResult persLoanResult)
	{
		startPage();
        List<PersLoanResult> list = persLoanResultService.selectPersLoanResultList(persLoanResult);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出个人贷款产品结果列表
	 */
	@RequiresPermissions("system:persLoanResult:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PersLoanResult persLoanResult)
    {
    	List<PersLoanResult> list = persLoanResultService.selectPersLoanResultList(persLoanResult);
        ExcelUtil<PersLoanResult> util = new ExcelUtil<PersLoanResult>(PersLoanResult.class);
        return util.exportExcel(list, "persLoanResult");
    }
	
	/**
	 * 新增个人贷款产品结果
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存个人贷款产品结果
	 */
	@RequiresPermissions("system:persLoanResult:add")
	@Log(title = "个人贷款产品结果", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PersLoanResult persLoanResult)
	{		
		return toAjax(persLoanResultService.insertPersLoanResult(persLoanResult));
	}

	/**
	 * 修改个人贷款产品结果
	 */
	@GetMapping("/edit/{persLoanResult}")
	public String edit(@PathVariable("persLoanResult") Integer persLoanResultId, ModelMap mmap)
	{
		PersLoanResult persLoanResult = persLoanResultService.selectPersLoanResultById(persLoanResultId);
		mmap.put("persLoanResult", persLoanResult);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人贷款产品结果
	 */
	@RequiresPermissions("system:persLoanResult:edit")
	@Log(title = "个人贷款产品结果", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PersLoanResult persLoanResult)
	{		
		return toAjax(persLoanResultService.updatePersLoanResult(persLoanResult));
	}
	
	/**
	 * 删除个人贷款产品结果
	 */
	@RequiresPermissions("system:persLoanResult:remove")
	@Log(title = "个人贷款产品结果", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(persLoanResultService.deletePersLoanResultByIds(ids));
	}
	
}
