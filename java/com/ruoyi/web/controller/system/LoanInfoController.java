package com.ruoyi.web.controller.system;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessServiceException;
import com.ruoyi.common.utils.ExcelUtil;
import com.ruoyi.common.utils.RegexUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.system.domain.LoanInfo;
import com.ruoyi.system.service.ILoanInfoService;
import com.ruoyi.system.vo.PersLoanDTO;
import com.ruoyi.system.vo.PersLoanRequestDTO;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 产品总 信息操作处理
 *
 * @author ruoyi
 * @date 2019-01-21
 */
@Controller
@RequestMapping("/system/loanInfo")
public class LoanInfoController extends BaseController {
    private String prefix = "system/loanInfo";

    @Autowired
    private ILoanInfoService loanInfoService;

    @RequiresPermissions("system:loanInfo:view")
    @GetMapping()
    public String loanInfo() {
        return prefix + "/loanInfo";
    }

    /**
     * 查询产品总列表
     */
    @RequiresPermissions("system:loanInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LoanInfo loanInfo) {
        startPage();
        List<LoanInfo> list = loanInfoService.selectLoanInfoList(loanInfo);
        return getDataTable(list);
    }


    /**
     * 导出产品总列表
     */
    @RequiresPermissions("system:loanInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LoanInfo loanInfo) {
        List<LoanInfo> list = loanInfoService.selectLoanInfoList(loanInfo);
        ExcelUtil<LoanInfo> util = new ExcelUtil<LoanInfo>(LoanInfo.class);
        return util.exportExcel(list, "loanInfo");
    }

    /**
     * 新增产品总
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存产品总
     */
    @RequiresPermissions("system:loanInfo:add")
    @Log(title = "产品总", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LoanInfo loanInfo) {
        return toAjax(loanInfoService.insertLoanInfo(loanInfo));
    }

    /**
     * 修改产品总
     */
    @GetMapping("/edit/{loanInfoId}")
    public String edit(@PathVariable("loanInfoId") Integer loanInfoId, ModelMap mmap) {
        LoanInfo loanInfo = loanInfoService.selectLoanInfoById(loanInfoId);
        mmap.put("loanInfo", loanInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存产品总
     */
    @RequiresPermissions("system:loanInfo:edit")
    @Log(title = "产品总", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LoanInfo loanInfo) {
        return toAjax(loanInfoService.updateLoanInfo(loanInfo));
    }

    /**
     * 删除产品总
     */
    @RequiresPermissions("system:loanInfo:remove")
    @Log(title = "产品总", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(loanInfoService.deleteLoanInfoByIds(ids));
    }

    /**
     * 导入产品总
     */
    @GetMapping("/import")
    public String importSave() {
        return prefix + "/import";
    }

    /**
     * 导入保存产品总
     */
    @RequiresPermissions("system:loanInfo:import")
    @Log(title = "产品总", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    @ResponseBody
    public AjaxResult importSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);

        }
        /** 获取文件的后缀* */
        String fileName = multipartFile.getOriginalFilename();

        List<PersLoanDTO> persLoanDTOArrayList;

        if (!fileName.matches(RegexUtils.REGEX_XLS) && !fileName.matches(RegexUtils.REGEX_XLSX)) {
            return error("上传文件格式不正确");
        }

        try {
            ExcelUtil<PersLoanDTO> util = new ExcelUtil<>(PersLoanDTO.class);
            persLoanDTOArrayList = util.importExcel(multipartFile.getInputStream());
            loanInfoService.batchSave(persLoanDTOArrayList);
        } catch (IOException e) {
            e.printStackTrace();
            return error("解析excel文件失败");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO 完善导入
        return success();
    }
}
