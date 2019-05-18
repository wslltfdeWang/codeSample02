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
import com.ruoyi.system.domain.PersCreditInfo;
import com.ruoyi.system.service.IPersCreditInfoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 个人征信 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-12
 */
@Controller
@RequestMapping("/system/persCreditInfo")
public class PersCreditInfoController extends BaseController
{
    private String prefix = "system/persCreditInfo";
	
	@Autowired
	private IPersCreditInfoService persCreditInfoService;
	
	@RequiresPermissions("system:persCreditInfo:view")
	@GetMapping()
	public String persCreditInfo()
	{
	    return prefix + "/persCreditInfo";
	}
	
	/**
	 * 查询个人征信列表
	 */
	@RequiresPermissions("system:persCreditInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PersCreditInfo persCreditInfo)
	{
		startPage();
        List<PersCreditInfo> list = persCreditInfoService.selectPersCreditInfoList(persCreditInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出个人征信列表
	 */
	@RequiresPermissions("system:persCreditInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PersCreditInfo persCreditInfo)
    {
    	List<PersCreditInfo> list = persCreditInfoService.selectPersCreditInfoList(persCreditInfo);
        ExcelUtil<PersCreditInfo> util = new ExcelUtil<PersCreditInfo>(PersCreditInfo.class);
        return util.exportExcel(list, "persCreditInfo");
    }
	
	/**
	 * 新增个人征信
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存个人征信
	 */
	@RequiresPermissions("system:persCreditInfo:add")
	@Log(title = "个人征信", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PersCreditInfo persCreditInfo)
	{		
		return toAjax(persCreditInfoService.insertPersCreditInfo(persCreditInfo));
	}

	/**
	 * 修改个人征信
	 */
	@GetMapping("/edit/{persCreditInfoId}")
	public String edit(@PathVariable("persCreditInfoId") Integer persCreditInfoId, ModelMap mmap)
	{
		PersCreditInfo persCreditInfo = persCreditInfoService.selectPersCreditInfoById(persCreditInfoId);
		mmap.put("persCreditInfo", persCreditInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人征信
	 */
	@RequiresPermissions("system:persCreditInfo:edit")
	@Log(title = "个人征信", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PersCreditInfo persCreditInfo)
	{		
		return toAjax(persCreditInfoService.updatePersCreditInfo(persCreditInfo));
	}
	
	/**
	 * 删除个人征信
	 */
	@RequiresPermissions("system:persCreditInfo:remove")
	@Log(title = "个人征信", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(persCreditInfoService.deletePersCreditInfoByIds(ids));
	}
	
}
