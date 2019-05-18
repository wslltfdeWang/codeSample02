var dic = require("../../../../utils/dic.js"),
    util = require("../../../../utils/util.js"),
    API_getLoanList = require("../../../../utils/config.js").API_getLoanList,
    API_getRecommendLoanList = require("../../../../utils/config.js").API_getRecommendLoanList,
    API_referQr = require("../../../../utils/config.js").API_referQr,
    API_reportRecord_save = require("../../../../utils/config.js").API_reportRecord_save;
var app = getApp();

Page({
    onShareAppMessage() {
        return {
            title: '精选方案',
            path: 'page/home/pages/personal-loan-report/personal-loan-report'
        }
    },
    data: {
        grade: {},
        loanList: [],
        recommendLoanList:[],
        numList:['一','二','三','四','五','六','七','八','九','十']
    },
    onLoad: function (e) {
        const self = this
        //精选方案
        app.getUserOpenId(function (err, openid) {
            if (!err) {
                console.log(openid)
                var data = {};
                data.openId = openid;
                wx.request({
                    url: API_getLoanList,
                    data: data,
                    method: 'GET',
                    header: {
                        "Content-Type": "application/json;charset=UTF-8"
                    },
                    success: function (res) {
                        if (res.data.code == 0) {
                            console.log('查询报告个人信息成功！')
                            var loanResponse = res.data.loanResponse;

                            var loanList = loanResponse.loanList;
                                //换行显示
                                loanList.forEach((item) => {
                                   var arr = item.repayMethod.split("###");
                                   item.repayMethod = arr.join("\n");//多行数，换行显示
                                   // if (arr.length == 1) {
                                   //    item.repayMethod = "\n" + arr.join("\n");//只有一行，让内容居中
                                   // }else{
                                   //    item.repayMethod = arr.join("\n");//多行数，换行显示
                                   // }
                                })
                            
                            self.setData({
                                grade: loanResponse.grade,
                                loanList: loanList
                            })

                        } else {
                            console.log('保查询报告个人信息失败！')
                            wx.showModal({
                                title: '抱歉',
                                content: '您录入的信息不足，无法为您生成方案，请重新完善您的信息',
                                showCancel: false,
                                confirmText: '返回',
                                success: function (res) {
                                    if (res.confirm) {
                                        wx.navigateTo({
                                            url: '/page/home/pages/personal-finance-info/personal-finance-info'
                                        })
                                    }
                                }
                            })
                        }
                    }
                });
            } else {
                console.log('获取openid失败')
            }
        })
        //爆款推荐
        app.getUserOpenId(function (err, openid) {
            if (!err) {
                console.log(openid)
                var data = {};
                data.openId = openid;
                wx.request({
                    url: API_getRecommendLoanList,
                    data: data,
                    method: 'GET',
                    header: {
                        "Content-Type": "application/json;charset=UTF-8"
                    },
                    success: function (res) {
                        if (res.data.code == 0) {
                            console.log('查询个人爆款推荐信息成功！')
                            var loanResponse = res.data.loanResponse;

                            var recommendLoanList = loanResponse.loanList;
                                recommendLoanList.forEach((item) => {
                                  item.loanUrl = API_referQr + item.loanUrl;
                                })

                            self.setData({
                                recommendLoanList: recommendLoanList
                            })
                        }
                    }
                });
            } else {
                console.log('获取openid失败')
            }
        })
    },
    onReady: function () {

    },
    checkboxChange: function (e) {
        console.log('checkbox发生chang事件', e.detail.value)
    },
    changeIndex: util.changeIndex,
    doNext: function (e) {
        const self = this

        wx.navigateTo({
            url: '../loan-result/loan-result'
        })

    },
    deal: function (e) {
        //报告结果ID
        var resultId = e.currentTarget.dataset.resultId;
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
                data.customerType='1'; //用户类型（0：不区分，1：个人用户，2：企业用户）
                data.dealType='0'; //0:立即办理，1：人工方案
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
                                url: '../loan-result/loan-result?dealType=0'
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
        
    },
    man: function (e) {
        //报告结果ID
        var resultId = e.currentTarget.dataset.resultId;
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
                data.customerType='1'; //用户类型（0：不区分，1：个人用户，2：企业用户）
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
                                url: '../loan-result/loan-result?dealType=1'
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
    },
    appoint: function (e) {
        //报告结果ID
        var resultId = e.currentTarget.dataset.resultId;
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
                data.customerType='1'; //用户类型（0：不区分，1：个人用户，2：企业用户）
                data.dealType='2'; //0:立即办理，1：人工方案,2:预约咨询
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
           url: '../demand-area/demand-area' 
         })
    }
  }

});