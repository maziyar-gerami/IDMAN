document.addEventListener('DOMContentLoaded', function () {
  var router = new VueRouter({
    mode: 'history',
    routes: []
  });
  Vue.component('v-pagination', window['vue-plain-pagination'])
  new Vue({
    router,
    el: '#app',
    data: {
      dropdownMenu: false,
      dateNav: "",
      dateNavEn: "",
      dateNavText: "",
      recordsShownOnPage: 20,
      recordsShownOnPageAudits: 20,
      currentPageAudits: 1,
      totalAudits: 1,
      currentPageAudit: 1,
      totalAudit: 1,
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      activeItem: "myAudits",
      audit: [],
      audits: [],
      auditPage: [],
      auditsPage: [],
      changePageAudit: false,
      changePageAudits: false,
      userPicture: "images/PlaceholderUser.png",
      auditDate: "",
      auditsDate: "",
      auditsUserId: "",
      auditMore: "",
      bootstrapPaginationClasses: {
        ul: 'pagination',
        li: 'page-item',
        liActive: 'active',
        liDisable: 'disabled',
        button: 'page-link'  
      },
      paginationAnchorTexts: {
          first: '<<',
          prev: '<',
          next: '>',
          last: '>>'
      },
      loader: false,
      deleteInputIcon: "left: 7%;",
      deleteInputIcon1: "left: 10%;",
      isListEmpty: false,
      isListEmpty1: false,
      s0: "پارسو",
      s1: "",
      s2: "خروج",
      s3: "داشبورد",
      s4: "سرویس ها",
      s5: "گروه ها",
      s6: "رویداد ها",
      s7: "پروفایل",
      s8: "دسترسی سریع",
      s9: "خلاصه وضعیت",
      s10: "قوانین",
      s11: "حریم خصوصی",
      s12: "راهنما",
      s13: "کاربران",
      s14: "./dashboard",
      s15: "./services",
      s16: "./users",
      s17: "شناسه",
      s18: "نام",
      s19: "توضیحات",
      s20: "اتصال",
      s21: "./groups",
      s22: "./profile",
      s23: "./privacy",
      s24: "پیکربندی",
      s25: "./configs",
      s26: "تاریخ",
      s27: "جستجو",
      s28: "عملیات",
      s29: "برنامه",
      s30: "زمان",
      s31: "ممیزی های من",
      s32: "ممیزی های کاربران",
      s33: "شناسه",
      s34: " مثال: admin ",
      s35: " مثال: 1399/05/01 ",
      s36: "./events",
      s37: "شناسه",
      s38: "تاریخ",
      s39: "ورود موفق",
      s40: "ورود ناموفق",
      s41: "سرویس",
      s42: "سیستم عامل",
      s43: "مرورگر",
      s44: "تعداد رکورد ها: ",
      s45: "ممیزی ها",
      s46: "/audits",
      s47: "بازگشت",
      s48: "رکوردی یافت نشد",
      rolesText: "نقش ها",
      rolesURLText: "./roles",
      reportsText: "گزارش ها",
      reportsURLText: "./reports",
    },
    created: function () {
      this.setDateNav();
      this.getUserInfo();
      this.getUserPic();
      this.getAudit();
      this.getAudits();
      if(typeof this.$route.query.en !== 'undefined'){
        this.changeLang()
      }
    },
    methods: {
      setDateNav: function () {
        this.dateNav = new persianDate().format("dddd, DD MMMM YYYY");
        persianDate.toCalendar("gregorian");
        persianDate.toLocale("en");
        this.dateNavEn = new persianDate().format("dddd, DD MMMM YYYY");
        persianDate.toCalendar("persian");
        persianDate.toLocale("fa");
        this.dateNavText = this.dateNav;
      },
      dropdownNavbar: function () {
        if(this.dropdownMenu){
          let dropdowns = document.getElementsByClassName("dropdown-content");
          for (let i = 0; i < dropdowns.length; ++i) {
            let openDropdown = dropdowns[i];
            if(openDropdown.classList.contains("show")) {
              openDropdown.classList.remove("show");
            }
          }
          this.dropdownMenu = false;
        }else{
          document.getElementById("dropdownMenu").classList.toggle("show");
          this.dropdownMenu = true;
        }
      },
      isActive (menuItem) {
        return this.activeItem === menuItem
      },
      setActive (menuItem) {
        this.activeItem = menuItem
      },
      showMore: function(info) {
        this.auditMore = info;
        document.getElementById("auditList").style = "display: none;";
        document.getElementById("auditMore").style = "";
      },
      showList: function() {
        document.getElementById("auditList").style = "";
        document.getElementById("auditMore").style = "display: none;";
        this.auditMore = "";
      },
      changeRecordsAudit: function(event) {
        this.recordsShownOnPage = event.target.value;
        this.getAuditDate();
      },
      changeRecordsAudits: function(event) {
        this.recordsShownOnPageAudits = event.target.value;
        this.getAuditsDate();
      },
      getUserInfo: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user") //
          .then((res) => {
            vm.username = res.data.userId;
            vm.name = res.data.displayName;
            vm.nameEN = res.data.firstName + " " + res.data.lastName;
            vm.s1 = vm.name;
          });
      },
      getUserPic: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user/photo") //
            .then((res) => {
              if(res.data == "Problem" || res.data == "NotExist"){
                vm.userPicture = "images/PlaceholderUser.png";
              }else{
                vm.userPicture = "/api/user/photo";
              }
            })
            .catch((error) => {
                if (error.response) {
                  if (error.response.status == 400 || error.response.status == 500 || error.response.status == 403) {
                    vm.userPicture = "images/PlaceholderUser.png";
                  }else{
                    vm.userPicture = "/api/user/photo";
                  }
                }
            });
      },
      exportAudits: function () {
        url_ = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        vm.loader = true;
          axios({
            url: url_ + "/api/audits/users/export",
            method: "GET",
            responseType: "blob",
          }).then((response) => {
            vm.loader = false;
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute("download", "audits.xls");
            document.body.appendChild(link);
            link.click();
          }).catch((error) => {
            vm.loader = false;
          });
      },
      faMonthtoNumMonth: function (faMonth) {
        let numMonth = "";
        switch(faMonth) {
          case "فروردین":
            numMonth = "01";
            break;
          case "اردیبهشت":
            numMonth = "02";
            break;
          case "خرداد":
            numMonth = "03";
            break;
          case "تیر":
            numMonth = "04";
            break;
          case "مرداد":
            numMonth = "05";
            break;
          case "شهریور":
            numMonth = "06";
            break;
          case "مهر":
            numMonth = "07";
            break;
          case "آبان":
            numMonth = "08";
            break;
          case "آذر":
            numMonth = "09";
            break;
          case "دی":
            numMonth = "10";
            break;
          case "بهمن":
            numMonth = "11";
            break;
          case "اسفند":
            numMonth = "12";
            break;
          default:
            console.log("Wrong Input for Month");
        }
        return numMonth;
      },
      faNumToEnNum: function (str) {
        let s = str.split("");
        let sEn = "";
        for(i = 0; i < s.length; ++i){
          if(s[i] == '۰'){
            sEn = sEn + '0';
          }else if(s[i] == '۱'){
            sEn = sEn + '1';
          }else if(s[i] == '۲'){
            sEn = sEn + '2';
          }else if(s[i] == '۳'){
            sEn = sEn + '3';
          }else if(s[i] == '۴'){
            sEn = sEn + '4';
          }else if(s[i] == '۵'){
            sEn = sEn + '5';
          }else if(s[i] == '۶'){
            sEn = sEn + '6';
          }else if(s[i] == '۷'){
            sEn = sEn + '7';
          }else if(s[i] == '۸'){
            sEn = sEn + '8';
          }else if(s[i] == '۹'){
            sEn = sEn + '9';
          }
        }
        return sEn;
      },
      getAudits: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageAudits){
          this.currentPageAudits = 1;
        }
        
        let tempEvent = {};
        this.audits = [];
        axios.get(url + "/api/audits/users/" + vm.currentPageAudits + "/" + vm.recordsShownOnPageAudits)
        .then((res) => {
          if(res.data.auditList.length == 0){
            vm.isListEmpty1 = true;
          }
          vm.totalAudits = Math.ceil(res.data.size / vm.recordsShownOnPageAudits);
          res.data.auditList.forEach(function (item) {
            tempEvent = {};

            let f = item.actionPerformed.indexOf("_", item.actionPerformed.indexOf("_") + 1);
            let l = item.actionPerformed.length;
            tempEvent.actionPerformed = item.actionPerformed.substr(0, f+1) + "\n" + item.actionPerformed.substr(f+1, l-(f+1));
            tempEvent.principal = item.principal.replace("audit:", "");
            tempEvent.applicationCode = item.applicationCode;
            tempEvent.clientIpAddress = item.clientIpAddress;
            tempEvent.serverIpAddress = item.serverIpAddress;
            tempEvent.resourceOperatedUpon = item.resourceOperatedUpon;

            let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
            tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
            tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;
            
            vm.audits.push(tempEvent);
          });
        });
        this.changePageAudits = false;
      },
      getAudit: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty = false;
        if(!this.changePageAudit){
          this.currentPageAudit = 1;
        }
        
        let tempEvent = {};
        this.audit = [];
        axios.get(url + "/api/audits/user/" + vm.currentPageAudit + "/" + vm.recordsShownOnPage) //
        .then((res) => {
          if(res.data.auditList.length == 0){
            vm.isListEmpty = true;
          }
          vm.totalAudit = Math.ceil(res.data.size / vm.recordsShownOnPage);
          res.data.auditList.forEach(function (item) {
            tempEvent = {};

            tempEvent.actionPerformed = item.actionPerformed;
            tempEvent.principal = item.principal.replace("audit:", "");
            tempEvent.applicationCode = item.applicationCode;
            tempEvent.clientIpAddress = item.clientIpAddress;
            tempEvent.resourceOperatedUpon = item.resourceOperatedUpon;

            let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
            tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
            tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;
            
            vm.audit.push(tempEvent);
          });
        });
        this.changePageAudit = false;
      },
      getAuditDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty = false;
        if(!this.changePageAudit){
          this.currentPageAudit = 1;
        }

        this.auditDate = document.getElementById("auditDate").value;
        if(this.auditDate == ""){
          this.getAudit();
        }else{
          let tempArray = this.auditDate.split(" ");
          this.auditDate = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
          let tempEvent = {};
          this.audit = [];
          axios.get(url + "/api/audits/user/date/" + vm.auditDate + "/" + vm.currentPageAudit + "/" + vm.recordsShownOnPage) //
          .then((res) => {
            if(res.data.auditList.length == 0){
              vm.isListEmpty = true;
            }
            vm.totalAudit = Math.ceil(res.data.size / vm.recordsShownOnPage);
            res.data.auditList.forEach(function (item) {
              tempEvent = {};

              tempEvent.actionPerformed = item.actionPerformed;
              tempEvent.principal = item.principal.replace("audit:", "");
              tempEvent.applicationCode = item.applicationCode;
              tempEvent.clientIpAddress = item.clientIpAddress;
              tempEvent.resourceOperatedUpon = item.resourceOperatedUpon;

              let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
              tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
              tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;
                
              vm.audit.push(tempEvent);
            });
          });
        }
        this.changePageAudit = false;
      },
      getAuditsUserId: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageAudits){
          this.currentPageAudits = 1;
        }

        this.auditsDate = document.getElementById("auditsDate").value;
        if(this.auditsDate == "" && this.auditsUserId == ""){
          this.getAudits();
        }else if(this.auditsDate == "" && this.auditsUserId != ""){
          let tempEvent = {};
          this.audits = [];
          axios.get(url + "/api/audits/users/" + vm.auditsUserId + "/" + + vm.currentPageAudits + "/" + vm.recordsShownOnPageAudits) //
          .then((res) => {
            if(res.data.auditList.length == 0){
              vm.isListEmpty1 = true;
            }
            vm.totalAudits = Math.ceil(res.data.size / vm.recordsShownOnPageAudits);
            res.data.auditList.forEach(function (item) {
              tempEvent = {};

              tempEvent.actionPerformed = item.actionPerformed;
              tempEvent.principal = item.principal.replace("audit:", "");
              tempEvent.applicationCode = item.applicationCode;
              tempEvent.clientIpAddress = item.clientIpAddress;
              tempEvent.serverIpAddress = item.serverIpAddress;
              tempEvent.resourceOperatedUpon = item.resourceOperatedUpon;

              let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
              tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
              tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;
              
              vm.audits.push(tempEvent);
            });
          });
        }else if(this.auditsDate != "" && this.auditsUserId == ""){
          this.getAuditsDate();
        }else if(this.auditsDate != "" && this.auditsUserId != ""){
          this.getAuditsUserIdDate();
        }
        this.changePageAudits = false;
      },
      getAuditsDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageAudits){
          this.currentPageAudits = 1;
        }

        this.auditsDate = document.getElementById("auditsDate").value;
        if(this.auditsDate == "" && this.auditsUserId == ""){
          this.getAudits();
        }else if(this.auditsDate == "" && this.auditsUserId != ""){
          this.getAuditsUserId();
        }else if(this.auditsDate != "" && this.auditsUserId == ""){
          let tempArray = this.auditsDate.split(" ");
          this.auditsDate = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
          let tempEvent = {};
          this.audits = [];
          axios.get(url + "/api/audits/users/date/" + vm.auditsDate + "/" + vm.currentPageAudits + "/" + vm.recordsShownOnPageAudits) //
            .then((res) => {
              if(res.data.auditList.length == 0){
                vm.isListEmpty1 = true;
              }
              vm.totalAudits = Math.ceil(res.data.size / vm.recordsShownOnPageAudits);
              res.data.auditList.forEach(function (item) {
                tempEvent = {};

                tempEvent.actionPerformed = item.actionPerformed;
                tempEvent.principal = item.principal.replace("audit:", "");
                tempEvent.applicationCode = item.applicationCode;
                tempEvent.clientIpAddress = item.clientIpAddress;
                tempEvent.serverIpAddress = item.serverIpAddress;
                tempEvent.resourceOperatedUpon = item.resourceOperatedUpon;

                let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
                tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
                tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;
                
                vm.audits.push(tempEvent);
              });
            });
        }else if(this.auditsDate != "" && this.auditsUserId != ""){
          this.getAuditsUserIdDate();
        }
        this.changePageAudits = false;
      },
      getAuditsUserIdDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageAudits){
          this.currentPageAudits = 1;
        }
        
        this.auditsDate = document.getElementById("auditsDate").value;
        let tempArray = this.auditsDate.split(" ");
        this.auditsDate = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);  
        let tempEvent = {};
        this.audits = [];
        axios.get(url + "/api/audits/users/" + vm.auditsUserId + "/date/" + vm.auditsDate + "/" + vm.currentPageAudits + "/" + vm.recordsShownOnPageAudits) //
          .then((res) => {
            if(res.data.auditList.length == 0){
              vm.isListEmpty1 = true;
            }
            vm.totalAudits = Math.ceil(res.data.size / vm.recordsShownOnPageAudits);
            res.data.auditList.forEach(function (item) {
              tempEvent = {};

              tempEvent.actionPerformed = item.actionPerformed;
              tempEvent.principal = item.principal.replace("audit:", "");
              tempEvent.applicationCode = item.applicationCode;
              tempEvent.clientIpAddress = item.clientIpAddress;
              tempEvent.serverIpAddress = item.serverIpAddress;
              tempEvent.resourceOperatedUpon = item.resourceOperatedUpon;

              let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
              tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
              tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;
                
              vm.audits.push(tempEvent);
            });
          });
        this.changePageAudits = false;
      },
      removeAuditDate: function () {
        document.getElementById("auditDate").value = "";
        this.getAuditDate();
      },
      removeAuditsUserId: function () {
        this.auditsUserId = "";
        this.getAuditsDate();
      },
      removeAuditsDate: function () {
        document.getElementById("auditsDate").value = "";
        this.getAuditsDate();
      },
      changeLang: function () {
        if(this.lang == "EN"){
          this.margin = "margin-left: 30px;";
          this.lang = "فارسی";
          this.isRtl = false;
          this.deleteInputIcon = "right: 7%;";
          this.deleteInputIcon1 = "right: 10%;";
          this.dateNavText = this.dateNavEn;
          this.s0 = "Parsso";
          this.s1 = this.nameEN;
          this.s2 = "Exit";
          this.s3 = "Dashboard";
          this.s4 = "Services";
          this.s5 = "Groups";
          this.s6 = "Events";
          this.s7 = "Profile";
          this.s8 = "Quick Access";
          this.s9 = "Status Summary";
          this.s10 = "Rules";
          this.s11 = "Privacy";
          this.s12 = "Guide";
          this.s13 = "Users";
          this.s14 = "./dashboard?en";
          this.s15 = "./services?en";
          this.s16 = "./users?en";
          this.s17 = "ID";
          this.s18 = "Name";
          this.s19 = "Description";
          this.s20 = "Connect";
          this.s21 = "./groups?en";
          this.s22 = "./profile?en";
          this.s23 = "./privacy?en";
          this.s24 = "Configs";
          this.s25 = "./configs?en";
          this.s26 = "Date";
          this.s27 = "Search";
          this.s28 = "Action";
          this.s29 = "Application";
          this.s30 = "Time";
          this.s31 = "My Audits";
          this.s32 = "Users Audits";
          this.s33 = "UserId";
          this.s34 = "Example: admin";
          this.s35 = "Example: 1399/05/01";
          this.s36 = "./events?en";
          this.s37 = "UserId";
          this.s38 = "Date";
          this.s39 = "Successful Login";
          this.s40 = "Failed Login";
          this.s41 = "Service";
          this.s42 = "OS";
          this.s43 = "Browser";
          this.s44 = "Records a Page: ";
          this.s45 = "Audits";
          this.s46 = "/audits?en";
          this.s47 = "Go Back";
          this.s48 = "No Records Found";
          this.rolesText = "Roles";
          this.rolesURLText = "./roles?en";
          this.reportsText = "Reports";
          this.reportsURLText = "./reports?en";
        } else{
            this.margin = "margin-right: 30px;";
            this.lang = "EN";
            this.isRtl = true;
            this.deleteInputIcon = "left: 7%;";
            this.deleteInputIcon1 = "left: 10%;";
            this.dateNavText = this.dateNav;
            this.s0 = "پارسو";
            this.s1 = this.name;
            this.s2 = "خروج";
            this.s3 = "داشبورد";
            this.s4 = "سرویس ها";
            this.s5 = "گروه ها";
            this.s6 = "رویداد ها";
            this.s7 = "پروفایل";
            this.s8 = "دسترسی سریع";
            this.s9 = "خلاصه وضعیت";
            this.s10 = "قوانین";
            this.s11 = "حریم خصوصی";
            this.s12 = "راهنما";
            this.s13 = "کاربران";
            this.s14 = "./dashboard";
            this.s15 = "./services";
            this.s16 = "./users";
            this.s17 = "شناسه";
            this.s18 = "نام";
            this.s19 = "توضیحات";
            this.s20 = "اتصال";
            this.s21 = "./groups";
            this.s22 = "./profile";
            this.s23 = "./privacy";
            this.s24 = "پیکربندی";
            this.s25 = "./configs";
            this.s26 = "تاریخ";
            this.s27 = "جستجو";
            this.s28 = "عملیات";
            this.s29 = "برنامه";
            this.s30 = "زمان";
            this.s31 = "ممیزی های من";
            this.s32 = "ممیزی های کاربران";
            this.s33 = "شناسه";
            this.s34 = "مثال: admin";
            this.s35 = " مثال: 1399/05/01";
            this.s36 = "./events";
            this.s37 = "شناسه";
            this.s38 = "تاریخ";
            this.s39 = "ورود موفق";
            this.s40 = "ورود ناموفق";
            this.s41 = "سرویس";
            this.s42 = "سیستم عامل";
            this.s43 = "مرورگر";
            this.s44 = "تعداد رکورد ها: ";
            this.s45 = "ممیزی ها";
            this.s46 = "/audits";
            this.s47 = "بازگشت";
            this.s48 = "رکوردی یافت نشد";
            this.rolesText = "نقش ها";
            this.rolesURLText = "./roles";
            this.reportsText = "گزارش ها";
            this.reportsURLText = "./reports";
        }
      },
      div: function (a, b) {
        return parseInt((a / b));
      },
      gregorian_to_jalali: function (g_y, g_m, g_d) {
        var g_days_in_month = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        var j_days_in_month = [31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29];
        var jalali = [];
        var gy = g_y - 1600;
        var gm = g_m - 1;
        var gd = g_d - 1;
    
        var g_day_no = 365 * gy + this.div(gy + 3, 4) - this.div(gy + 99, 100) + this.div(gy + 399, 400);
    
        for (var i = 0; i < gm; ++i)
            g_day_no += g_days_in_month[i];
        if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)))
            g_day_no++;
        g_day_no += gd;
    
        var j_day_no = g_day_no - 79;
    
        var j_np = this.div(j_day_no, 12053);
        j_day_no = j_day_no % 12053;
    
        var jy = 979 + 33 * j_np + 4 * this.div(j_day_no, 1461);
    
        j_day_no %= 1461;
    
        if (j_day_no >= 366) {
            jy += this.div(j_day_no - 1, 365);
            j_day_no = (j_day_no - 1) % 365;
        }
        for (var i = 0; i < 11 && j_day_no >= j_days_in_month[i]; ++i)
            j_day_no -= j_days_in_month[i];
        var jm = i + 1;
        var jd = j_day_no + 1;
        jalali[0] = jy;
        jalali[1] = jm;
        jalali[2] = jd;
        return jalali;
      }
    },

    computed:{
      sortedAudit:function() {
          this.auditPage = this.audit;
          return this.auditPage;
      },
      sortedAudits:function() {
        this.auditsPage = this.audits;
        return this.auditsPage;
      }
    },

    watch : {
      currentPageAudit : function() {
        this.changePageAudit = true;
        this.getAuditDate();
      },
      currentPageAudits : function () {
        this.changePageAudits = true;
        this.getAuditsDate();
      }
    }
  })
})