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
import com.ruoyi.system.domain.PersNeedInfo;
import com.ruoyi.system.service.IPersNeedInfoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 个人需求 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-12
 */
@Controller
@RequestMapping("/system/persNeedInfo")
public class PersNeedInfoController extends BaseController
{
    private String prefix = "system/persNeedInfo";
	
	@Autowired
	private IPersNeedInfoService persNeedInfoService;
	
	@RequiresPermissions("system:persNeedInfo:view")
	@GetMapping()
	public String persNeedInfo()
	{
	    return prefix + "/persNeedInfo";
	}
	
	/**
	 * 查询个人需求列表
	 */
	@RequiresPermissions("system:persNeedInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PersNeedInfo persNeedInfo)
	{
		startPage();
        List<PersNeedInfo> list = persNeedInfoService.selectPersNeedInfoList(persNeedInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出个人需求列表
	 */
	@RequiresPermissions("system:persNeedInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PersNeedInfo persNeedInfo)
    {
    	List<PersNeedInfo> list = persNeedInfoService.selectPersNeedInfoList(persNeedInfo);
        ExcelUtil<PersNeedInfo> util = new ExcelUtil<PersNeedInfo>(PersNeedInfo.class);
        return util.exportExcel(list, "persNeedInfo");
    }
	
	/**
	 * 新增个人需求
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存个人需求
	 */
	@RequiresPermissions("system:persNeedInfo:add")
	@Log(title = "个人需求", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PersNeedInfo persNeedInfo)
	{		
		return toAjax(persNeedInfoService.insertPersNeedInfo(persNeedInfo));
	}

	/**
	 * 修改个人需求
	 */
	@GetMapping("/edit/{persNeedInfoId}")
	public String edit(@PathVariable("persNeedInfoId") Integer persNeedInfoId, ModelMap mmap)
	{
		PersNeedInfo persNeedInfo = persNeedInfoService.selectPersNeedInfoById(persNeedInfoId);
		mmap.put("persNeedInfo", persNeedInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人需求
	 */
	@RequiresPermissions("system:persNeedInfo:edit")
	@Log(title = "个人需求", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PersNeedInfo persNeedInfo)
	{		
		return toAjax(persNeedInfoService.updatePersNeedInfo(persNeedInfo));
	}
	
	/**
	 * 删除个人需求
	 */
	@RequiresPermissions("system:persNeedInfo:remove")
	@Log(title = "个人需求", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(persNeedInfoService.deletePersNeedInfoByIds(ids));
	}
	
}
