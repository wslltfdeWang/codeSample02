Page({
  onShareAppMessage() {
    return {
      title: '信息录入',
      path: 'page/home/pages/info-entry/info-entry'
    }
  },
  personal:function(){
    console.log('personal');
    wx.redirectTo({
      url: '../personal-need-info/personal-need-info' 
	  })
  },
  enterprise:function(){
    console.log('enterprise');
    // wx.showModal({
    //   title: '抱歉',
    //   content: '正在构建企业用户服务中\n敬请期待...',
    //   showCancel: false,
    //   confirmText: '返回',
    //   success: function (res) {
    //     if (res.confirm) {
    //       console.log('用户点击了“返回授权”')
    //     }
    //   }
    // })
    wx.redirectTo({
      url: '../enterprise-need-info/enterprise-need-info' 
    })
  }
})
