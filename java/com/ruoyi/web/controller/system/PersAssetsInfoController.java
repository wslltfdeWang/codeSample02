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
import com.ruoyi.system.domain.PersAssetsInfo;
import com.ruoyi.system.service.IPersAssetsInfoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 个人资产 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-12
 */
@Controller
@RequestMapping("/system/persAssetsInfo")
public class PersAssetsInfoController extends BaseController
{
    private String prefix = "system/persAssetsInfo";
	
	@Autowired
	private IPersAssetsInfoService persAssetsInfoService;
	
	@RequiresPermissions("system:persAssetsInfo:view")
	@GetMapping()
	public String persAssetsInfo()
	{
	    return prefix + "/persAssetsInfo";
	}
	
	/**
	 * 查询个人资产列表
	 */
	@RequiresPermissions("system:persAssetsInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PersAssetsInfo persAssetsInfo)
	{
		startPage();
        List<PersAssetsInfo> list = persAssetsInfoService.selectPersAssetsInfoList(persAssetsInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出个人资产列表
	 */
	@RequiresPermissions("system:persAssetsInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PersAssetsInfo persAssetsInfo)
    {
    	List<PersAssetsInfo> list = persAssetsInfoService.selectPersAssetsInfoList(persAssetsInfo);
        ExcelUtil<PersAssetsInfo> util = new ExcelUtil<PersAssetsInfo>(PersAssetsInfo.class);
        return util.exportExcel(list, "persAssetsInfo");
    }
	
	/**
	 * 新增个人资产
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存个人资产
	 */
	@RequiresPermissions("system:persAssetsInfo:add")
	@Log(title = "个人资产", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PersAssetsInfo persAssetsInfo)
	{		
		return toAjax(persAssetsInfoService.insertPersAssetsInfo(persAssetsInfo));
	}

	/**
	 * 修改个人资产
	 */
	@GetMapping("/edit/{persAssetsInfoId}")
	public String edit(@PathVariable("persAssetsInfoId") Integer persAssetsInfoId, ModelMap mmap)
	{
		PersAssetsInfo persAssetsInfo = persAssetsInfoService.selectPersAssetsInfoById(persAssetsInfoId);
		mmap.put("persAssetsInfo", persAssetsInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人资产
	 */
	@RequiresPermissions("system:persAssetsInfo:edit")
	@Log(title = "个人资产", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PersAssetsInfo persAssetsInfo)
	{		
		return toAjax(persAssetsInfoService.updatePersAssetsInfo(persAssetsInfo));
	}
	
	/**
	 * 删除个人资产
	 */
	@RequiresPermissions("system:persAssetsInfo:remove")
	@Log(title = "个人资产", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(persAssetsInfoService.deletePersAssetsInfoByIds(ids));
	}
	
}
