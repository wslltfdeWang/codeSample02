var dic = require("../../../../utils/dic.js"), 
    util = require("../../../../utils/util.js"), 
    API_demand_save = require("../../../../utils/config.js").API_demand_save,
    API_reportRecord_save = require("../../../../utils/config.js").API_reportRecord_save;
var app = getApp();

Page({
  onShareAppMessage() {
    return {
      title: '需求留言区',
      path: 'page/home/pages/demand-area/demand-area'
    }
  },
  data: {
    itemMap:{
       demandType:'需求类别',
       demandDesc:'需求描述'
    }
  },
  onLoad: function(e) {
        if (util.getValueArray({
            demandType:dic.demandType
        })); 
    },
  changeIndex:util.changeIndex,
  doNext: function(e) {
    const self = this
     //检验必填项
    if (util.checkRequired(e, self.data.itemMap)){
        app.getUserOpenId(function(err,openid){
          if (!err) {
              wx.showLoading({
                  title: '参谋长思考中',
                  mask: true
              })
             console.log(openid)
             var data = e.detail.value;
             data.demandTypeKey = self.data.demandTypeKey;
             data.openId = openid;
             wx.request({  
                url: API_demand_save,  
                data: data,  
                method: 'POST',  
                header: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                success: function(res){
                    wx.hideLoading()
                    if(res.data.code == 0){
                        console.log('保存需求留言区信息成功！')
                        wx.showModal({
                            title: '提示',
                            content: '您的需求提交成功，我们会尽快与您联系!',
                            showCancel: false,
                            confirmText: '确定',
                            success: function (res) {
                                if (res.confirm) {
                                    wx.switchTab({
                                      url: '/page/home/index'
                                    })
                                }
                            }
                        })
                    }else{
                       console.log('保存惬意用户-需求信息成功失败！')
                       util.showWarn('保存失败！原因：'+res.data.msg);
                    }
                }
              });
          }else{
            console.log('获取openid失败')
            util.showWarn('app.getUserOpenId 失败！');
          }
        })
     }
  },
  goBack: function() {
      wx.navigateBack({
          delta: 1
      });
  },
  man: function (e) {

        //报告结果ID
        var resultId = -777;
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
                data.reportType = '0'; //0:贷款方案；1：政府贴息；2：保险业务
                data.customerType='2'; //用户类型（0：不区分，1：个人用户，2：企业用户）
                data.dealType='1'; //0:立即办理，1：人工方案
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
                                url: '../loan-result/loan-result'
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