function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
      var dropdowns = document.getElementsByClassName("dropdown-content");
      var i;
      for (i = 0; i < dropdowns.length; ++i) {
        var openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
          openDropdown.classList.remove('show');
        }
      }
  }
}
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
      menuS: false,
      menuSA: false,
      changePageEvent: false,
      changePageEvents: false,
      userPicture: "images/PlaceholderUser.png",
      eventDate: "",
      eventsDate: "",
      tempEventDate: "",
      tempEventsDate: "",
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
      s22: "./settings",
      s23: "./privacy",
      s24: "پیکربندی",
      s25: "./configs",
      s26: "تاریخ: ",
      s27: "جستجو",
      s28: "نوع رویداد",
      s29: "برنامه",
      s30: "زمان",
      s31: "رویداد های من",
      s32: "رویداد های کاربران",
      s33: "شناسه: ",
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
    },
    created: function () {
      this.getUserInfo();
      this.isAdmin();
      this.getUserPic();
      this.getEvent();
      this.getEvents();
      if(typeof this.$route.query.en !== 'undefined'){
        this.changeLang()
      }
    },
    methods: {
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
      isAdmin: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user/isAdmin") //
          .then((res) => {
            if(res.data == "0"){
              vm.menuS = true;
              vm.menuSA = true;
            }else if(res.data == "1"){
                vm.menuS = true;
            }
          });
      },
      getUserInfo: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        axios.get(url + "/api/user") //
          .then((res) => {
            vm.userInfo = res.data;
            vm.username = vm.userInfo.userId;
            vm.name = vm.userInfo.displayName;
            vm.nameEN = vm.userInfo.firstName + vm.userInfo.lastName;
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
      exportEvents: function(){
        url_ = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        vm.loader = true;
          axios({
            url: url_ + "/api/events/export",
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
      getEvents: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        var tempEvent = {};
        this.events = [];
        axios.get(url + "/api/events/" + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents)
        .then((res) => {
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
        if(!this.changePageEvent){
          this.currentPageEvent = 1;
        }
        
        var tempEvent = {};
        this.event = [];
        axios.get(url + "/api/events/user/" + vm.currentPageEvent + "/" + vm.recordsShownOnPage) //
        .then((res) => {
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
        if(!this.changePageEvent){
          this.currentPageEvent = 1;
        }
        
        if(this.tempEventDate == ""){
          this.getEvent();
        }else{
          if((this.tempEventDate.match(/\//g) || []).length == 2){
            var tempEvent = {};
            this.event = [];
            var date = this.tempEventDate.split('/');
            if(date[0].length != 4){
              date[0] = "13" + date[0];
            }
            if(date[1].length != 2){
              date[1] = "0" + date[1];
            }
            if(date[2].length != 2){
              date[2] = "0" + date[2];
            }
            this.eventDate = date[2] + date[1] + date[0];
            axios.get(url + "/api/events/user/date/" + vm.eventDate + "/" + vm.currentPageEvent + "/" + vm.recordsShownOnPage) //
            .then((res) => {
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
          }else{
            console.log("Date Format is Wrong!");
          }
        }
        this.changePageEvent = false;
      },
      getEventsUserId: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        if(this.tempEventsDate == "" && this.eventsUserId == ""){
          this.getEvents();
        }else if(this.tempEventsDate == "" && this.eventsUserId != ""){
          var tempEvent = {};
          this.events = [];
          axios.get(url + "/api/events/users/" + vm.eventsUserId + "/" + + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents) //
          .then((res) => {
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
        }else if(this.tempEventsDate != "" && this.eventsUserId == ""){
          this.getEventsDate();
        }else if(this.tempEventsDate != "" && this.eventsUserId != ""){
          this.getEventsUserIdDate();
        }
        this.changePageEvents = false;
      },
      getEventsDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        if(this.tempEventsDate == "" && this.eventsUserId == ""){
          this.getEvents();
        }else if(this.tempEventsDate == "" && this.eventsUserId != ""){
          this.getEventsUserId();
        }else if(this.tempEventsDate != "" && this.eventsUserId == ""){
          if((this.tempEventsDate.match(/\//g) || []).length == 2){
            var tempEvent = {};
            this.events = [];
            var date = this.tempEventsDate.split('/');
            if(date[0].length != 4){
              date[0] = "13" + date[0];
            }
            if(date[1].length != 2){
              date[1] = "0" + date[1];
            }
            if(date[2].length != 2){
              date[2] = "0" + date[2];
            }
            this.eventsDate = date[2] + date[1] + date[0];
            axios.get(url + "/api/events/date/" + vm.eventsDate + "/" + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents) //
            .then((res) => {
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
          }else{
            console.log("Date Format is Wrong!");
          }
        }else if(this.tempEventsDate != "" && this.eventsUserId != ""){
          this.getEventsUserIdDate();
        }
        this.changePageEvents = false;
      },
      getEventsUserIdDate: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
        var vm = this;
        if(!this.changePageEvents){
          this.currentPageEvents = 1;
        }
        
        if((this.tempEventsDate.match(/\//g) || []).length == 2){
            var tempEvent = {};
            this.events = [];
            var date = this.tempEventsDate.split('/');
            if(date[0].length != 4){
              date[0] = "13" + date[0];
            }
            if(date[1].length != 2){
              date[1] = "0" + date[1];
            }
            if(date[2].length != 2){
              date[2] = "0" + date[2];
            }
            this.eventsDate = date[2] + date[1] + date[0];
            axios.get(url + "/api/events/users/" + vm.eventsUserId + "/date/" + vm.eventsDate + "/" + vm.currentPageEvents + "/" + vm.recordsShownOnPageEvents) //
            .then((res) => {
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
          }else{
            console.log("Date Format is Wrong!");
          }
        this.changePageEvents = false;
      },
      changeLang: function () {
        if(this.lang == "EN"){
          this.getEvents();
          this.getEvent();
          this.margin = "margin-left: 30px;";
          this.lang = "فارسی";
          this.isRtl = false;
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
          this.s22 = "./settings?en";
          this.s23 = "./privacy?en";
          this.s24 = "Configs";
          this.s25 = "./configs?en";
          this.s26 = "Date: ";
          this.s27 = "Search";
          this.s28 = "Event Type";
          this.s29 = "Application";
          this.s30 = "Time";
          this.s31 = "My Events";
          this.s32 = "Users Events";
          this.s33 = "UserId: ";
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
        } else{
            this.getEvents();
            this.getEvent();
            this.margin = "margin-right: 30px;";
            this.lang = "EN";
            this.isRtl = true;
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
            this.s22 = "./settings";
            this.s23 = "./privacy";
            this.s24 = "پیکربندی";
            this.s25 = "./configs";
            this.s26 = "تاریخ: ";
            this.s27 = "جستجو";
            this.s28 = "نوع رویداد";
            this.s29 = "برنامه";
            this.s30 = "زمان";
            this.s31 = "رویداد های من";
            this.s32 = "رویداد های کاربران";
            this.s33 = "شناسه: ";
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