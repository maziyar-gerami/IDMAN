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
      recordsShownOnPageEvents: 20,
      currentPageEvents: 1,
      totalEvents: 1,
      currentPageEvent: 1,
      totalEvent: 1,
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      activeItem: "myEvents",
      event: [],
      events: [],
      eventPage: [],
      eventsPage: [],
      changePageEvent: false,
      changePageEvents: false,
      userPicture: "images/PlaceholderUser.png",
      eventDate: "",
      eventsDate: "",
      eventsUserId: "",
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
      s28: "نوع رویداد",
      s29: "برنامه",
      s30: "زمان",
      s31: "رویداد های من",
      s32: "رویداد های کاربران",
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
      s47: "رکوردی یافت نشد",
      rolesText: "نقش ها",
      rolesURLText: "./roles",
      reportsText: "گزارش ها",
      reportsURLText: "./reports",
      publicmessagesText: "اعلان های عمومی",
      publicmessagesURLText: "./publicmessages",
      ticketingText: "پشتیبانی",
      ticketingURLText: "./ticketing",
      showMeeting: false,
      meetingInviteLinkStyle: "border-top-left-radius: 0;border-bottom-left-radius: 0;",
      meetingInviteLinkCopyStyle: "border-top-right-radius: 0;border-bottom-right-radius: 0;",
      meetingAdminLink: "",
      meetingGuestLink: "",
      meetingText: "جلسه مجازی",
      enterMeetingText: "ورود به جلسه",
      inviteToMeetingText: "دعوت به جلسه",
      copyText: "کپی",
      returnText: "بازگشت",
    },
    created: function () {
      this.setDateNav();
      this.getUserInfo();
      this.getUserPic();
      this.getEvent();
      this.getEvents();
      if(window.localStorage.getItem("lang") === null){
        window.localStorage.setItem("lang", "FA");
      }else if(window.localStorage.getItem("lang") === "EN") {
        this.changeLang();
      }
    },
    methods: {
      setDateNav: function () {
        this.dateNav = new persianDate().format("dddd، DD MMMM YYYY");
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
      openMeeting: function () {
        window.open(this.meetingAdminLink, "_blank").focus();
      },
      openOverlay: function () {
        document.getElementById("overlay").style.display = "block";
      },
      closeOverlay: function () {
        document.getElementById("overlay").style.display = "none";
      },
      copyMeetingLink: function () {
        let copyText = document.getElementById("copyMeetingLink");
        copyText.select();
        document.execCommand("copy");
        document.getElementById("copyMeetingLinkBtn").disabled = true;
        setTimeout(function(){ document.getElementById("copyMeetingLinkBtn").disabled = false; }, 3000);
      },
      isActive (menuItem) {
        return this.activeItem === menuItem
      },
      setActive (menuItem) {
        this.activeItem = menuItem
      },
      changeRecordsEvent: function(event) {
        this.recordsShownOnPage = event.target.value;
        this.getEventDate();
      },
      changeRecordsEvents: function(event) {
        this.recordsShownOnPageEvents = event.target.value;
        this.getEventsDate();
      },
      getUserInfo: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user") //
          .then((res) => {
            vm.username = res.data.userId;
            vm.name = res.data.displayName;
            vm.nameEN = res.data.firstName + " " + res.data.lastName;
            if(window.localStorage.getItem("lang") === null || window.localStorage.getItem("lang") === "FA"){
              vm.s1 = vm.name;
            }else if(window.localStorage.getItem("lang") === "EN") {
              vm.s1 = vm.nameEN;
            }
            if(res.data.skyRoom.enable){
              vm.showMeeting = true;
              vm.meetingAdminLink = res.data.skyRoom.presenter;
              vm.meetingGuestLink = res.data.skyRoom.students;
            }
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
      exportEvents: function(){
        url_ = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        vm.loader = true;
          axios({
            url: url_ + "/api/events/users/export",
            method: "GET",
            responseType: "blob",
          }).then((response) => {
            vm.loader = false;
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute("download", "events.xls");
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
      getEvents: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        let tempEvent = {};
        this.events = [];
        axios.get(url + "/api/events/users/" + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents)
        .then((res) => {
          if(res.data.eventList.length == 0){
            vm.isListEmpty1 = true;
          }
          vm.totalEvents = Math.ceil(res.data.size / vm.recordsShownOnPageEvents);
          res.data.eventList.forEach(function (item) {
            tempEvent = {};
            if(item.action == "Successful Login"){
              tempEvent.action = vm.s39;
              tempEvent.rowStyle = "";
            }else if(item.action == "Unsuccessful Login"){
              tempEvent.action = vm.s40;
              tempEvent.rowStyle = "color: red;";
            }

            tempEvent.userId = item.userId;
            tempEvent.application = item.application;
            tempEvent.clientIP = item.clientIP;
            tempEvent.serverIP = item.serverIP;
            tempEvent.service = item.service;

            let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
            tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
            tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;

            var osName = item.agentInfo.os.toLowerCase();
            tempEvent.osdes = item.agentInfo.os;
            if(osName.search("windows") != -1){
              tempEvent.oscol = "color: darkblue;";
              tempEvent.os = "fa fa-windows fa-stack-2x";
            }else if(osName.search("ios") != -1 || osName.search("mac") != -1){
              tempEvent.oscol = "color: dimgray;";
              tempEvent.os = "fa fa-apple fa-stack-2x";
            }else if(osName.search("android") != -1){
              tempEvent.oscol = "color: #56c736;";
              tempEvent.os = "fa fa-android fa-stack-2x";
            }else if(osName.search("linux") != -1 || osName.search("ubuntu") != -1 || osName.search("debian") != -1){
              tempEvent.oscol = "color: #f7c600;";
              tempEvent.os = "fa fa-linux fa-stack-2x";
            }else{
              tempEvent.os = "fa fa-question-circle fa-stack-2x";
            }

            var browserName = item.agentInfo.browser.toLowerCase();
            tempEvent.browserdes = item.agentInfo.browser;
            if(browserName.search("firefox") != -1){
              tempEvent.browsercol = "color: #f65d2b;";
              tempEvent.browser = "fa fa-firefox fa-stack-2x";
            }else if(browserName.search("chrome") != -1){
              tempEvent.browsercol = "color: #109855";
              tempEvent.browser = "fa fa-chrome fa-stack-2x";
            }else if(browserName.search("safari") != -1){
              tempEvent.browsercol = "color: #1688e2;";
              tempEvent.browser = "fa fa-safari fa-stack-2x";
            }else if(browserName.search("edge") != -1){
              tempEvent.browsercol = "color: #44ce90;";
              tempEvent.browser = "fa fa-edge fa-stack-2x";
            }else if(browserName.search("opera") != -1){
              tempEvent.browsercol = "color: #e21126;";
              tempEvent.browser = "fa fa-opera fa-stack-2x";
            }else if(browserName.search("internet explorer") != -1 || browserName.search("ie") != -1){
              tempEvent.browsercol = "color: #1db5e7;";
              tempEvent.browser = "fa fa-internet-explorer fa-stack-2x";
            }else{
              tempEvent.browser = "fa fa-question-circle fa-stack-2x";
            }

            vm.events.push(tempEvent);
          });
        });
        this.changePageEvents = false;
      },
      getEvent: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty = false;
        if(!this.changePageEvent){
          this.currentPageEvent = 1;
        }
        
        let tempEvent = {};
        this.event = [];
        axios.get(url + "/api/events/user/" + vm.currentPageEvent + "/" + vm.recordsShownOnPage) //
        .then((res) => {
          if(res.data.eventList.length == 0){
            vm.isListEmpty = true;
          }
          vm.totalEvent = Math.ceil(res.data.size / vm.recordsShownOnPage);
          res.data.eventList.forEach(function (item) {
            tempEvent = {};
            if(item.action == "Successful Login"){
              tempEvent.action = vm.s39;
              tempEvent.rowStyle = "";
            }else if(item.action == "Unsuccessful Login"){
              tempEvent.action = vm.s40;
              tempEvent.rowStyle = "color: red;";
            }

            tempEvent.userId = item.userId;
            tempEvent.application = item.application;
            tempEvent.clientIP = item.clientIP;
            tempEvent.service = item.service;

            let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
            tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
            tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;

            var osName = item.agentInfo.os.toLowerCase();
            tempEvent.osdes = item.agentInfo.os;
            if(osName.search("windows") != -1){
              tempEvent.oscol = "color: darkblue;";
              tempEvent.os = "fa fa-windows fa-stack-2x";
            }else if(osName.search("ios") != -1 || osName.search("mac") != -1){
              tempEvent.oscol = "color: dimgray;";
              tempEvent.os = "fa fa-apple fa-stack-2x";
            }else if(osName.search("android") != -1){
              tempEvent.oscol = "color: #56c736;";
              tempEvent.os = "fa fa-android fa-stack-2x";
            }else if(osName.search("linux") != -1 || osName.search("ubuntu") != -1 || osName.search("debian") != -1){
              tempEvent.oscol = "color: #f7c600;";
              tempEvent.os = "fa fa-linux fa-stack-2x";
            }else{
              tempEvent.os = "fa fa-question-circle fa-stack-2x";
            }

            var browserName = item.agentInfo.browser.toLowerCase();
            tempEvent.browserdes = item.agentInfo.browser;
            if(browserName.search("firefox") != -1){
              tempEvent.browsercol = "color: #f65d2b;";
              tempEvent.browser = "fa fa-firefox fa-stack-2x";
            }else if(browserName.search("chrome") != -1){
              tempEvent.browsercol = "color: #109855;";
              tempEvent.browser = "fa fa-chrome fa-stack-2x";
            }else if(browserName.search("safari") != -1){
              tempEvent.browsercol = "color: #1688e2;";
              tempEvent.browser = "fa fa-safari fa-stack-2x";
            }else if(browserName.search("edge") != -1){
              tempEvent.browsercol = "color: #44ce90;";
              tempEvent.browser = "fa fa-edge fa-stack-2x";
            }else if(browserName.search("opera") != -1){
              tempEvent.browsercol = "color: #e21126;";
              tempEvent.browser = "fa fa-opera fa-stack-2x";
            }else if(browserName.search("internet explorer") != -1 || browserName.search("ie") != -1){
              tempEvent.browsercol = "color: #1db5e7;";
              tempEvent.browser = "fa fa-internet-explorer fa-stack-2x";
            }else{
              tempEvent.browser = "fa fa-question-circle fa-stack-2x";
            }

            vm.event.push(tempEvent);
          });
        });
        this.changePageEvent = false;
      },
      getEventDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty = false;
        if(!this.changePageEvent){
          this.currentPageEvent = 1;
        }
        
        this.eventDate = document.getElementById("eventDate").value;
        if(this.eventDate == ""){
          this.getEvent();
        }else{
          let tempArray = this.eventDate.split(" ");
          this.eventDate = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
          let tempEvent = {};
          this.event = [];
          axios.get(url + "/api/events/user/date/" + vm.eventDate + "/" + vm.currentPageEvent + "/" + vm.recordsShownOnPage) //
            .then((res) => {
              if(res.data.eventList.length == 0){
                vm.isListEmpty = true;
              }
              vm.totalEvent = Math.ceil(res.data.size / vm.recordsShownOnPage);
              res.data.eventList.forEach(function (item) {
                tempEvent = {};
                if(item.action == "Successful Login"){
                  tempEvent.action = vm.s39;
                  tempEvent.rowStyle = "";
                }else if(item.action == "Unsuccessful Login"){
                  tempEvent.action = vm.s40;
                  tempEvent.rowStyle = "color: red;";
                }

                tempEvent.userId = item.userId;
                tempEvent.application = item.application;
                tempEvent.clientIP = item.clientIP;
                tempEvent.service = item.service;

                let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
                tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
                tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;

                var osName = item.agentInfo.os.toLowerCase();
                tempEvent.osdes = item.agentInfo.os;
                if(osName.search("windows") != -1){
                  tempEvent.oscol = "color: darkblue;";
                  tempEvent.os = "fa fa-windows fa-stack-2x";
                }else if(osName.search("ios") != -1 || osName.search("mac") != -1){
                  tempEvent.oscol = "color: dimgray;";
                  tempEvent.os = "fa fa-apple fa-stack-2x";
                }else if(osName.search("android") != -1){
                  tempEvent.oscol = "color: #56c736;";
                  tempEvent.os = "fa fa-android fa-stack-2x";
                }else if(osName.search("linux") != -1 || osName.search("ubuntu") != -1 || osName.search("debian") != -1){
                  tempEvent.oscol = "color: #f7c600;";
                  tempEvent.os = "fa fa-linux fa-stack-2x";
                }else{
                  tempEvent.os = "fa fa-question-circle fa-stack-2x";
                }

                var browserName = item.agentInfo.browser.toLowerCase();
                tempEvent.browserdes = item.agentInfo.browser;
                if(browserName.search("firefox") != -1){
                  tempEvent.browsercol = "color: #f65d2b;";
                  tempEvent.browser = "fa fa-firefox fa-stack-2x";
                }else if(browserName.search("chrome") != -1){
                  tempEvent.browsercol = "color: #109855;";
                  tempEvent.browser = "fa fa-chrome fa-stack-2x";
                }else if(browserName.search("safari") != -1){
                  tempEvent.browsercol = "color: #1688e2;";
                  tempEvent.browser = "fa fa-safari fa-stack-2x";
                }else if(browserName.search("edge") != -1){
                  tempEvent.browsercol = "color: #44ce90;";
                  tempEvent.browser = "fa fa-edge fa-stack-2x";
                }else if(browserName.search("opera") != -1){
                  tempEvent.browsercol = "color: #e21126;";
                  tempEvent.browser = "fa fa-opera fa-stack-2x";
                }else if(browserName.search("internet explorer") != -1 || browserName.search("ie") != -1){
                  tempEvent.browsercol = "color: #1db5e7;";
                  tempEvent.browser = "fa fa-internet-explorer fa-stack-2x";
                }else{
                  tempEvent.browser = "fa fa-question-circle fa-stack-2x";
                }

                vm.event.push(tempEvent);
              });
            });
        }
        this.changePageEvent = false;
      },
      getEventsUserId: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        this.eventsDate = document.getElementById("eventsDate").value;
        if(this.eventsDate == "" && this.eventsUserId == ""){
          this.getEvents();
        }else if(this.eventsDate == "" && this.eventsUserId != ""){
          let tempEvent = {};
          this.events = [];
          axios.get(url + "/api/events/users/" + vm.eventsUserId + "/" + + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents) //
          .then((res) => {
            if(res.data.eventList.length == 0){
              vm.isListEmpty1 = true;
            }
            vm.totalEvents = Math.ceil(res.data.size / vm.recordsShownOnPageEvents);
            res.data.eventList.forEach(function (item) {
              tempEvent = {};
              if(item.action == "Successful Login"){
                tempEvent.action = vm.s39;
                tempEvent.rowStyle = "";
              }else if(item.action == "Unsuccessful Login"){
                tempEvent.action = vm.s40;
                tempEvent.rowStyle = "color: red;";
              }

              tempEvent.userId = item.userId;
              tempEvent.application = item.application;
              tempEvent.clientIP = item.clientIP;
              tempEvent.serverIP = item.serverIP;
              tempEvent.service = item.service;

              let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
              tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
              tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;

              var osName = item.agentInfo.os.toLowerCase();
              tempEvent.osdes = item.agentInfo.os;
              if(osName.search("windows") != -1){
                tempEvent.oscol = "color: darkblue;";
                tempEvent.os = "fa fa-windows fa-stack-2x";
              }else if(osName.search("ios") != -1 || osName.search("mac") != -1){
                tempEvent.oscol = "color: dimgray;";
                tempEvent.os = "fa fa-apple fa-stack-2x";
              }else if(osName.search("android") != -1){
                tempEvent.oscol = "color: #56c736;";
                tempEvent.os = "fa fa-android fa-stack-2x";
              }else if(osName.search("linux") != -1 || osName.search("ubuntu") != -1 || osName.search("debian") != -1){
                tempEvent.oscol = "color: #f7c600;";
                tempEvent.os = "fa fa-linux fa-stack-2x";
              }else{
                tempEvent.os = "fa fa-question-circle fa-stack-2x";
              }

              var browserName = item.agentInfo.browser.toLowerCase();
              tempEvent.browserdes = item.agentInfo.browser;
              if(browserName.search("firefox") != -1){
                tempEvent.browsercol = "color: #f65d2b;";
                tempEvent.browser = "fa fa-firefox fa-stack-2x";
              }else if(browserName.search("chrome") != -1){
                tempEvent.browsercol = "color: #109855;";
                tempEvent.browser = "fa fa-chrome fa-stack-2x";
              }else if(browserName.search("safari") != -1){
                tempEvent.browsercol = "color: #1688e2;";
                tempEvent.browser = "fa fa-safari fa-stack-2x";
              }else if(browserName.search("edge") != -1){
                tempEvent.browsercol = "color: #44ce90;";
                tempEvent.browser = "fa fa-edge fa-stack-2x";
              }else if(browserName.search("opera") != -1){
                tempEvent.browsercol = "color: #e21126;";
                tempEvent.browser = "fa fa-opera fa-stack-2x";
              }else if(browserName.search("internet explorer") != -1 || browserName.search("ie") != -1){
                tempEvent.browsercol = "color: #1db5e7;";
                tempEvent.browser = "fa fa-internet-explorer fa-stack-2x";
              }else{
                tempEvent.browser = "fa fa-question-circle fa-stack-2x";
              }

              vm.events.push(tempEvent);
            });
          });
        }else if(this.eventsDate != "" && this.eventsUserId == ""){
          this.getEventsDate();
        }else if(this.eventsDate != "" && this.eventsUserId != ""){
          this.getEventsUserIdDate();
        }
        this.changePageEvents = false;
      },
      getEventsDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        this.eventsDate = document.getElementById("eventsDate").value;
        if(this.eventsDate == "" && this.eventsUserId == ""){
          this.getEvents();
        }else if(this.eventsDate == "" && this.eventsUserId != ""){
          this.getEventsUserId();
        }else if(this.eventsDate != "" && this.eventsUserId == ""){
          let tempArray = this.eventsDate.split(" ");
          this.eventsDate = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
          let tempEvent = {};
          this.events = [];
          axios.get(url + "/api/events/users/date/" + vm.eventsDate + "/" + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents) //
            .then((res) => {
              if(res.data.eventList.length == 0){
                vm.isListEmpty1 = true;
              }
              vm.totalEvents = Math.ceil(res.data.size / vm.recordsShownOnPageEvents);
              res.data.eventList.forEach(function (item) {
                tempEvent = {};
                if(item.action == "Successful Login"){
                  tempEvent.action = vm.s39;
                  tempEvent.rowStyle = "";
                }else if(item.action == "Unsuccessful Login"){
                  tempEvent.action = vm.s40;
                  tempEvent.rowStyle = "color: red;";
                }

                tempEvent.userId = item.userId;
                tempEvent.application = item.application;
                tempEvent.clientIP = item.clientIP;
                tempEvent.serverIP = item.serverIP;
                tempEvent.service = item.service;

                let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
                tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
                tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;

                var osName = item.agentInfo.os.toLowerCase();
                tempEvent.osdes = item.agentInfo.os;
                if(osName.search("windows") != -1){
                  tempEvent.oscol = "color: darkblue;";
                  tempEvent.os = "fa fa-windows fa-stack-2x";
                }else if(osName.search("ios") != -1 || osName.search("mac") != -1){
                  tempEvent.oscol = "color: dimgray;";
                  tempEvent.os = "fa fa-apple fa-stack-2x";
                }else if(osName.search("android") != -1){
                  tempEvent.oscol = "color: #56c736;";
                  tempEvent.os = "fa fa-android fa-stack-2x";
                }else if(osName.search("linux") != -1 || osName.search("ubuntu") != -1 || osName.search("debian") != -1){
                  tempEvent.oscol = "color: #f7c600;";
                  tempEvent.os = "fa fa-linux fa-stack-2x";
                }else{
                  tempEvent.os = "fa fa-question-circle fa-stack-2x";
                }

                var browserName = item.agentInfo.browser.toLowerCase();
                tempEvent.browserdes = item.agentInfo.browser;
                if(browserName.search("firefox") != -1){
                  tempEvent.browsercol = "color: #f65d2b;";
                  tempEvent.browser = "fa fa-firefox fa-stack-2x";
                }else if(browserName.search("chrome") != -1){
                  tempEvent.browsercol = "color: #109855;";
                  tempEvent.browser = "fa fa-chrome fa-stack-2x";
                }else if(browserName.search("safari") != -1){
                  tempEvent.browsercol = "color: #1688e2;";
                  tempEvent.browser = "fa fa-safari fa-stack-2x";
                }else if(browserName.search("edge") != -1){
                  tempEvent.browsercol = "color: #44ce90;";
                  tempEvent.browser = "fa fa-edge fa-stack-2x";
                }else if(browserName.search("opera") != -1){
                  tempEvent.browsercol = "color: #e21126;";
                  tempEvent.browser = "fa fa-opera fa-stack-2x";
                }else if(browserName.search("internet explorer") != -1 || browserName.search("ie") != -1){
                  tempEvent.browsercol = "color: #1db5e7;";
                  tempEvent.browser = "fa fa-internet-explorer fa-stack-2x";
                }else{
                  tempEvent.browser = "fa fa-question-circle fa-stack-2x";
                }

                vm.events.push(tempEvent);
              });
            });
        }else if(this.eventsDate != "" && this.eventsUserId != ""){
          this.getEventsUserIdDate();
        }
        this.changePageEvents = false;
      },
      getEventsUserIdDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        this.isListEmpty1 = false;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        this.eventsDate = document.getElementById("eventsDate").value;
        let tempArray = this.eventsDate.split(" ");
        this.eventsDate = this.faNumToEnNum(tempArray[1]) + this.faMonthtoNumMonth(tempArray[0]) + this.faNumToEnNum(tempArray[2]);
        let tempEvent = {};
        this.events = [];
        axios.get(url + "/api/events/users/" + vm.eventsUserId + "/date/" + vm.eventsDate + "/" + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents) //
            .then((res) => {
              if(res.data.eventList.length == 0){
                vm.isListEmpty1 = true;
              }
              vm.totalEvents = Math.ceil(res.data.size / vm.recordsShownOnPageEvents);
              res.data.eventList.forEach(function (item) {
                tempEvent = {};
                if(item.action == "Successful Login"){
                  tempEvent.action = vm.s39;
                  tempEvent.rowStyle = "";
                }else if(item.action == "Unsuccessful Login"){
                  tempEvent.action = vm.s40;
                  tempEvent.rowStyle = "color: red;";
                }

                tempEvent.userId = item.userId;
                tempEvent.application = item.application;
                tempEvent.clientIP = item.clientIP;
                tempEvent.serverIP = item.serverIP;
                tempEvent.service = item.service;

                let dateArray = vm.gregorian_to_jalali(item.time.year, item.time.month, item.time.day)
                tempEvent.date = dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2];
                tempEvent.clock = item.time.hours + ":" + item.time.minutes + ":" + item.time.seconds;

                var osName = item.agentInfo.os.toLowerCase();
                tempEvent.osdes = item.agentInfo.os;
                if(osName.search("windows") != -1){
                  tempEvent.oscol = "color: darkblue;";
                  tempEvent.os = "fa fa-windows fa-stack-2x";
                }else if(osName.search("ios") != -1 || osName.search("mac") != -1){
                  tempEvent.oscol = "color: dimgray;";
                  tempEvent.os = "fa fa-apple fa-stack-2x";
                }else if(osName.search("android") != -1){
                  tempEvent.oscol = "color: #56c736;";
                  tempEvent.os = "fa fa-android fa-stack-2x";
                }else if(osName.search("linux") != -1 || osName.search("ubuntu") != -1 || osName.search("debian") != -1){
                  tempEvent.oscol = "color: #f7c600;";
                  tempEvent.os = "fa fa-linux fa-stack-2x";
                }else{
                  tempEvent.os = "fa fa-question-circle fa-stack-2x";
                }

                var browserName = item.agentInfo.browser.toLowerCase();
                tempEvent.browserdes = item.agentInfo.browser;
                if(browserName.search("firefox") != -1){
                  tempEvent.browsercol = "color: #f65d2b;";
                  tempEvent.browser = "fa fa-firefox fa-stack-2x";
                }else if(browserName.search("chrome") != -1){
                  tempEvent.browsercol = "color: #109855;";
                  tempEvent.browser = "fa fa-chrome fa-stack-2x";
                }else if(browserName.search("safari") != -1){
                  tempEvent.browsercol = "color: #1688e2;";
                  tempEvent.browser = "fa fa-safari fa-stack-2x";
                }else if(browserName.search("edge") != -1){
                  tempEvent.browsercol = "color: #44ce90;";
                  tempEvent.browser = "fa fa-edge fa-stack-2x";
                }else if(browserName.search("opera") != -1){
                  tempEvent.browsercol = "color: #e21126;";
                  tempEvent.browser = "fa fa-opera fa-stack-2x";
                }else if(browserName.search("internet explorer") != -1 || browserName.search("ie") != -1){
                  tempEvent.browsercol = "color: #1db5e7;";
                  tempEvent.browser = "fa fa-internet-explorer fa-stack-2x";
                }else{
                  tempEvent.browser = "fa fa-question-circle fa-stack-2x";
                }

                vm.events.push(tempEvent);
              });
            });
        this.changePageEvents = false;
      },
      removeEventDate: function () {
        document.getElementById("eventDate").value = "";
        this.getEventDate();
      },
      removeEventsUserId: function () {
        this.eventsUserId = "";
        this.getEventsDate();
      },
      removeEventsDate: function () {
        document.getElementById("eventsDate").value = "";
        this.getEventsDate();
      },
      changeLang: function () {
        if(this.lang == "EN"){
          window.localStorage.setItem("lang", "EN");
          this.getEvents();
          this.getEvent();
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
          this.s17 = "ID";
          this.s18 = "Name";
          this.s19 = "Description";
          this.s20 = "Connect";
          this.s24 = "Configs";
          this.s26 = "Date";
          this.s27 = "Search";
          this.s28 = "Event Type";
          this.s29 = "Application";
          this.s30 = "Time";
          this.s31 = "My Events";
          this.s32 = "Users Events";
          this.s33 = "UserId";
          this.s34 = "Example: admin";
          this.s35 = "Example: 1399/05/01";
          this.s37 = "UserId";
          this.s38 = "Date";
          this.s39 = "Successful Login";
          this.s40 = "Failed Login";
          this.s41 = "Service";
          this.s42 = "OS";
          this.s43 = "Browser";
          this.s44 = "Records a Page: ";
          this.s45 = "Audits";
          this.s47 = "No Records Found";
          this.rolesText = "Roles";
          this.reportsText = "Reports";
          this.publicmessagesText = "Public Messages";
          this.ticketingText = "Ticketing";
          this.meetingInviteLinkStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
          this.meetingInviteLinkCopyStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
          this.meetingText = "Meeting";
          this.enterMeetingText = "Enter Meeting";
          this.inviteToMeetingText = "Invite To Meeting";
          this.copyText = "Copy";
          this.returnText = "Return";
        }else {
            window.localStorage.setItem("lang", "FA");
            this.getEvents();
            this.getEvent();
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
            this.s17 = "شناسه";
            this.s18 = "نام";
            this.s19 = "توضیحات";
            this.s20 = "اتصال";
            this.s24 = "پیکربندی";
            this.s26 = "تاریخ";
            this.s27 = "جستجو";
            this.s28 = "نوع رویداد";
            this.s29 = "برنامه";
            this.s30 = "زمان";
            this.s31 = "رویداد های من";
            this.s32 = "رویداد های کاربران";
            this.s33 = "شناسه";
            this.s34 = "مثال: admin";
            this.s35 = " مثال: 1399/05/01";
            this.s37 = "شناسه";
            this.s38 = "تاریخ";
            this.s39 = "ورود موفق";
            this.s40 = "ورود ناموفق";
            this.s41 = "سرویس";
            this.s42 = "سیستم عامل";
            this.s43 = "مرورگر";
            this.s44 = "تعداد رکورد ها: ";
            this.s45 = "ممیزی ها";
            this.s47 = "رکوردی یافت نشد";
            this.rolesText = "نقش ها";
            this.reportsText = "گزارش ها";
            this.publicmessagesText = "اعلان های عمومی";
            this.ticketingText = "پشتیبانی";
            this.meetingInviteLinkStyle = "border-top-left-radius: 0;border-bottom-left-radius: 0;";
            this.meetingInviteLinkCopyStyle = "border-top-right-radius: 0;border-bottom-right-radius: 0;";
            this.meetingText = "جلسه مجازی";
            this.enterMeetingText = "ورود به جلسه";
            this.inviteToMeetingText = "دعوت به جلسه";
            this.copyText = "کپی";
            this.returnText = "بازگشت";
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
      sortedEvent:function() {
          this.eventPage = this.event;
          return this.eventPage;
      },
      sortedEvents:function() {
        this.eventsPage = this.events;
        return this.eventsPage;
      }
    },

    watch : {
      currentPageEvent : function() {
        this.changePageEvent = true;
        this.getEventDate();
      },
      currentPageEvents : function () {
        this.changePageEvents = true;
        this.getEventsDate();
      }
    }
  })
})