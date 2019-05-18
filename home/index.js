var API_getNewslistByPage = require("../../utils/config.js").API_getNewslistByPage,
    API_referQr = require("../../utils/config.js").API_referQr;
var app = getApp();

Page({
  onLoad(query) {
    const self = this
    const scene = decodeURIComponent(query.scene);
    console.log("推荐人openid:"+scene);
    if(scene != "undefined"){
      console.log("保存scene:"+scene);
      //将推荐人openId放入缓存中
      wx.setStorageSync("referOpenId", scene);
    }
    //根据openid查询用户信息
    app.getUserOpenId(function(err,openid){
          if (!err) {
            app.getUserInfoByOpenId(openid).then(function (res) {
                  console.log(res);
                  if (res.code == 0){
                       if (!res.userInfo) {
                          wx.redirectTo({
                            url: '/page/mine/authorize/authorize'
                          })
                        }
                  }else{
                    wx.redirectTo({
                        url: '/page/mine/authorize/authorize'
                      })
                  }
             });
          }else{
            console.log('获取openid失败')
            util.showWarn('app.getUserOpenId 失败！');
          }
    })
    //加载新闻资讯
    var data = {pageNum:1,
                pageSize:3};
    wx.request({
        url: API_getNewslistByPage,
        data: data,
        method: 'GET',
        header: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        success: function (res) {
            if (res.data.code == 0) {
                console.log('查询新闻资讯成功！')
                var newsList = res.data.pageInfo.list;
                //日期字符串截取
                newsList.forEach((item) => {
                  item.createTime = item.createTime.substring(0, 10);
                  var API_referQr = "https://golden-adviser.com:8443/"
                  item.iconUrl = API_referQr + item.iconUrl;  
                })
                self.setData({
                    newsList: newsList
                })
            }
        }
    });
  },
  onShow() {  
    wx.reportAnalytics('enter_home_programmatically', {})
  },
  onShareAppMessage() {
    return {
      title: '金参谋数据分析平台',
      path: 'page/home/index'
    }
  },
  data: {
      array: [{
        mode: 'scaleToFill',
        text: '保持纵横比缩放图片，使图片的长边能完全显示出来',
        src: '../../images/banner.png'
      }],
      newsList: [],
  },
  secuApply: function () {
    console.log('insurance');
    //已经授权登录 但没有注册
    if(!app.globalData.userInfo.telNum) {
      wx.navigateTo({
        url: '/page/mine/register/register?registerType=2'
      })
      //已经授权和注册
    }else{
      wx.navigateTo({
        url: './pages/info-insurance/info-insurance'
      })
    }
  },
  govApply: function () {
    console.log('policy');
    //已经授权登录 但没有注册
    if(!app.globalData.userInfo.telNum) {
      wx.navigateTo({
        url: '/page/mine/register/register?registerType=1'
      })
      //已经授权和注册
    }else{
      wx.navigateTo({
        url: './pages/info-policy/info-policy'
      })
    }
  },
  loanApply:function(){
    console.log('loanApply');
    //已经授权登录 但没有注册
    if(!app.globalData.userInfo.telNum) {
        wx.navigateTo({
          url: '/page/mine/register/register?registerType=0' 
        })
    //已经授权和注册 
    }else{
         wx.navigateTo({
           url: './pages/info-entry/info-entry' 
         })
    }
  },
  demandArea:function(){
    console.log('demandArea');
    //已经授权登录 但没有注册
    if(!app.globalData.userInfo.telNum) {
        wx.navigateTo({
          url: '/page/mine/register/register?registerType=3' 
        })
    //已经授权和注册 
    }else{
         wx.navigateTo({
           url: './pages/demand-area/demand-area' 
         })
    }
  },
  loadMoreNews:function(){
    console.log('loadMoreNews');
    wx.navigateTo({
      url: './pages/news-list/news-list' 
    })
  },
  // 资讯
  jumpNewsDetail: function (e) {
    console.log(e.currentTarget.dataset.newsId)
    var newsId = e.currentTarget.dataset.newsId;
    wx.navigateTo({
      url: './pages/news-detail/news-detail?newsId=' + newsId,
      success: function (res) {
        // success
      },
      fail: function () {
        // fail
      },
      complete: function () {
        // complete
      }
    })
  },
})
