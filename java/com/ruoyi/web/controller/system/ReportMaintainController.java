package com.ruoyi.web.controller.system;

import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.ExcelUtil;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.system.domain.ReportRecord;
import com.ruoyi.system.service.IReportMaintainService;
import com.ruoyi.system.vo.reportmaintan.ReportMaintainExportVO;
import com.ruoyi.system.vo.reportmaintan.ReportMaintainVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 报告记录 信息操作处理
 *
 * @author Villy
 * @date 2019-05-6
 */
@Controller
@RequestMapping("/system/reportMaintain")
public class ReportMaintainController extends BaseController {
    private String prefix = "system/reportMaintain";

    @Autowired
    private IReportMaintainService reportMaintainService;

    @RequiresPermissions("system:reportMaintain:view")
    @GetMapping()
    public String reportMaintain() {
        return prefix + "/reportMaintain";
    }

    /**
     * 查询报告记录列表
     */
    @RequiresPermissions("system:reportMaintain:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ReportMaintainVO reportMaintainVO) {
        if (reportMaintainVO.getCustomerType() != null && "".equals(reportMaintainVO.getCustomerType().trim())) {
            reportMaintainVO.setCustomerType("1");
        }
        startPage();
        List<ReportMaintainVO> list = reportMaintainService.selectReportMaintainList(reportMaintainVO);
        return getDataTable(list);
    }

    /**
     * 导出报告记录列表
     */
    @RequiresPermissions("system:reportMaintain:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ReportMaintainVO reportMaintainVO) {
        if (reportMaintainVO.getCustomerType() != null && "".equals(reportMaintainVO.getCustomerType().trim())) {
            reportMaintainVO.setCustomerType("1");
        }
        List<ReportMaintainExportVO> list = reportMaintainService.exportReportMaintainList(reportMaintainVO);
        if (list.size() == 0) {
            return error("未查询到数据");
        }
        ExcelUtil<ReportMaintainExportVO> util = new ExcelUtil<>(ReportMaintainExportVO.class);
        return util.exportExcel(list, "用户需求明细");
    }

}
