var dic = require("../../../../utils/dic.js"),
    util = require("../../../../utils/util.js"),
    API_reportRecord_save = require("../../../../utils/config.js").API_reportRecord_save;
var app = getApp();

Page({
  onShareAppMessage() {
    return {
      title: '投资补助贷款补贴',
      path: 'page/home/pages/info-policy/info-policy'
    }
  },
  data: {
  },
  onLoad: function() {

     
  },
  man: function (e) {
        //报告结果ID
        var resultId = -888;
        app.getUserOpenId(function (err, openid) {
            if (!err) {
              wx.showLoading({
                  title: '参谋长思考中',
                  mask: true
              })
                console.log(openid)
                var data = {};
                data.openId = openid;
                data.reportResultId = resultId;
                data.reportType = '1'; //0:贷款方案；1：政府贴息；2：保险业务
                data.customerType='0'; //用户类型（0：不区分，1：个人用户，2：企业用户）
                data.dealType='2'; //0:立即办理，1：人工方案,2:预约咨询,3:方案定制预约（针对保险）
                wx.request({
                    url: API_reportRecord_save,
                    data: data,
                    method: 'POST',
                    header: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    success: function (res) {
                        wx.hideLoading()
                        if (res.data.code == 0) {
                            console.log('保存报告记录成功！')
                            wx.navigateTo({
                                url: '../loan-result/loan-result?dealType=2'
                            })

                        } else {
                            console.log('保存报告记录失败！')
                            util.showWarn('报告记录失败！原因：' + res.data.msg);
                        }
                    }
                });
            } else {
                console.log('获取openid失败')
            }
        })
    }

});
