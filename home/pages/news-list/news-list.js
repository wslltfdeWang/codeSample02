var API_getNewslistByPage = require("../../../../utils/config.js").API_getNewslistByPage,
    API_referQr = require("../../../../utils/config.js").API_referQr;
var app = getApp();

Page({
  onLoad() {
    // 页面初次加载，请求第一页数据
    this.getNewslist(1,true)
  },
  onShow() {  
    wx.reportAnalytics('enter_home_programmatically', {})
  },
  onShareAppMessage() {
    return {
      title: '新闻列表',
      path: 'page/home/pages/news-list/news-list'
    }
  },
  data: {
      pageNum:1, //当前第几页，默认从第1页查询
      pages:0, //总页数
      newsList: [],
  },
  onReachBottom() {
    // 下拉触底，先判断是否有请求正在进行中
    // 以及检查当前请求页数是不是小于数据总页数，如符合条件，则发送请求
    console.log("onReachBottom.... ")
    if (!this.loading && this.data.pageNum < this.data.pages) {
      this.getNewslist(this.data.pageNum + 1)
    }
  },

  onPullDownRefresh() {
    // 上拉刷新
    console.log(" 上拉刷新onPullDownRefresh.... ")
    if (!this.loading) {
      this.getNewslist(1,true).then(() => {
        // 处理完成后，终止下拉刷新
        wx.stopPullDownRefresh()
      })
    }
  },

  // 资讯
  jumpNewsDetail: function (e) {
    console.log(e.currentTarget.dataset.newsId)
    var newsId = e.currentTarget.dataset.newsId;
    wx.navigateTo({
      url: '../news-detail/news-detail?newsId=' + newsId,
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
  getNewslist(pageNum,override) {
    const self  = this
    this.loading = true
    var pageSize = 10;
    // 向后端请求指定页码的数据
    return this.getNewslistByPage(pageNum,pageSize).then(res => {
      if(res.data.code == 0){
          //日期字符串截取
          var newsList = res.data.pageInfo.list;
          newsList.forEach((item) => {
            item.createTime = item.createTime.substring(0, 10); 
            var API_referQr = "https://golden-adviser.com:8443/"
            item.iconUrl = API_referQr + item.iconUrl; 
          })
          self.setData({
            pageNum: pageNum,   //当前的页号
            pages: res.data.pageInfo.pages,  //总页数
            newsList:  override ? newsList : this.data.newsList.concat(newsList)
          })
      }
    }).catch(err => {
      console.log("==> [ERROR]", err)
    }).then(() => {
      self.loading = false
    })
  },
  getNewslistByPage: function (pageNum,pageSize) {
    return new Promise(function (resolve, reject) {
      wx.request({
          url: API_getNewslistByPage,
          data: {
            pageNum:pageNum,
            pageSize:pageSize
          },
          success(res) {
            resolve(res)
          },
          fail(res) {
            var res = {
                 code: 1,
                 msg:'出错了'
              }
            reject(res);
          }
      })
    })
  }
})
