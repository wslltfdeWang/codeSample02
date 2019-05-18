let dic = require("../../../../utils/dic.js"),
    util = require("../../../../utils/util.js"),
    API_persFinanceDetail_save = require("../../../../utils/config.js").API_persFinanceDetail_save;
let app = getApp();

Page({
    onShareAppMessage() {
        return {
            title: '财务信息',
            path: 'page/home/pages/personal-finance-info/personal-finance-info'
        }
    },
    data: {
        itemMap: {},
        socialSecurityFlag: false,
        insuranceFlag: false,
        houseFundFlag: false,
        wageFlag: false,
        carsFlag: false,
        socialSecurity: [{
            pay: '',
            num: ''
        }],
        insurance: [{
            pay: '',
            num: ''
        }],
        houseFund: [{
            pay: '',
            num: ''
        }],
        wage: [{
            pay: '',
            num: ''
        }],
        cars: [{
            pay: '',
            num: ''
        }],

    },
    onLoad: function (e) {

    },
    inputChange: util.inputChange,
    changeIndex: util.changeIndex,
    showWarn: util.showWarn,

    addChange: function (e) {
        let type = e.currentTarget.dataset.type;
        let objs = this.data[type];
        objs[e.currentTarget.dataset.idx][e.currentTarget.dataset.detail] = e.detail.value;
        if (e.currentTarget.dataset.idx == 0 && this.data[type].length == 1 && e.currentTarget.dataset.detail == 'pay') {
            if (e.detail.value != null && e.detail.value != '') {
                this.setData({
                    [type]: objs,
                    [type + 'Flag']: true
                })
            } else {
                this.setData({
                    [type]: objs,
                    [type + 'Flag']: false
                })
            }
        }
    },
    doNext: function (e) {
        let loanDetails = [];
        let data2 = this.data
        //检验必填项
        for (let v in data2.socialSecurity) {
            if (data2.socialSecurity[v].pay.trim() == '' || data2.socialSecurity[v].pay.trim() == '0') {
                continue
            } else {
                if (!/^(0|[1-9][0-9]*)$/.test(data2.socialSecurity[v].pay))
                    return this.showWarn("请输入正确的社保金额");
                if (data2.socialSecurity[v].num.trim() == '' || data2.socialSecurity[v].num.trim() == '0') {
                    return this.showWarn("社保上交月份不能为空");
                } else {
                    if (!/^(0|[1-9][0-9]*)$/.test(data2.socialSecurity[v].num))
                        return this.showWarn("请输入正确的社保上交月份");
                    let obj = {};
                    obj.type = 'socialSecurity'
                    obj.pay = data2.socialSecurity[v].pay
                    obj.num = data2.socialSecurity[v].num
                    loanDetails.push(obj)
                }
            }
        }
        for (let v in data2.insurance) {
            if (data2.insurance[v].pay.trim() == '' || data2.insurance[v].pay.trim() == '0') {
                continue
            } else {
                if (!/^(0|[1-9][0-9]*)$/.test(data2.insurance[v].pay))
                    return this.showWarn("请输入正确的商保金额");
                if (data2.insurance[v].num.trim() == '' || data2.insurance[v].num.trim() == '0') {
                    return this.showWarn("商业保单连续缴费次数");
                } else {
                    if (!/^(0|[1-9][0-9]*)$/.test(data2.insurance[v].num))
                        return this.showWarn("请输入正确的商保缴费次数");
                    let obj = {};
                    obj.type = 'insurance'
                    obj.pay = data2.insurance[v].pay
                    obj.num = data2.insurance[v].num
                    loanDetails.push(obj)
                }
            }
        }
        for (let v in data2.houseFund) {
            if (data2.houseFund[v].pay.trim() == '' || data2.houseFund[v].pay.trim() == '0') {
                continue
            } else {
                if (!/^(0|[1-9][0-9]*)$/.test(data2.houseFund[v].pay))
                    return this.showWarn("请输入正确的公积金金额");
                if (data2.houseFund[v].num.trim() == '' || data2.houseFund[v].num.trim() == '0') {
                    return this.showWarn("公积金连续上交月份不能为空");
                } else {
                    if (!/^(0|[1-9][0-9]*)$/.test(data2.houseFund[v].num))
                        return this.showWarn("请输入正确的公积金连续上交月份");
                    let obj = {};
                    obj.type = 'houseFund'
                    obj.pay = data2.houseFund[v].pay
                    obj.num = data2.houseFund[v].num
                    loanDetails.push(obj)
                }
            }
        }
        for (let v in data2.wage) {
            if (data2.wage[v].pay.trim() == '' || data2.wage[v].pay.trim() == '0') {
                continue
            } else {
                if (!/^(0|[1-9][0-9]*)$/.test(data2.wage[v].pay))
                    return this.showWarn("请输入正确的工资金额");
                if (data2.wage[v].num.trim() == '' || data2.wage[v].num.trim() == '0') {
                    return this.showWarn("工资连续上交月份不能为空");
                } else {
                    if (!/^(0|[1-9][0-9]*)$/.test(data2.wage[v].num))
                        return this.showWarn("请输入正确的工资连续上交月份");
                    let obj = {};
                    obj.type = 'wage'
                    obj.pay = data2.wage[v].pay
                    obj.num = data2.wage[v].num
                    loanDetails.push(obj)
                }
            }
        }
        for (let v in data2.cars) {
            if (data2.cars[v].pay.trim() == '' || data2.cars[v].pay.trim() == '0') {
                continue
            } else {
                if (!/^(0|[1-9][0-9]*)$/.test(data2.cars[v].pay))
                    return this.showWarn("请输入正确的车贷金额");
                if (data2.cars[v].num.trim() == '' || data2.cars[v].num.trim() == '0') {
                    return this.showWarn("车贷连续上交月份不能为空");
                } else {
                    if (!/^(0|[1-9][0-9]*)$/.test(data2.cars[v].num))
                        return this.showWarn("请输入正确的车贷连续上交月份");
                    let obj = {};
                    obj.type = 'cars'
                    obj.pay = data2.cars[v].pay
                    obj.num = data2.cars[v].num
                    loanDetails.push(obj)
                }
            }
        }
        app.getUserOpenId(function (err, openid) {
            if (!err) {
                wx.showLoading({
                    title: '参谋长记录中',
                    mask: true
                })
                console.log(openid)
                let data = {}
                data.loans = loanDetails;
                data.openId = openid;
                wx.request({
                    url: API_persFinanceDetail_save,
                    data: data,
                    method: 'POST',
                    header: {
                        "Content-Type": "application/json;charset=UTF-8"
                    },
                    success: function (res) {
                        wx.hideLoading()
                        if (res.data.code == 0) {
                            console.log('保存个人信息-财务信息成功！')
                            wx.navigateTo({
                                url: '../personal-assets-info/personal-assets-info'
                            })

                        } else {
                            console.log('保存个人信息-财务信息失败！')
                            util.showWarn('保存失败！原因：' + res.data.msg);
                        }
                    }
                });
            } else {
                console.log('获取openid失败')
            }
        })
    },
    goBack: function () {
        wx.navigateBack({
            delta: 1
        });
    },
    addItem: function (e) {
        let type = e.currentTarget.dataset.type;
        let objs = this.data[type];
        let len = objs.length
        if (len > 2) {
            wx.showModal({
                title: "提示",
                showCancel: !1,
                content: "最多录入3条同类信息！"
            })
            return;
        }
        let obj = {};
        obj.pay = '';
        obj.num = '';
        objs.push(obj)
        this.setData({
            [type + 'Flag']: true,
            [type]: objs
        })
    },
    delItem: function (e) {
        let type = e.currentTarget.dataset.type;
        let objs = this.data[type];
        objs.splice(e.currentTarget.dataset.idx, 1);
        let flag = true;
        if (this.data[type].length == 1 && this.data[type][0].pay == 0) {
            flag = false;
        }
        this.setData({
            [type + 'Flag']: flag,
            [type]: objs
        })
    }
});