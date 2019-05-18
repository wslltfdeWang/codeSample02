var dic = require("../../../../utils/dic.js"), 
    util = require("../../../../utils/util.js"), 
    API_generateReferQrByOpenId = require("../../../../utils/config.js").API_generateReferQrByOpenId,
    API_referQr = require("../../../../utils/config.js").API_referQr;

var app = getApp();

Page({
  onLoad(options) {
    const self = this
    var dealType = options.dealType;
    var messageUrl = '';
    //0:立即办理，1：人工方案,2:预约咨询,3:方案定制预约（针对保险）
    if(dealType == 0){
      messageUrl = API_referQr +"images/loan-result/deal-now.png";
    }else if(dealType == 1){
      messageUrl = API_referQr +"images/loan-result/appiont-plan.png";
    }else if(dealType == 2){
      messageUrl = API_referQr +"images/loan-result/appiont-consult.png";
    }else if(dealType == 3){
      messageUrl = API_referQr +"images/loan-result/appiont-customize.png";
    }

    self.setData({
        dealType:dealType,
        messageUrl:messageUrl
    })

    app.getUserOpenId(function(err,openid){
          if (!err) {
            wx.showLoading({
              title: '参谋长思考中',
              mask: true
            })

             console.log(openid)
             var data = {};
                 data.openId = openid;
             wx.request({  
                url: API_generateReferQrByOpenId,  
                data: data,  
                method: 'GET',  
                header: {
                    "Content-Type": "application/json;charset=UTF-8"
                },
                success: function(res){ 
                  //关闭遮罩
                  wx.hideLoading();
                    if(res.data.code == 0){
                        console.log('生成或者查询二维码成功！')
                        var referQrUrl = API_referQr + res.data.referQrUrl; 
                        app.globalData.userInfo.referQrUrl = referQrUrl;                                     
                        self.setData({
                          referQrUrl:referQrUrl
                        })
                       
                    }else{
                       console.log('生成或者查询二维码失败！')
                       util.showWarn('生成或者查询二维码失败！原因：'+res.data.msg);
                    }
                }
              });
          }else{
            console.log('获取openid失败')
            //关闭遮罩
            wx.hideLoading();

          }
      })
  },
  onShow() {  
    wx.reportAnalytics('enter_home_programmatically', {})
  },
  onShareAppMessage() {
    var dealType = this.data.dealType;
    return {
      title: '报告',
      path: 'page/home/pages/loan-result/loan-result?dealType='+dealType
    }
  },
  data: {
    referQrUrl:'',
    dealType:'', //0:立即办理，1：人工方案,2:预约咨询,3:方案定制预约（针对保险）
    messageUrl:''
  },
  goHome: function() {
      wx.switchTab({
        url: '/page/home/index'
      })
  }
})
