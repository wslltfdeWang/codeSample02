var API_getNewsById = require("../../../../utils/config.js").API_getNewsById;
var app = getApp();

Page({
  onLoad(options) {
    const self = this
    var newsId = options.newsId; 

     wx.request({
        url: API_getNewsById,
        data: {newsId:newsId},
        method: 'GET',
        header: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        success: function (res) {
            if (res.data.code == 0) {
                console.log('根据新闻ID查询新闻资讯成功！')

                self.setData({
                    url: res.data.news.url,
                    newsId:newsId
                })
            }
        }
    });
  },
  onShow() {  
    wx.reportAnalytics('enter_home_programmatically', {})
  },
  onShareAppMessage() {
    var newsId = this.data.newsId;
    return {
      title: '新闻详情',
      path: 'page/home/pages/news-detail/news-detail?newsId='+newsId
    }
  },
  data: {
      url:"",
      newsId:""
  },
})
