package com.ruoyi.web.controller.tool;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.exception.BusinessServiceException;
import com.ruoyi.common.utils.ConstantUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.WechatConfUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.loan.service.IPersLoanService;
import com.ruoyi.system.domain.Demand;
import com.ruoyi.system.domain.News;
import com.ruoyi.system.domain.PersBasicInfo;
import com.ruoyi.system.domain.ReportRecord;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.service.IApiService;
import com.ruoyi.system.service.IDemandService;
import com.ruoyi.system.service.INewsService;
import com.ruoyi.system.service.IPersBasicInfoService;
import com.ruoyi.system.service.IPersFinanceInfoService;
import com.ruoyi.system.service.IReportRecordService;
import com.ruoyi.system.service.IWxUserService;
import com.ruoyi.system.vo.ComInfoRequestVO;
import com.ruoyi.system.vo.LoanDetailVO;
import com.ruoyi.system.vo.PersAssetsInfoVO;
import com.ruoyi.system.vo.PersFinanceInfoVO;
import com.ruoyi.system.vo.PersLoanRequestDTO;
import com.ruoyi.system.vo.PersLoanResponseDTO;
import com.ruoyi.system.vo.PersNeedInfoVO;
import com.ruoyi.system.vo.RecommendLoanResDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Anf&Villy
 */
@Api("api接口管理")
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private IApiService iApiService;
    @Autowired
    private IWxUserService wxUserService;
    @Autowired
    private IPersBasicInfoService persBasicInfoService;
    @Autowired
    private IPersFinanceInfoService persFinanceInfoService;
    @Autowired
    private IPersLoanService persLoanService;
    @Autowired
    private IReportRecordService reportRecordService;
    @Autowired
    INewsService newsService;
    @Autowired
    IDemandService demandService;

    /**
     * 获得sessionId
     */
    @ApiOperation("获得sessionId")
    @GetMapping("/getSessionId")
    @ResponseBody
    public Object getSessionId(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            return session.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @ApiOperation("获取openId")
    @GetMapping("/getOpenId")
    public AjaxResult getOpenId(String code) {
        if (StringUtils.isEmpty(code)) {
            return error();
        }

        String apiUrl = "https://api.weixin.qq.com/sns/jscode2session";
        String appid = Global.getAppid();
        String secret = Global.getSecret();
        String param = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String result = HttpUtils.sendGet(apiUrl, param);

        if (!StringUtils.isEmpty(result)) {
            JSONObject resultJson = JSONObject.parseObject(result);
            String openid = (String) resultJson.get("openid");
            String session_key = (String) resultJson.get("session_key");
            if (!StringUtils.isEmpty(openid)) {
                return success().put("openid", openid).put("session_key", session_key);
            } else {
                return error("获取openid失败");
            }
        } else {
            return error("调用jscode2session接口失败");
        }
    }

    @ApiOperation("根据openId获取微信用户")
    @GetMapping("/getWxUserByOpenId")
    public AjaxResult getWxUserByOpenId(String openId) {
        WxUser wxUser = wxUserService.selectWxUserByOpenId(openId);
        //只要wxUser不为null就返回成功，至于这个用户是否授权或是否注册，由前端判断
        if (wxUser != null) {
            return success().put("wxUser", wxUser);
        } else {
            return error();
        }
    }

    @ApiOperation("保存微信用户")
    @PostMapping("/wxUser/save")
    public AjaxResult save(WxUser wxUser, HttpServletRequest request) {
        if (StringUtils.isEmpty(wxUser.getOpenId())) {
            return error(ConstantUtil.GET_NO_OPENID_ERROR);
        }
        //如果验证码不为空  则验证验证码是否正确
        if (StringUtils.isNotEmpty(wxUser.getSmsCode())) {
            //判断手机号是否唯一
            WxUser wxUserTelNum = wxUserService.selectWxUserByTelNum(wxUser.getTelNum());
            if (wxUserTelNum != null) {
                //防止由于网络原因，已经注册用户且绑定了手机号，又跳转到注册页面
                if (wxUser.getTelNum().equals(wxUserTelNum.getTelNum())
                        && !wxUser.getOpenId().equals(wxUserTelNum.getOpenId())) {
                    return error("该手机号已经注册过！请更换一个手机号，重试！");
                }
            }
            String smsCode = (String) request.getSession().getAttribute("SMS_CODE");
            Long sendTime = (Long) request.getSession().getAttribute("SEND_TIME");
            if (!wxUser.getSmsCode().equals(smsCode)) {
                return error("验证码不正确！");
            }
            if ((System.currentTimeMillis() - sendTime) > 1000 * 60 * 5) {
                return error("验证码失效！");
            }
        }

        WxUser wxUser1 = wxUserService.selectWxUserByOpenId(wxUser.getOpenId());
        if (wxUser1 == null) {
            wxUser.setCreateBy("-1");
            wxUser.setCreateTime(new Date());
            wxUser.setUpdateBy("-1");
            wxUser.setUpdateTime(new Date());
            //生成推荐码
            AjaxResult gernerateQrResult = this.generateReferQrByOpenId(wxUser.getOpenId());
            if (gernerateQrResult.get("code").equals(0)) {
                String referQrUrl = (String) gernerateQrResult.get("referQrUrl");
                if (StringUtils.isNotEmpty(referQrUrl)) {
                    wxUser.setReferQrUrl(referQrUrl);
                }
            }
            //增加一次openId验证，防止因网络问题导致重复写入用户
            try {
                return wxUserService.insertWxUser(wxUser) > 0 ? success() : error();
            } catch (BusinessServiceException e) {
                return error(e.getMessage());
            }
        } else {
            wxUser.setUpdateBy("-1");
            wxUser.setUpdateTime(new Date());
            wxUser.setFpUserId(wxUser1.getFpUserId());
            //若当前用户的推荐人不为空，则将推荐人openid设为null，避免覆盖推荐人
            if (StringUtils.isNotEmpty(wxUser1.getReferOpenId())
                    && StringUtils.isNotEmpty(wxUser.getReferOpenId())) {
                wxUser.setReferOpenId(null);
            }
            //生成推荐码
            AjaxResult gernerateQrResult = this.generateReferQrByOpenId(wxUser.getOpenId());
            if (gernerateQrResult.get("code").equals(0)) {
                String referQrUrl = (String) gernerateQrResult.get("referQrUrl");
                if (StringUtils.isNotEmpty(referQrUrl)) {
                    wxUser.setReferQrUrl(referQrUrl);
                }
            }
            return wxUserService.updateWxUser(wxUser) > 0 ? success() : error();
        }
    }

    @ApiOperation("保存个人基础信息")
    @PostMapping("/persBasicInfo/save")
    public AjaxResult save(PersBasicInfo persBasicInfo) {
        if (StringUtils.isEmpty(persBasicInfo.getOpenId())) {
            return error(ConstantUtil.ADD_NO_OPENID_ERROR);
        }
        PersBasicInfo persBasicInfo1 = persBasicInfoService.selectPersBasicInfoByOpenId(persBasicInfo.getOpenId());
        if (persBasicInfo1 == null) {
            persBasicInfo.setCreateBy("-1");
            persBasicInfo.setCreateTime(new Date());
            persBasicInfo.setUpdateBy("-1");
            persBasicInfo.setUpdateTime(new Date());
            return persBasicInfoService.insertPersBasicInfo(persBasicInfo) > 0 ? success() : error();
        } else {
            persBasicInfo.setUpdateBy("-1");
            persBasicInfo.setUpdateTime(new Date());
            persBasicInfo.setPersBasicInfoId(persBasicInfo1.getPersBasicInfoId());
            return persBasicInfoService.updatePersBasicInfo(persBasicInfo) > 0 ? success() : error();


        }
    }

    @ApiOperation("保存个人需求信息（统一只做新增不做更新）")
    @PostMapping("/persNeedInfo/save")
    public AjaxResult save(PersNeedInfoVO persNeedInfoVO) {
        try {
            validOpenId(persNeedInfoVO);
        } catch (BusinessServiceException e) {
            return error(e.getMessage());
        }

        try {
            return iApiService.save(persNeedInfoVO);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("保存个人财务信息")
    @PostMapping("/persFinanceInfo/save")
    public AjaxResult save(PersFinanceInfoVO persFinanceInfoVO) {
        try {
            validOpenId(persFinanceInfoVO);
        } catch (BusinessServiceException e) {
            return error(e.getMessage());
        }

        List<PersFinanceInfoVO> persFinanceInfoVOList = persFinanceInfoService.selectFinanceInfoVOByUserId(persFinanceInfoVO);
        if (persFinanceInfoVOList.size() == 0) {
            persFinanceInfoVO.setCreateBy("-1");
            persFinanceInfoVO.setCreateTime(new Date());
            persFinanceInfoVO.setUpdateBy("-1");
            persFinanceInfoVO.setUpdateTime(new Date());
            return persFinanceInfoService.insertPersFinanceInfo(persFinanceInfoVO) > 0 ? success() : error();
        } else if (persFinanceInfoVOList.size() > 1) {
            return error(ConstantUtil.DUP_DATA_ERROR);
        } else {
            persFinanceInfoVO.setUpdateBy("-1");
            persFinanceInfoVO.setUpdateTime(new Date());
            persFinanceInfoVO.setPersFinanceInfoId(persFinanceInfoVOList.get(0).getPersFinanceInfoId());
            return persFinanceInfoService.updatePersFinanceInfoAll(persFinanceInfoVO) > 0 ? success() : error();
        }
    }

    @ApiOperation("保存个人详细财务信息")
    @PostMapping("/persFinanceDetail/save")
    public AjaxResult savePersFinanceDetail(@RequestBody LoanDetailVO loanDetailVO) {
        try {
            validOpenId(loanDetailVO);
            this.iApiService.savePersFinanceDetail(loanDetailVO);
        } catch (BusinessServiceException e) {
            return error("保存个人财务信息失败!");
        }
        return success();
    }

    @ApiOperation("保存个人资产信息")
    @PostMapping("/persAssetsInfo/save")
    public AjaxResult save(@RequestBody PersAssetsInfoVO persAssetsInfoVO) {
        try {
            validOpenId(persAssetsInfoVO);
        } catch (BusinessServiceException e) {
            return error(e.getMessage());
        }

        try {
            return iApiService.save(persAssetsInfoVO);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    @ApiOperation("根据openId获取匹配产品列表")
    @GetMapping("/getLoanList")
    public AjaxResult getLoanInfo(String openId) {
        PersLoanRequestDTO persLoanRequestDTO = new PersLoanRequestDTO();
        persLoanRequestDTO.setOpenId(openId);
        PersLoanResponseDTO responseDTO;
        try {
            validOpenId(persLoanRequestDTO);
        } catch (BusinessServiceException e) {
            return error(e.getMessage());
        }
        try {
            responseDTO = this.persLoanService.getLoans(persLoanRequestDTO);
        } catch (BusinessServiceException e) {
            return error(e.getMessage());
        }
        return success().put("loanResponse", responseDTO);
    }

    @ApiOperation("根据openId产生推荐码")
    @GetMapping("/generateReferQrByOpenId")
    public AjaxResult generateReferQrByOpenId(String openId) {
        //若当前用户已经生成过推荐码 则直接返回
        WxUser wxUser = wxUserService.selectWxUserByOpenId(openId);
        if (wxUser != null && wxUser.getReferQrUrl() != null && StringUtils.isNotEmpty(wxUser.getReferQrUrl())) {
            //判断文件是否存在
            String filepath = Global.getProfilePath() + wxUser.getReferQrUrl();
            File file = new File(filepath);
            if (file.exists()) {
                return success().put("referQrUrl", wxUser.getReferQrUrl());
            }
        }

        String accessToken = null;
        String apiUrl = "https://api.weixin.qq.com/cgi-bin/token";
        String appid = Global.getAppid();
        String secret = Global.getSecret();
        String param = "grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        String result = HttpUtils.sendGet(apiUrl, param);
        if (!StringUtils.isEmpty(result)) {
            JSONObject resultJson = JSONObject.parseObject(result);
            accessToken = (String) resultJson.get("access_token");
            if (!StringUtils.isEmpty(accessToken)) {
                return iApiService.generateReferQrByOpenId(openId, accessToken);
            } else {
                return error("获取accessToken失败");
            }
        } else {
            return error("获取accessToken失败");
        }

    }

    @ApiOperation("获取手机验证码")
    @PostMapping("/wxUser/getSmsCode")
    public AjaxResult getSmsCode(String telNum, HttpServletRequest request) {
        try {
            //数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            String code = createCharacter();
            String[] params = {code};

            SmsSingleSender ssender = new SmsSingleSender(WechatConfUtil.APP_ID_SMS, WechatConfUtil.APP_KEY_SMS);
            // 签名参数未提供或者为空时，会使用默认签名发送短信
            SmsSingleSenderResult result = ssender.sendWithParam("86", telNum,
                    291342, params, "金参谋", "", "");
            //将验证码写入session
            request.getSession().setAttribute("SMS_CODE", code);
            request.getSession().setAttribute("SEND_TIME", System.currentTimeMillis());

            return success().put("result", result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
            return error();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
            return error();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
            return error();
        }
    }


    /**
     * 生成短信验证码
     *
     * @return 短信验证码
     */
    private static String createCharacter() {
        char[] codeSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
            s.append(r);
        }
        return s.toString();
    }

    @ApiOperation("保存报告记录信息")
    @PostMapping("/reportRecord/save")
    public AjaxResult save(ReportRecord reportRecord) {
        //1.判断openId是否为空
        if (StringUtils.isEmpty(reportRecord.getOpenId())) {
            return error(ConstantUtil.GET_NO_OPENID_ERROR);
        }
        //2。判断该微信用户在微信用户表是否存在
        WxUser wxUser1 = wxUserService.selectWxUserByOpenId(reportRecord.getOpenId());
        if (wxUser1 == null) {
            return error("该微信用户不存在,保存报告记录失败！");
        }
        //3、保存报告记录表
        reportRecord.setFpUserId(wxUser1.getFpUserId());
        reportRecord.setCreateBy(reportRecord.getOpenId());
        reportRecord.setCreateTime(new Date());
        reportRecord.setUpdateBy(reportRecord.getOpenId());
        reportRecord.setUpdateTime(new Date());
        return reportRecordService.insertReportRecord(reportRecord) > 0 ? success() : error();
    }

    @ApiOperation("获取当前爆款产品列表")
    @GetMapping("/getRecommendLoanList")
    public AjaxResult getRecommendLoanList(String openId) {
        PersLoanRequestDTO persLoanRequestDTO = new PersLoanRequestDTO();
        persLoanRequestDTO.setOpenId(openId);
        try {
            validOpenId(persLoanRequestDTO);
        } catch (BusinessServiceException e) {
            return error(e.getMessage());
        }
        RecommendLoanResDTO responseDTO;
        try {
            responseDTO = this.persLoanService.getRecommendLoans(persLoanRequestDTO);
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return success().put("loanResponse", responseDTO);
    }

    /**
     * 分页查询新闻资讯列表
     */
    @GetMapping("/getNewslistByPage")
    @ResponseBody
    public AjaxResult getNewslistByPage(News news, PageDomain pageDomain) {
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNull(pageNum) || StringUtils.isNull(pageSize)) {
            return error("分页参数不正确");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsService.selectNewsList(news);
        PageInfo<News> pageInfo = new PageInfo<>(list, pageSize);

        return success().put("pageInfo", pageInfo);
    }

    /**
     * 根据新闻ID查询新闻
     */
    @GetMapping("/getNewsById")
    @ResponseBody
    public AjaxResult getNewsById(Integer newsId) {

        if (StringUtils.isNull(newsId)) {
            return error("新闻ID为空");
        }

        News news = newsService.selectNewsById(newsId);
        if (news != null) {
            return success().put("news", news);
        } else {
            return error("该新闻不存在");
        }
    }

    @ApiOperation("保存企业基本信息及需求")
    @PostMapping("/comNeedInfo/save")
    public AjaxResult saveComNeed(@RequestBody ComInfoRequestVO comInfoRequestVO) {
        try {
            validOpenId(comInfoRequestVO);
        } catch (BusinessServiceException e) {
            return error(e.getMessage());
        }
        try {
            return iApiService.saveComNeed(comInfoRequestVO);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }
    
    @ApiOperation("保存需求留言区信息")
    @PostMapping("/demand/save")
    public AjaxResult save(Demand demand) {
        //1.判断openId是否为空
        if (StringUtils.isEmpty(demand.getOpenId())) {
            return error(ConstantUtil.GET_NO_OPENID_ERROR);
        }
        //2。判断该微信用户在微信用户表是否存在
        WxUser wxUser1 = wxUserService.selectWxUserByOpenId(demand.getOpenId());
        if (wxUser1 == null) {
            return error("该微信用户不存在,保存报告记录失败！");
        }
        //3、保存报告记录表
        demand.setFpUserId(wxUser1.getFpUserId());
        demand.setCreateBy(demand.getOpenId());
        demand.setCreateTime(new Date());
        demand.setUpdateBy(demand.getOpenId());
        demand.setUpdateTime(new Date());
        return demandService.insertDemand(demand) > 0 ? success() : error();
    }
}
