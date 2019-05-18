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
import com.ruoyi.system.domain.PersFinanceDetail;
import com.ruoyi.system.service.IPersFinanceDetailService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 个人财务详细 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-03-15
 */
@Controller
@RequestMapping("/system/persFinanceDetail")
public class PersFinanceDetailController extends BaseController
{
    private String prefix = "system/persFinanceDetail";
	
	@Autowired
	private IPersFinanceDetailService persFinanceDetailService;
	
	@RequiresPermissions("system:persFinanceDetail:view")
	@GetMapping()
	public String persFinanceDetail()
	{
	    return prefix + "/persFinanceDetail";
	}
	
	/**
	 * 查询个人财务详细列表
	 */
	@RequiresPermissions("system:persFinanceDetail:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PersFinanceDetail persFinanceDetail)
	{
		startPage();
        List<PersFinanceDetail> list = persFinanceDetailService.selectPersFinanceDetailList(persFinanceDetail);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出个人财务详细列表
	 */
	@RequiresPermissions("system:persFinanceDetail:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PersFinanceDetail persFinanceDetail)
    {
    	List<PersFinanceDetail> list = persFinanceDetailService.selectPersFinanceDetailList(persFinanceDetail);
        ExcelUtil<PersFinanceDetail> util = new ExcelUtil<PersFinanceDetail>(PersFinanceDetail.class);
        return util.exportExcel(list, "persFinanceDetail");
    }
	
	/**
	 * 新增个人财务详细
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存个人财务详细
	 */
	@RequiresPermissions("system:persFinanceDetail:add")
	@Log(title = "个人财务详细", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PersFinanceDetail persFinanceDetail)
	{		
		return toAjax(persFinanceDetailService.insertPersFinanceDetail(persFinanceDetail));
	}

	/**
	 * 修改个人财务详细
	 */
	@GetMapping("/edit/{persFinanceDetailId}")
	public String edit(@PathVariable("persFinanceDetailId") Integer persFinanceDetailId, ModelMap mmap)
	{
		PersFinanceDetail persFinanceDetail = persFinanceDetailService.selectPersFinanceDetailById(persFinanceDetailId);
		mmap.put("persFinanceDetail", persFinanceDetail);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人财务详细
	 */
	@RequiresPermissions("system:persFinanceDetail:edit")
	@Log(title = "个人财务详细", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PersFinanceDetail persFinanceDetail)
	{		
		return toAjax(persFinanceDetailService.updatePersFinanceDetail(persFinanceDetail));
	}
	
	/**
	 * 删除个人财务详细
	 */
	@RequiresPermissions("system:persFinanceDetail:remove")
	@Log(title = "个人财务详细", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(persFinanceDetailService.deletePersFinanceDetailByIds(ids));
	}
	
}
