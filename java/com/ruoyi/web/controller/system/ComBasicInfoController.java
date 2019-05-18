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
import com.ruoyi.system.domain.ComBasicInfo;
import com.ruoyi.system.service.IComBasicInfoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 公司基本 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-04-27
 */
@Controller
@RequestMapping("/system/comBasicInfo")
public class ComBasicInfoController extends BaseController
{
    private String prefix = "system/comBasicInfo";
	
	@Autowired
	private IComBasicInfoService comBasicInfoService;
	
	@RequiresPermissions("system:comBasicInfo:view")
	@GetMapping()
	public String comBasicInfo()
	{
	    return prefix + "/comBasicInfo";
	}
	
	/**
	 * 查询公司基本列表
	 */
	@RequiresPermissions("system:comBasicInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ComBasicInfo comBasicInfo)
	{
		startPage();
        List<ComBasicInfo> list = comBasicInfoService.selectComBasicInfoList(comBasicInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出公司基本列表
	 */
	@RequiresPermissions("system:comBasicInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ComBasicInfo comBasicInfo)
    {
    	List<ComBasicInfo> list = comBasicInfoService.selectComBasicInfoList(comBasicInfo);
        ExcelUtil<ComBasicInfo> util = new ExcelUtil<ComBasicInfo>(ComBasicInfo.class);
        return util.exportExcel(list, "comBasicInfo");
    }
	
	/**
	 * 新增公司基本
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存公司基本
	 */
	@RequiresPermissions("system:comBasicInfo:add")
	@Log(title = "公司基本", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ComBasicInfo comBasicInfo)
	{		
		return toAjax(comBasicInfoService.insertComBasicInfo(comBasicInfo));
	}

	/**
	 * 修改公司基本
	 */
	@GetMapping("/edit/{comBasicInfoId}")
	public String edit(@PathVariable("comBasicInfoId") Integer comBasicInfoId, ModelMap mmap)
	{
		ComBasicInfo comBasicInfo = comBasicInfoService.selectComBasicInfoById(comBasicInfoId);
		mmap.put("comBasicInfo", comBasicInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存公司基本
	 */
	@RequiresPermissions("system:comBasicInfo:edit")
	@Log(title = "公司基本", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ComBasicInfo comBasicInfo)
	{		
		return toAjax(comBasicInfoService.updateComBasicInfo(comBasicInfo));
	}
	
	/**
	 * 删除公司基本
	 */
	@RequiresPermissions("system:comBasicInfo:remove")
	@Log(title = "公司基本", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(comBasicInfoService.deleteComBasicInfoByIds(ids));
	}
	
}
