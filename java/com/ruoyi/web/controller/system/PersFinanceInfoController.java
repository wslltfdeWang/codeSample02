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
import com.ruoyi.system.domain.PersFinanceInfo;
import com.ruoyi.system.service.IPersFinanceInfoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 个人财务 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-12
 */
@Controller
@RequestMapping("/system/persFinanceInfo")
public class PersFinanceInfoController extends BaseController
{
    private String prefix = "system/persFinanceInfo";
	
	@Autowired
	private IPersFinanceInfoService persFinanceInfoService;
	
	@RequiresPermissions("system:persFinanceInfo:view")
	@GetMapping()
	public String persFinanceInfo()
	{
	    return prefix + "/persFinanceInfo";
	}
	
	/**
	 * 查询个人财务列表
	 */
	@RequiresPermissions("system:persFinanceInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PersFinanceInfo persFinanceInfo)
	{
		startPage();
        List<PersFinanceInfo> list = persFinanceInfoService.selectPersFinanceInfoList(persFinanceInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出个人财务列表
	 */
	@RequiresPermissions("system:persFinanceInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PersFinanceInfo persFinanceInfo)
    {
    	List<PersFinanceInfo> list = persFinanceInfoService.selectPersFinanceInfoList(persFinanceInfo);
        ExcelUtil<PersFinanceInfo> util = new ExcelUtil<PersFinanceInfo>(PersFinanceInfo.class);
        return util.exportExcel(list, "persFinanceInfo");
    }
	
	/**
	 * 新增个人财务
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存个人财务
	 */
	@RequiresPermissions("system:persFinanceInfo:add")
	@Log(title = "个人财务", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PersFinanceInfo persFinanceInfo)
	{		
		return toAjax(persFinanceInfoService.insertPersFinanceInfo(persFinanceInfo));
	}

	/**
	 * 修改个人财务
	 */
	@GetMapping("/edit/{persFinanceInfoId}")
	public String edit(@PathVariable("persFinanceInfoId") Integer persFinanceInfoId, ModelMap mmap)
	{
		PersFinanceInfo persFinanceInfo = persFinanceInfoService.selectPersFinanceInfoById(persFinanceInfoId);
		mmap.put("persFinanceInfo", persFinanceInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人财务
	 */
	@RequiresPermissions("system:persFinanceInfo:edit")
	@Log(title = "个人财务", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PersFinanceInfo persFinanceInfo)
	{		
		return toAjax(persFinanceInfoService.updatePersFinanceInfo(persFinanceInfo));
	}
	
	/**
	 * 删除个人财务
	 */
	@RequiresPermissions("system:persFinanceInfo:remove")
	@Log(title = "个人财务", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(persFinanceInfoService.deletePersFinanceInfoByIds(ids));
	}
	
}
