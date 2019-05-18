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
import com.ruoyi.system.domain.PersBasicInfo;
import com.ruoyi.system.service.IPersBasicInfoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 个人基本 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-12
 */
@Controller
@RequestMapping("/system/persBasicInfo")
public class PersBasicInfoController extends BaseController
{
    private String prefix = "system/persBasicInfo";
	
	@Autowired
	private IPersBasicInfoService persBasicInfoService;
	
	@RequiresPermissions("system:persBasicInfo:view")
	@GetMapping()
	public String persBasicInfo()
	{
	    return prefix + "/persBasicInfo";
	}
	
	/**
	 * 查询个人基本列表
	 */
	@RequiresPermissions("system:persBasicInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PersBasicInfo persBasicInfo)
	{
		startPage();
        List<PersBasicInfo> list = persBasicInfoService.selectPersBasicInfoList(persBasicInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出个人基本列表
	 */
	@RequiresPermissions("system:persBasicInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PersBasicInfo persBasicInfo)
    {
    	List<PersBasicInfo> list = persBasicInfoService.selectPersBasicInfoList(persBasicInfo);
        ExcelUtil<PersBasicInfo> util = new ExcelUtil<PersBasicInfo>(PersBasicInfo.class);
        return util.exportExcel(list, "persBasicInfo");
    }
	
	/**
	 * 新增个人基本
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存个人基本
	 */
	@RequiresPermissions("system:persBasicInfo:add")
	@Log(title = "个人基本", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PersBasicInfo persBasicInfo)
	{		
		return toAjax(persBasicInfoService.insertPersBasicInfo(persBasicInfo));
	}

	/**
	 * 修改个人基本
	 */
	@GetMapping("/edit/{persBasicInfoId}")
	public String edit(@PathVariable("persBasicInfoId") Integer persBasicInfoId, ModelMap mmap)
	{
		PersBasicInfo persBasicInfo = persBasicInfoService.selectPersBasicInfoById(persBasicInfoId);
		mmap.put("persBasicInfo", persBasicInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人基本
	 */
	@RequiresPermissions("system:persBasicInfo:edit")
	@Log(title = "个人基本", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PersBasicInfo persBasicInfo)
	{		
		return toAjax(persBasicInfoService.updatePersBasicInfo(persBasicInfo));
	}
	
	/**
	 * 删除个人基本
	 */
	@RequiresPermissions("system:persBasicInfo:remove")
	@Log(title = "个人基本", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(persBasicInfoService.deletePersBasicInfoByIds(ids));
	}
	
}
