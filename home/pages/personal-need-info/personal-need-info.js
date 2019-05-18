var dic = require("../../../../utils/dic.js"), 
    util = require("../../../../utils/util.js"), 
    API_persNeedInfo_save = require("../../../../utils/config.js").API_persNeedInfo_save;
var app = getApp();

Page({
  onShareAppMessage() {
    return {
      title: '需求信息',
      path: 'page/home/pages/personal-need-info/personal-need-info'
    }
  },
  data: {
    itemMap:{
       amount:'金额',
       usage:'用途'
    }
  },
  onLoad: function(e) {
        if (util.getValueArray({
            usage:dic.usage
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
             data.usageKey = self.data.usageKey;
             data.openId = openid;
             wx.request({  
                url: API_persNeedInfo_save,  
                data: data,  
                method: 'POST',  
                header: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                success: function(res){
                    wx.hideLoading()
                    if(res.data.code == 0){
                        console.log('保存个人用户-需求信息成功！')
                        wx.navigateTo({
                            url: '../personal-finance-info/personal-finance-info' 
                          })
                       
                    }else{
                       console.log('保存个人用户-需求信息成功失败！')
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
  }
});