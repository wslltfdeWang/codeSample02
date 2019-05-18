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
import com.ruoyi.system.domain.ReportRecord;
import com.ruoyi.system.service.IReportRecordService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;

/**
 * 报告记录 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-03-20
 */
@Controller
@RequestMapping("/system/reportRecord")
public class ReportRecordController extends BaseController
{
    private String prefix = "system/reportRecord";
	
	@Autowired
	private IReportRecordService reportRecordService;
	
	@RequiresPermissions("system:reportRecord:view")
	@GetMapping()
	public String reportRecord()
	{
	    return prefix + "/reportRecord";
	}

	/**
	 * 查询报告记录列表
	 */
	@RequiresPermissions("system:reportRecord:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ReportRecord reportRecord)
	{
		startPage();
        List<ReportRecord> list = reportRecordService.selectReportRecordList(reportRecord);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出报告记录列表
	 */
	@RequiresPermissions("system:reportRecord:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ReportRecord reportRecord)
    {
    	List<ReportRecord> list = reportRecordService.selectReportRecordList(reportRecord);
        ExcelUtil<ReportRecord> util = new ExcelUtil<ReportRecord>(ReportRecord.class);
        return util.exportExcel(list, "reportRecord");
    }
	
	/**
	 * 新增报告记录
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存报告记录
	 */
	@RequiresPermissions("system:reportRecord:add")
	@Log(title = "报告记录", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ReportRecord reportRecord)
	{		
		return toAjax(reportRecordService.insertReportRecord(reportRecord));
	}

	/**
	 * 修改报告记录
	 */
	@GetMapping("/edit/{fpReportRecordId}")
	public String edit(@PathVariable("fpReportRecordId") Integer fpReportRecordId, ModelMap mmap)
	{
		ReportRecord reportRecord = reportRecordService.selectReportRecordById(fpReportRecordId);
		mmap.put("reportRecord", reportRecord);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存报告记录
	 */
	@RequiresPermissions("system:reportRecord:edit")
	@Log(title = "报告记录", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ReportRecord reportRecord)
	{		
		return toAjax(reportRecordService.updateReportRecord(reportRecord));
	}
	
	/**
	 * 删除报告记录
	 */
	@RequiresPermissions("system:reportRecord:remove")
	@Log(title = "报告记录", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(reportRecordService.deleteReportRecordByIds(ids));
	}
	
}
