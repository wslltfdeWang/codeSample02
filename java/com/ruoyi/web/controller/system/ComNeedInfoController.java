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
import com.ruoyi.system.domain.ComNeedInfo;
import com.ruoyi.system.service.IComNeedInfoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 公司需求 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-04-27
 */
@Controller
@RequestMapping("/system/comNeedInfo")
public class ComNeedInfoController extends BaseController
{
    private String prefix = "system/comNeedInfo";
	
	@Autowired
	private IComNeedInfoService comNeedInfoService;
	
	@RequiresPermissions("system:comNeedInfo:view")
	@GetMapping()
	public String comNeedInfo()
	{
	    return prefix + "/comNeedInfo";
	}
	
	/**
	 * 查询公司需求列表
	 */
	@RequiresPermissions("system:comNeedInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ComNeedInfo comNeedInfo)
	{
		startPage();
        List<ComNeedInfo> list = comNeedInfoService.selectComNeedInfoList(comNeedInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出公司需求列表
	 */
	@RequiresPermissions("system:comNeedInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ComNeedInfo comNeedInfo)
    {
    	List<ComNeedInfo> list = comNeedInfoService.selectComNeedInfoList(comNeedInfo);
        ExcelUtil<ComNeedInfo> util = new ExcelUtil<ComNeedInfo>(ComNeedInfo.class);
        return util.exportExcel(list, "comNeedInfo");
    }
	
	/**
	 * 新增公司需求
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存公司需求
	 */
	@RequiresPermissions("system:comNeedInfo:add")
	@Log(title = "公司需求", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ComNeedInfo comNeedInfo)
	{		
		return toAjax(comNeedInfoService.insertComNeedInfo(comNeedInfo));
	}

	/**
	 * 修改公司需求
	 */
	@GetMapping("/edit/{comNeedInfoId}")
	public String edit(@PathVariable("comNeedInfoId") Integer comNeedInfoId, ModelMap mmap)
	{
		ComNeedInfo comNeedInfo = comNeedInfoService.selectComNeedInfoById(comNeedInfoId);
		mmap.put("comNeedInfo", comNeedInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存公司需求
	 */
	@RequiresPermissions("system:comNeedInfo:edit")
	@Log(title = "公司需求", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ComNeedInfo comNeedInfo)
	{		
		return toAjax(comNeedInfoService.updateComNeedInfo(comNeedInfo));
	}
	
	/**
	 * 删除公司需求
	 */
	@RequiresPermissions("system:comNeedInfo:remove")
	@Log(title = "公司需求", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(comNeedInfoService.deleteComNeedInfoByIds(ids));
	}
	
}
