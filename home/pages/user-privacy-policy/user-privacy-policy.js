var app = getApp();

Page({
  onLoad(options) {
    const self = this
    var typeId = options.typeId; // 0：用户协议，1：隐私政策
    if(typeId == 0){
      self.setData({
        url:'https://golden-adviser.com/policy/userAgreement'
      })
    }else if(typeId == 1){
      wx.setNavigationBarTitle({
        title: '隐私政策'
      })
      self.setData({
        url:'https://golden-adviser.com/policy/privacyAgreement'
      })
    }
  },
  onShow() {  
    wx.reportAnalytics('enter_home_programmatically', {})
  },
  data: {
      url:"",
      typeId:""
  },
})
