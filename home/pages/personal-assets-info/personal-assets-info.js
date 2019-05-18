var dic = require("../../../../utils/dic.js"),
    util = require("../../../../utils/util.js"),
    API_persAssetsInfo_save = require("../../../../utils/config.js").API_persAssetsInfo_save;
var app = getApp();

Page({
    onShareAppMessage() {
        return {
            title: '资产信息',
            path: 'page/home/pages/personal-assets-info/personal-assets-info'
        }
    },
    data: {
        itemMap: {
            job:'工作',
            haveCar:'是否有车',
            credit:'征信良好',
            haveHouse:'是否有房'
        },
        jobKey: "",
        haveCarKey: "",
        creditKey: "",
        haveHouseKey:"",
        companyTimeKey:"",
        houseTypeKeys: ["", "10", "20", "30", "40", "50"],
        houseTypeValues: ["请选择", "商品房", "洋房", "别墅", "商业", "其他"],
        mortgageKeys: ["", "10", "20"],
        mortgageValues: ["请选择", "是", "否"],
        areaKeys: ["", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100", "110", "120", "130", "140", "150", "160", "170", "180", "190", "200", "210", "220", "230", "240", "250", "260", "270", "280", "290", "300", "310", "320", "330", "340", "350", "360", "370", "380", "390"],
        areaValues: ["请选择", "万州区", "涪陵区", "渝中区", "大渡口区", "江北区", "沙坪坝区", "九龙坡区", "南岸区", "北碚区", "綦江区", "大足区", "渝北区", "巴南区", "黔江区", "长寿区", "江津区", "合川区", "永川区", "南川区", "璧山区", "铜梁区", "潼南县", "荣昌县", "梁平县", "城口县", "丰都县", "垫江县", "武隆县", "忠县", "开县", "云阳县", "奉节县", "巫山县", "巫溪县", "石柱土家族自治县", "秀山土家族苗族自治县", "酉阳土家族苗族自治县", "彭水苗族土家族自治县", "其他"],
        houses: [{
            houseNum: 0,
            houseTypeIndex: 0,
            houseTypeKey: "",
            mortgageIndex: 0,
            mortgageKey: "",
            areaIndex: 0,
            areaKey: "",
            price: "",     //房产价格
            houseMortPay:"", //房贷月供
            houseMortMonth:"", //连续上交月份
            loanAmount:""//已还房贷
        }],

    },
    onLoad: function (e) {
        if (util.getValueArray({
            haveCar: dic.haveCar,
            credit: dic.credit,
            job: dic.job,
            area: dic.area,
            haveHouse:dic.haveHouse,
            companyTime:dic.companyTime
        })) ;
    },
    changeIndex: util.changeIndex,
    doNext: function (e) {
        const self = this

        //检验必填项
        if (util.checkRequired(e, self.data.itemMap)) {
            var data = e.detail.value;
            data.jobKey = self.data.jobKey;
            data.haveCarKey = self.data.haveCarKey;
            data.creditKey = self.data.creditKey;
            data.haveHouseKey = self.data.haveHouseKey;
            data.companyTimeKey = self.data.companyTimeKey;

            var housearr = self.data.houses;
            var houses = housearr.slice(0);//赋值数据的值 而非引用
            for (var i = 0; i < houses.length; i++) {
                var price = houses[i].price || '';
                if ("" == price || "" == price.trim() || "0" == price) {
                    houses.splice(i, 1)
                    continue
                }
                //房产已经抵押才会判断以下录入项是否为空
                var mortgageKey =houses[i].mortgageKey;
                if(mortgageKey === "10"){
                    var houseMortPay = houses[i].houseMortPay || '';
                    if ("" == houseMortPay || "" == houseMortPay.trim() || "0" == houseMortPay) {
                        houses.splice(i, 1)
                        continue
                    }
                    var houseMortMonth = houses[i].houseMortMonth || '';
                    if ("" == houseMortMonth || "" == houseMortMonth.trim() || "0" == houseMortMonth) {
                        houses.splice(i, 1)
                        continue
                    }
                    var loanAmount = houses[i].loanAmount || '';
                    if ("" == loanAmount || "" == loanAmount.trim() || "0" == loanAmount) {
                        houses.splice(i, 1)
                        continue
                    }
                }
                
            }
            data.houses = houses;
         //有房
         if (data.haveHouseKey == 10) {
            if(data.houses.length == 0){
                 return util.showWarn('请完善房产信息！至少填写一套完整的房产信息。');
             }
         }
        }
         
        //检验必填项
        if (util.checkRequired(e, self.data.itemMap)) {
            app.getUserOpenId(function (err, openid) {
                if (!err) {
                    wx.showLoading({
                        title: '参谋长思考中',
                        mask: true
                    })
                    console.log(openid)
                    
                    data.openId = openid;
                    wx.request({
                        url: API_persAssetsInfo_save,
                        data: data,
                        method: 'POST',
                        header: {
                            "Content-Type": "application/json;charset=UTF-8"
                        },
                        success: function (res) {
                            wx.hideLoading()
                            if (res.data.code == 0) {
                                console.log('保存个人信息-资产信息成功！')
                                wx.navigateTo({
                                    url: '../personal-loan-report/personal-loan-report'
                                })

                            } else {
                                console.log('保存个人信息-资产信息失败！')
                                util.showWarn('保存失败！原因：' + res.data.msg);
                            }
                        }
                    });
                } else {
                    console.log('获取openid失败')
                }
            })
        }

    },
    goBack: function () {
        wx.navigateBack({
            delta: 1
        });
    },
    addHouse: function () {
        var houses = this.data.houses;
        var length = houses.length;
        var obj = {};
        obj.houseNum = length;
        obj.houseTypeIndex = 0;
        obj.mortgageIndex = 0;
        obj.areaIndex = 0;
        obj.price = null;
        houses.push(obj)
        this.setData({
            houses: houses
        })
    },
    changeHouseTypeIndex: function (e) {
        var houseNum = e.currentTarget.dataset.houseNum;
        var houses = this.data.houses;
        houses[houseNum].houseTypeIndex = e.detail.value;
        houses[houseNum].houseTypeKey = this.data.houseTypeKeys[e.detail.value];
        this.setData({
            houses: houses
        })
    },
    changeMortgageIndex: function (e) {
        var houseNum = e.currentTarget.dataset.houseNum;
        var houses = this.data.houses;
        houses[houseNum].mortgageIndex = e.detail.value;
        houses[houseNum].mortgageKey = this.data.mortgageKeys[e.detail.value];
        this.setData({
            houses: houses
        })
    },
    changeAreaIndex: function (e) {
        var houseNum = e.currentTarget.dataset.houseNum;
        var houses = this.data.houses;
        houses[houseNum].areaIndex = e.detail.value;
        houses[houseNum].areaKey = this.data.areaKeys[e.detail.value];
        this.setData({
            houses: houses
        })
    },
    changePriceIndex: function (e) {
        var houseNum = e.currentTarget.dataset.houseNum;
        var houses = this.data.houses;
        houses[houseNum].price = e.detail.value;
        this.setData({
            houses: houses
        })
    },
    changeHouseMortPayIndex: function (e) {
        var houseNum = e.currentTarget.dataset.houseNum;
        var houses = this.data.houses;
        houses[houseNum].houseMortPay = e.detail.value;
        this.setData({
            houses: houses
        })
    },
    changeHouseMortMonthIndex: function (e) {
        var houseNum = e.currentTarget.dataset.houseNum;
        var houses = this.data.houses;
        houses[houseNum].houseMortMonth = e.detail.value;
        this.setData({
            houses: houses
        })
    },
    changeLoanAmountIndex: function (e) {
        var houseNum = e.currentTarget.dataset.houseNum;
        var houses = this.data.houses;
        houses[houseNum].loanAmount = e.detail.value;
        this.setData({
            houses: houses
        })
    },
});