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
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.service.IWxUserService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 微信用户 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-12
 */
@Controller
@RequestMapping("/system/wxUser")
public class WxUserController extends BaseController
{
    private String prefix = "system/wxUser";
	
	@Autowired
	private IWxUserService wxUserService;
	
	@RequiresPermissions("system:wxUser:view")
	@GetMapping()
	public String wxUser()
	{
	    return prefix + "/wxUser";
	}
	
	/**
	 * 查询微信用户列表
	 */
	@RequiresPermissions("system:wxUser:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(WxUser wxUser)
	{
		startPage();
        List<WxUser> list = wxUserService.selectWxUserList(wxUser);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出微信用户列表
	 */
	@RequiresPermissions("system:wxUser:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WxUser wxUser)
    {
    	List<WxUser> list = wxUserService.selectWxUserList(wxUser);
        ExcelUtil<WxUser> util = new ExcelUtil<WxUser>(WxUser.class);
        return util.exportExcel(list, "wxUser");
    }
	
	/**
	 * 新增微信用户
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存微信用户
	 */
	@RequiresPermissions("system:wxUser:add")
	@Log(title = "微信用户", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(WxUser wxUser)
	{
		try {
			return toAjax(wxUserService.insertWxUser(wxUser));
		} catch (BusinessServiceException e) {
			return error(e.getMessage());
		}
	}

	/**
	 * 修改微信用户
	 */
	@GetMapping("/edit/{fpUserId}")
	public String edit(@PathVariable("fpUserId") Integer fpUserId, ModelMap mmap)
	{
		WxUser wxUser = wxUserService.selectWxUserById(fpUserId);
		mmap.put("wxUser", wxUser);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存微信用户
	 */
	@RequiresPermissions("system:wxUser:edit")
	@Log(title = "微信用户", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(WxUser wxUser)
	{		
		return toAjax(wxUserService.updateWxUser(wxUser));
	}
	
	/**
	 * 删除微信用户
	 */
	@RequiresPermissions("system:wxUser:remove")
	@Log(title = "微信用户", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(wxUserService.deleteWxUserByIds(ids));
	}
	
}
